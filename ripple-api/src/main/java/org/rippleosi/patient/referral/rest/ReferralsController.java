/*
 * Copyright 2015 Ripple OSI
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package org.rippleosi.patient.referral.rest;

import java.util.List;

import org.rippleosi.common.exception.DataNotFoundException;
import org.rippleosi.common.types.RepoSourceType;
import org.rippleosi.common.types.RepoSourceTypes;
import org.rippleosi.common.types.lookup.RepoSourceLookupFactory;
import org.rippleosi.patient.referral.model.ReferralDetails;
import org.rippleosi.patient.referral.model.ReferralSummary;
import org.rippleosi.patient.referral.search.ReferralSearch;
import org.rippleosi.patient.referral.search.ReferralSearchFactory;
import org.rippleosi.patient.referral.store.ReferralStore;
import org.rippleosi.patient.referral.store.ReferralStoreFactory;
import org.rippleosi.patient.referral.types.ReferralStates;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 */
@RestController
@RequestMapping("/patients/{patientId}/referrals")
public class ReferralsController {

    @Autowired
    private RepoSourceLookupFactory repoSourceLookup;
    
    @Autowired
    private ReferralSearchFactory referralSearchFactory;

    @Autowired
    private ReferralStoreFactory referralStoreFactory;

    @RequestMapping(method = RequestMethod.GET)
    public List<ReferralSummary> findAllReferrals(@PathVariable("patientId") String patientId,
                                                  @RequestParam(required = false) String source) {

        ReferralSearch referralSearch = referralSearchFactory.select(RepoSourceTypes.MARAND);
        List<ReferralSummary> openEHRReferrals = referralSearch.findAllReferrals(patientId);

        referralSearch = referralSearchFactory.select(RepoSourceTypes.SCCIS);
        List<ReferralSummary> scCISReferrals = referralSearch.findAllReferrals(patientId);

        openEHRReferrals.addAll(scCISReferrals);
        return openEHRReferrals;
    }

    @RequestMapping(value = "/{referralId}", method = RequestMethod.GET)
    public ReferralDetails findReferral(@PathVariable("patientId") String patientId,
                                        @PathVariable("referralId") String referralId,
                                        @RequestParam(required = false) String source) {
        final RepoSourceType sourceType = repoSourceLookup.lookup(source);
        ReferralSearch referralSearch = referralSearchFactory.select(sourceType);

        ReferralDetails referral = referralSearch.findReferral(patientId, referralId);
        if (referral.getReferralState().equalsIgnoreCase("completed")) {
            // If this is a response then obtain the original request.
            ReferralDetails originalReferral = referralSearch.findReferralByReference(patientId, referral.getReference());
            // Populate the missing fields in the Referral Response from the original request
            referral.setDateResponded(referral.getDateOfReferral());
            referral.setDateOfReferral(originalReferral.getDateCreated());
            referral.setReason(originalReferral.getReason());
        }
        else
        {
            // This is a referral Request that's been passed in.
            // Check if any responses already exist for it. - Only for Marand at present
            if (sourceType == RepoSourceTypes.MARAND) {
                if (referral.getResponseSourceId() == null || referral.getResponseSourceId().equalsIgnoreCase("")) {
                    String responseId = null;
                    try {
                        ReferralDetails possibleReferralResponse =
                                referralSearch.findReferralByReference(patientId, referral.getReference(),
                                        ReferralStates.COMPLETED.getReferralStateCode());
                        responseId = possibleReferralResponse.getSourceId();
                    } catch (DataNotFoundException dnfe) {
                    }  // Do Nothing
                    // Set the Response Source ID so that the UI knows if a request has any responses
                    referral.setResponseSourceId(responseId);
                }
            }
        }


        return referralSearch.findReferral(patientId, referralId);
    }

    @RequestMapping(method = RequestMethod.POST)
    public void createReferral(@PathVariable("patientId") String patientId,
                               @RequestParam(required = false) String source,
                               @RequestBody ReferralDetails referral) {
 //       final RepoSourceType sourceType = repoSourceLookup.lookup(source);
        // for the time being until SC CIS is used, store referrals in Marand
        // TODO: Default to SC CIS in Sprint 3
        final RepoSourceType sourceType = RepoSourceTypes.MARAND;
        ReferralStore referralStore = referralStoreFactory.select(sourceType);

        if (referral.getReferralState().equalsIgnoreCase("planned")) {
            if (referral.getReference() == null || referral.getReference().equals(""))
                referral.generateReference();
        }
        referralStore.create(patientId, referral);
    }

    @RequestMapping(method = RequestMethod.PUT)
    public void updateReferral(@PathVariable("patientId") String patientId,
                               @RequestParam(required = false) String source,
                               @RequestBody ReferralDetails referral) {
        //       final RepoSourceType sourceType = repoSourceLookup.lookup(source);
        // for the time being until SC CIS is used, store referrals in Marand
        // TODO: Default to SC CIS in Sprint 3
        final RepoSourceType sourceType = RepoSourceTypes.MARAND;
        ReferralStore referralStore = referralStoreFactory.select(sourceType);

        referralStore.update(patientId, referral);
    }
}
