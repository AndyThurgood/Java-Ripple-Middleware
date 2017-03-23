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
package org.rippleosi.patient.details.search;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.Transformer;
import org.apache.commons.lang3.StringUtils;
import org.rippleosi.patient.details.model.PatientEntity;
import org.rippleosi.patient.summary.model.PatientSummary;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

@Component
public class PatientEntityToSummaryTransformer implements Transformer<PatientEntity, PatientSummary> {

    @Override
    public PatientSummary transform(PatientEntity patientEntity) {
        PatientSummary patientSummary = new PatientSummary();

        String name = patientEntity.getFirstName() + " " + patientEntity.getLastName();

        Collection<String> addressList = Arrays.asList(StringUtils.trimToNull(patientEntity.getAddress1()),
                StringUtils.trimToNull(patientEntity.getAddress2()),
                StringUtils.trimToNull(patientEntity.getAddress3()),
                StringUtils.trimToNull(patientEntity.getPostcode()));

        addressList = CollectionUtils.removeAll(addressList, Collections.singletonList(null));

        String address = StringUtils.join(addressList, ", ");

        patientSummary.setId(patientEntity.getNhsNumber());
        patientSummary.setName(name);
        patientSummary.setAddress(address);
        patientSummary.setDateOfBirth(patientEntity.getDateOfBirth());
        patientSummary.setGender(patientEntity.getGender());
        patientSummary.setNhsNumber(patientEntity.getNhsNumber());
        patientSummary.setDepartment(patientEntity.getDepartment().getDepartment());

        return patientSummary;
    }
}
