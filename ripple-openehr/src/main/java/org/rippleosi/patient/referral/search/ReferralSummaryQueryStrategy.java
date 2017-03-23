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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.rippleosi.common.service.strategies.query.list.AbstractListGetQueryStrategy;
import org.rippleosi.patient.referral.model.ReferralSummary;

/**
 */
public class ReferralSummaryQueryStrategy extends AbstractListGetQueryStrategy<ReferralSummary> {

    ReferralSummaryQueryStrategy(String patientId) {
        super(patientId);
    }

    @Override
    public String getQuery(String namespace, String patientId) {

        return "select a/composer/name as author, " +
                "a/uid/value as compositionId, " +
                "a/context/start_time/value as date, " +
                "b_a/activities[at0001]/description[at0009]/items[at0121]/value/value as type, " +
                "b_a/activities[at0001]/description[at0009]/items[at0062]/value/value as reason, " +
                "b_a/activities[at0001]/description[at0009]/items[at0064]/value/value as summary, " +
                "b_a/protocol[at0008]/items[openEHR-EHR-CLUSTER.individual_person_uk.v1, " +
                "'Requestor']/items[openEHR-EHR-CLUSTER.person_name.v1]/items[at0001]/value/value as referralFrom, " +
                "b_a/protocol[at0008]/items[openEHR-EHR-CLUSTER.organisation.v1, " +
                "'Receiver']/items[at0001]/value/value as referralTo, " +
                "b_a/protocol[at0008]/items[at0011]/value/value as referral_ref, " +
                "b_d/description[at0001]/items[at0011]/value/value as Service_Service_name, " +
                "b_d/description[at0001]/items[at0028]/value/value as Outcome, " +
                "b_d/time/value as dateOfState, " +
                "b_d/ism_transition/current_state/value as state, " +
                "b_d/ism_transition/current_state/defining_code/code_string as stateCode, " +
                "b_d/ism_transition/careflow_step/value as careflow, " +
                "b_d/ism_transition/careflow_step/defining_code/code_string as careflowCode " +
                "from EHR e contains COMPOSITION a[openEHR-EHR-COMPOSITION.request.v1] " +
                "contains ( INSTRUCTION b_a[openEHR-EHR-INSTRUCTION.request.v0] or " +
                "ACTION b_d[openEHR-EHR-ACTION.service.v0]) " +
                "where a/name/value='Request for service' and " +
                " (e/ehr_status/subject/external_ref/namespace='" + namespace + "' and " +
                "e/ehr_status/subject/external_ref/id/value='" + patientId + "')";
    }

    @Override
    public List<ReferralSummary> transform(List<Map<String, Object>> resultSet) {
        Collection<Map<String, Object>> filtered = CollectionUtils.select(resultSet, new ReferralOnlyPredicate());

        return CollectionUtils.collect(filtered, new ReferralSummaryTransformer(), new ArrayList<>());
    }
}
