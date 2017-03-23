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
package org.rippleosi.patient.problems.search;

import java.util.List;
import java.util.Map;

import org.rippleosi.common.exception.DataNotFoundException;
import org.rippleosi.common.service.strategies.query.details.AbstractDetailsGetQueryStrategy;
import org.rippleosi.patient.problems.model.ProblemDetails;

/**
 */
public class ProblemDetailsQueryStrategy extends AbstractDetailsGetQueryStrategy<ProblemDetails> {

    private final String problemId;

    ProblemDetailsQueryStrategy(String patientId, String problemId) {
        super(patientId);
        this.problemId = problemId;
    }

    @Override
    public String getQuery(String namespace, String patientId) {
        return "select " +
                " a/uid/value as compositionID, " +
                " a/composer/name as author, " +
                " a/composer/external_ref/id/value as authorId, " +
                " a/context/start_time/value as dateCreated, " +
                " b_a/data[at0001]/items[at0002]/value/value as problem, " +
                " b_a/data[at0001]/items[at0002]/value/defining_code/code_string as problemCode, " +
                " b_a/data[at0001]/items[at0002]/value/defining_code/terminology_id/value as problemTerminology, " +
                " b_a/data[at0001]/items[at0077]/value/value as onsetDate, " +
                " b_a/data[at0001]/items[at0009]/value/value as description " +
                "from EHR e " +
                "contains COMPOSITION a[openEHR-EHR-COMPOSITION.problem_list.v1] " +
                "contains EVALUATION b_a[openEHR-EHR-EVALUATION.problem_diagnosis.v1] " +
                "where " +
                " a/name/value='Problem list' and " +
                " a/uid/value = '" + problemId + "' and " +
                "(e/ehr_status/subject/external_ref/namespace='" + namespace + "' and " +
                "e/ehr_status/subject/external_ref/id/value='" + patientId + "')";
    }

    @Override
    public ProblemDetails transform(List<Map<String, Object>> resultSet) {

        if (resultSet.isEmpty()) {
            throw new DataNotFoundException("No results found");
        }

        Map<String, Object> data = resultSet.get(0);

        return new ProblemDetailsTransformer().transform(data);
    }
}
