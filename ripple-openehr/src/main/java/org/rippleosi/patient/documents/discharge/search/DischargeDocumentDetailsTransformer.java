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

import java.util.Date;
import java.util.Map;

import org.apache.commons.collections4.MapUtils;
import org.apache.commons.collections4.Transformer;
import org.rippleosi.common.util.DateFormatter;
import org.rippleosi.patient.documents.discharge.model.DischargeDocumentDetails;

/**
 */
public class DischargeDocumentDetailsTransformer implements Transformer<Map<String, Object>, DischargeDocumentDetails> {

    @Override
    public DischargeDocumentDetails transform(Map<String, Object> input) {

        String startDateTimeAsString = MapUtils.getString(input, "start_date");
        String dateCreatedAsString = MapUtils.getString(input, "date_created");

        Date startDate = DateFormatter.toDateOnly(startDateTimeAsString);
        Date startTime = DateFormatter.toTimeOnly(startDateTimeAsString);
        Date dateCreated = DateFormatter.toDate(dateCreatedAsString);

        DischargeDocumentDetails dischargeDocument = new DischargeDocumentDetails();
        //dischargeDocument.setAuthor(MapUtils.getString(input, "author"));
        //dischargeDocument.setDateCreated(dateCreated);

        return dischargeDocument;
    }
}
