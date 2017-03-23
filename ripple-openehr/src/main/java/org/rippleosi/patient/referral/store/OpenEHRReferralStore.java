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
package org.rippleosi.patient.referral.store;

import java.util.HashMap;
import java.util.Map;

import org.apache.camel.Consume;
import org.rippleosi.common.service.AbstractOpenEhrService;
import org.rippleosi.common.service.strategies.store.CreateStrategy;
import org.rippleosi.common.service.strategies.store.DefaultStoreStrategy;
import org.rippleosi.common.service.strategies.store.UpdateStrategy;
import org.rippleosi.common.util.DateFormatter;
import org.rippleosi.patient.referral.model.ReferralDetails;
import org.rippleosi.patient.referral.types.ReferralStates;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 */
@Service
public class OpenEHRReferralStore extends AbstractOpenEhrService implements ReferralStore {

    @Value("${c4hOpenEHR.referralsTemplate}")
    private String referralsTemplate;

    @Override
    @Consume(uri = "activemq:Consumer.C4HOpenEHR.VirtualTopic.Marand.Referrals.Create")
    public void create(String patientId, ReferralDetails referral) {

        Map<String,Object> content = createFlatJsonContent(referral);

        CreateStrategy createStrategy = new DefaultStoreStrategy(patientId, referralsTemplate, content);

        createData(createStrategy);
    }

    @Override
    @Consume(uri = "activemq:Consumer.C4HOpenEHR.VirtualTopic.Marand.Referrals.Update")
    public void update(String patientId, ReferralDetails referral) {

        Map<String,Object> content = createFlatJsonContent(referral);

        UpdateStrategy updateStrategy = new DefaultStoreStrategy(referral.getSourceId(), patientId, referralsTemplate, content);

        updateData(updateStrategy);
    }

    private Map<String,Object> createFlatJsonContent(ReferralDetails referral) {
        final String REFERRALS_PREFIX = "request_for_service/referral_details/service_request:0/";
        final String INSTRUCTION_DETAILS_PREFIX = "request_for_service/referral_details/service:0/instruction_details:0|";
        final String ISM_TRANS_PREFIX = "request_for_service/referral_details/service:0/ism_transition/";
        final String SERVICE_PREFIX = "request_for_service/referral_details/service:0/";

        Map<String, Object> content = new HashMap<>();

        content.put("ctx/language", "en");
        content.put("ctx/territory", "GB");



        content.put("ctx/composer_name", referral.getAuthor());
        content.put("ctx/id_namespace", "NHS-UK");

        content.put(REFERRALS_PREFIX + "request:0/service_name", referral.getReferralType());
        content.put(REFERRALS_PREFIX + "request:0/reason_for_request", referral.getReason());
        content.put(REFERRALS_PREFIX + "request:0/timing", "R5/2016-10-04T21:00:00Z/P1M"); // Boilerplate value
        content.put(REFERRALS_PREFIX + "requestor/person_name/unstructured_name", referral.getReferralFrom());
        content.put(REFERRALS_PREFIX + "receiver_identifier", referral.getReference());
        content.put(REFERRALS_PREFIX + "receiver/name_of_organisation", referral.getReferralTo());
        content.put(REFERRALS_PREFIX + "narrative", referral.getReferralType());  // set to the same value as Type

        // ACTION class in OpenEHR - Boilerplate code
        content.put(INSTRUCTION_DETAILS_PREFIX + "composition_uid", "$selfComposition");
        content.put(INSTRUCTION_DETAILS_PREFIX + "wt_path", "request_for_service/referral_details/service_request:0");
        content.put(INSTRUCTION_DETAILS_PREFIX + "instruction_index", "0");  // Should this zero be in quotes?
        content.put(INSTRUCTION_DETAILS_PREFIX + "activity_id", "activities[at0002]");

        ReferralStates refState = ReferralStates.fromString(referral.getReferralState());

        content.put(ISM_TRANS_PREFIX + "current_state|code", refState.getReferralStateCode());
        content.put(ISM_TRANS_PREFIX + "current_state|value", refState.getRefState());
        if (refState.getRefState().equalsIgnoreCase("Planned")) {
            content.put(REFERRALS_PREFIX + "request:0/reason_description", referral.getClinicalSummary());
            content.put(ISM_TRANS_PREFIX + "careflow_step|code", "at0026");
            content.put(ISM_TRANS_PREFIX + "careflow_step|value", "Service request sent");
            String dateOfReferral = DateFormatter.toSimpleDateString(referral.getDateOfReferral());
            content.put(SERVICE_PREFIX + "time", dateOfReferral);
        } else if (refState.getRefState().equalsIgnoreCase("Completed")) {
            content.put(ISM_TRANS_PREFIX + "careflow_step|code", "at0005");
            content.put(ISM_TRANS_PREFIX + "careflow_step|value", "Service request complete");
            content.put(SERVICE_PREFIX + "comment", referral.getReferralOutcome());
            String dateResponded = DateFormatter.toSimpleDateString(referral.getDateResponded());
            content.put(SERVICE_PREFIX + "time", dateResponded);
        }

        content.put(SERVICE_PREFIX + "service_name", referral.getReferralType());  // Set to the same value as Type
        content.put(REFERRALS_PREFIX + "receiver_identifier", referral.getReference());  // Same as Reference


        return content;
    }
}
