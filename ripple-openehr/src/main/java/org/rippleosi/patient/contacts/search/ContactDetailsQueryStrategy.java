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
package org.rippleosi.patient.contacts.search;

import java.util.List;
import java.util.Map;

import org.rippleosi.common.exception.DataNotFoundException;
import org.rippleosi.common.service.strategies.query.details.AbstractDetailsGetQueryStrategy;
import org.rippleosi.patient.contacts.model.ContactDetails;

/**
 */
public class ContactDetailsQueryStrategy extends AbstractDetailsGetQueryStrategy<ContactDetails> {

    private final String contactId;

    ContactDetailsQueryStrategy(String patientId, String contactId) {
        super(patientId);
        this.contactId = contactId;
    }

    @Override
    public String getQuery(String namespace, String patientId) {
/*
        return "select a/uid/value as uid, " +
                "b_a/items[at0001]/value/value as name, " +
                "b_b/data[at0001]/items[at0030]/value/value as relationship, " +
                "b_b/data[at0001]/items[at0025]/value/value as next_of_kin, " +
                "b_c/items[at0002]/value/value as contact_information, " +
                "b_b/data[at0001]/items[at0017]/value/value as notes, " +
                "b_b/data[at0001]/items[at0035]/value/value as relationship_type, " +
                "b_b/data[at0001]/items[at0035]/value/defining_code/code_string as relationship_code, " +
                "b_b/data[at0001]/items[at0035]/value/defining_code/terminologyId/value as relationship_terminology, " +
                "b_d/items[at0002]/value/value as address, " +
                "b_c/items[at0002]/value/value as comms " +
                "from EHR e contains COMPOSITION a[openEHR-EHR-COMPOSITION.health_summary.v1]contains " +
                "(CLUSTER b_a[openEHR-EHR-CLUSTER.person_name.v1] or " +
                "ADMIN_ENTRY b_b[openEHR-EHR-ADMIN_ENTRY.relevant_contact_rcp.v1] or " +
                "CLUSTER b_c[openEHR-EHR-CLUSTER.telecom_uk.v1] or " +
                "CLUSTER b_d[openEHR-EHR-CLUSTER.address_uk.v0]) " +
                "where a/name/value='Relevant Contacts List' and " +
                "e/ehr_status/subject/external_ref/namespace = '" + namespace + "' " +
                "and e/ehr_status/subject/external_ref/id/value = '" + patientId + "'" +
                "and a/uid/value = '" + contactId + "'";
 */
        return "select" +
                " a/uid/value as uid, " +
                " a/context/start_time/value as dateCreated, " +
                " a/composer/name as author, " +
                " b_b/data[at0001]/items[at0030]/value/value as relationshipRoleType, " +
                " b_b/data[at0001]/items[at0025]/value/value as next_of_kin, " +
                " b_b/data[at0001]/items[at0017]/value/value as notes, " +
                " b_b/data[at0001]/items[at0035]/value/value as relationshipCategory, " +
                " b_b/data[at0001]/items[at0035]/value/defining_code/code_string as relationshipCategoryCode, " +
                " b_b/data[at0001]/items[at0035]/value/defining_code/terminologyId/value as relationshipCategoryTerminology, " +
                " b_b/data[at0001]/items[openEHR-EHR-CLUSTER.individual_person_uk.v1]/items[openEHR-EHR-CLUSTER.person_name.v1]/items[at0001]/value/value as name, " +
                " b_b/data[at0001]/items[openEHR-EHR-CLUSTER.individual_person_uk.v1]/items[openEHR-EHR-CLUSTER.address_uk.v0]/items[at0002]/value/value as address, " +
                " b_b/data[at0001]/items[openEHR-EHR-CLUSTER.individual_person_uk.v1]/items[openEHR-EHR-CLUSTER.telecom_uk.v1]/items[at0002]/value/value as contact_information " +
                " from EHR e " +
                " contains COMPOSITION a[openEHR-EHR-COMPOSITION.health_summary.v1] " +
                " contains (" +
                " ADMIN_ENTRY b_b[openEHR-EHR-ADMIN_ENTRY.relevant_contact_rcp.v1]) " +
                " where " +
                " a/name/value='Relevant Contacts List' and (" +
                " e/ehr_status/subject/external_ref/namespace='" + namespace + "'  and " +
                " e/ehr_status/subject/external_ref/id/value='" + patientId + "' and " +
                " a/uid/value = '" + contactId + "')";

    }

    @Override
    public ContactDetails transform(List<Map<String, Object>> resultSet) {

        if (resultSet.isEmpty()) {
            throw new DataNotFoundException("No results found");
        }

        Map<String,Object> data = resultSet.get(0);

        return new ContactDetailsTransformer().transform(data);
    }
}
