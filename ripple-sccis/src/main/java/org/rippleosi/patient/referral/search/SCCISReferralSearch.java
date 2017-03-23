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
package org.rippleosi.patient.referral.search;


import org.rippleosi.common.service.AbstractSCCISService;
import org.rippleosi.patient.referral.model.ReferralDetails;
import org.rippleosi.patient.referral.model.ReferralSummary;
import org.rippleosi.patient.referral.types.ReferralStates;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class SCCISReferralSearch extends AbstractSCCISService implements ReferralSearch {

    @Override
    public List<ReferralSummary> findAllReferrals(String patientId) {

        SCCISReferralSummaryTransformer transformer = new SCCISReferralSummaryTransformer();

        return findData(patientId, transformer);
    }

    @Override
    public ReferralDetails findReferral(String patientId, String referralId) {

        SCCISReferralDetailsTransformer transformer = new SCCISReferralDetailsTransformer(referralId);

        return findData(patientId, transformer);
    }

    @Override
    public ReferralDetails findReferralByReference(String patientId, String referralRef, String stateCode) {

        // For SC CIS, use the referral Reference as the referral ID (SourceId).  There are no referral responses yet
        // so this is acceptable for now.  Also all referrals have a state of "Planned" at present.
        return findReferral(patientId, referralRef);

    }

    @Override
    public ReferralDetails findReferralByReference(String patientId, String referralRef) {
        // Default to Referral Requests if the state hasn't been supplied
        return findReferralByReference(patientId, referralRef, ReferralStates.PLANNED.getReferralStateCode());
    }
}
