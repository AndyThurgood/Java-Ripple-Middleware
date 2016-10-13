/*
 *   Copyright 2016 Ripple OSI
 *
 *      Licensed under the Apache License, Version 2.0 (the "License");
 *      you may not use this file except in compliance with the License.
 *      You may obtain a copy of the License at
 *
 *          http://www.apache.org/licenses/LICENSE-2.0
 *
 *      Unless required by applicable law or agreed to in writing, software
 *      distributed under the License is distributed on an "AS IS" BASIS,
 *      WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *      See the License for the specific language governing permissions and
 *      limitations under the License.
 */
package org.rippleosi.patient.laborders.search;

import org.rippleosi.common.exception.DataNotFoundException;
import org.rippleosi.common.service.AbstractEtherCISQueryStrategy;
import org.rippleosi.patient.laborders.model.LabOrderDetails;

import java.util.List;
import java.util.Map;

public class EtherCISLabOrderDetailsQueryStrategy extends AbstractEtherCISQueryStrategy<LabOrderDetails> {

    private final String labOrderId;

    EtherCISLabOrderDetailsQueryStrategy(String patientId, String labOrderId) {
        super(patientId);
        this.labOrderId = labOrderId;
    }

    @Override
    public String getQuery(String namespace, String ehrId) {
        return "SELECT " +
                "ehr.entry.composition_id as uid, " +
                "ehr.party_identified.name as author, " +
                "ehr.event_context.start_time, " +
                "ehr.entry.entry #>> " +
                    "'{" +
                        "/composition[openEHR-EHR-COMPOSITION.referral.v0 and name/value=''Laboratory order''], " +
                        "/content[openEHR-EHR-INSTRUCTION.request-lab_test.v1],0, " +
                        "/activities[at0001 and name/value=''Lab Request''], /description[at0009], /items[at0121 and name/value=''Service requested''],/value,value" +
                    "}' as name, " +
                "ehr.entry.entry #>> " +
                    "'{" +
                        "/composition[openEHR-EHR-COMPOSITION.referral.v0 and name/value=''Laboratory order''], " +
                        "/content[openEHR-EHR-ACTION.laboratory_test.v1],0, /time,/value,value" +
                    "}' as order_date " +
                "FROM ehr.entry " +
                "INNER JOIN ehr.composition ON ehr.composition.id=ehr.entry.composition_id " +
                "INNER JOIN ehr.event_context ON ehr.event_context.composition_id=ehr.entry.composition_id " +
                "INNER JOIN ehr.party_identified ON ehr.composition.composer=ehr.party_identified.id " +
                "WHERE (ehr.composition.ehr_id='" + ehrId + "') " +
                "AND (ehr.entry.archetype_Id = 'openEHR-EHR-COMPOSITION.referral.v0') " +
                "AND ehr.entry.composition_id = '" + labOrderId + "';";
    }

    @Override
    public LabOrderDetails transform(List<Map<String, Object>> resultSet) {

        if (resultSet.isEmpty()) {
            throw new DataNotFoundException("No results found");
        }

        Map<String, Object> data = resultSet.get(0);

        return new EtherCISLabOrderDetailsTransformer().transform(data);
    }
}
