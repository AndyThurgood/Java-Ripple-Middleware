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
package org.rippleosi.patient.documents.discharge.search;

import java.util.List;

import org.rippleosi.common.service.AbstractOpenEhrService;
import org.rippleosi.patient.documents.discharge.model.DischargeDocumentDetails;
import org.rippleosi.patient.documents.common.model.AbstractDocumentSummary;
import org.springframework.stereotype.Service;

/**
 */
@Service
public class OpenEHRDischargeDocumentSearch extends AbstractOpenEhrService implements DischargeDocumentSearch {

    @Override
    public List<AbstractDocumentSummary> findAllDischargeDocuments(String patientId) {
        final DischargeDocumentSummaryQueryStrategy query = new DischargeDocumentSummaryQueryStrategy(patientId);

        return findData(query);
    }

    @Override
    public DischargeDocumentDetails findDischargeDocument(String patientId, String documentId) {
        final DischargeDocumentDetailsQueryStrategy query = new DischargeDocumentDetailsQueryStrategy(patientId, documentId);

        return findData(query);
    }
}
