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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.rippleosi.common.service.strategies.query.list.AbstractListGetQueryStrategy;
import org.rippleosi.patient.contacts.model.ContactSummary;

/**
 */
public class ContactSummaryQueryStrategy extends AbstractListGetQueryStrategy<ContactSummary> {

    ContactSummaryQueryStrategy(String patientId) {
        super(patientId);
    }

    @Override
    public String getQuery(String namespace, String patientId) {
/*
        return "select    a/uid/value as uid,    b_a/items[at0001]/value/value as name, " +
                "b_b/data[at0001]/items[at0030]/value/value as relationship, " +
                "b_b/data[at0001]/items[at0025]/value/value as next_of_kin,  " +
                "b_c/items[at0002]/value/value as contactInformation,  " +
                "b_b/data[at0001]/items[at0017]/value/value as notes,  " +
                "b_b/data[at0001]/items[at0035]/value/value as relationshipType, " +
                "b_b/data[at0001]/items[at0035]/value/defining_code/code_string as relationshipCode,  " +
                "b_b/data[at0001]/items[at0035]/value/defining_code/terminologyId/value as relationshipTerminology,  " +
                "b_d/items[at0002]/value/value as address, " +
                "b_c/items[at0002]/value/value as comms " +
                "from EHR e contains COMPOSITION a[openEHR-EHR-COMPOSITION.health_summary.v1] " +
                "contains (    CLUSTER b_a[openEHR-EHR-CLUSTER.person_name.v1] or  " +
                "ADMIN_ENTRY b_b[openEHR-EHR-ADMIN_ENTRY.relevant_contact_rcp.v1] or  " +
                "CLUSTER b_c[openEHR-EHR-CLUSTER.telecom_uk.v1] or  " +
                "CLUSTER b_d[openEHR-EHR-CLUSTER.address_uk.v0]) " +
                "where    a/name/value='Relevant Contacts List'  " +
                "and e/ehr_status/subject/external_ref/namespace = '" + namespace + "'  and " +
                "e/ehr_status/subject/external_ref/id/value = '" + patientId + "'";
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
                " b_b/data[at0001]/items[openEHR-EHR-CLUSTER.individual_person_uk.v1]/items[openEHR-EHR-CLUSTER.telecom_uk.v1]/items[at0002]/value/value as comms " +
                " from EHR e " +
                " contains COMPOSITION a[openEHR-EHR-COMPOSITION.health_summary.v1] " +
                " contains (" +
                " ADMIN_ENTRY b_b[openEHR-EHR-ADMIN_ENTRY.relevant_contact_rcp.v1]) " +
                " where " +
                " a/name/value='Relevant Contacts List' and (" +
                " e/ehr_status/subject/external_ref/namespace='" + namespace + "'  and " +
                " e/ehr_status/subject/external_ref/id/value='" + patientId + "')";
    }

    @Override
    public List<ContactSummary> transform(List<Map<String, Object>> resultSet) {
        return CollectionUtils.collect(resultSet, new ContactSummaryTransformer(), new ArrayList<>());
    }
}
