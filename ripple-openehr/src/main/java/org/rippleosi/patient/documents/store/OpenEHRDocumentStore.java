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
package org.rippleosi.patient.documents.store;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import org.apache.camel.Consume;
import org.rippleosi.common.service.AbstractOpenEhrService;
import org.rippleosi.common.service.CreateStrategy;
import org.rippleosi.common.service.DefaultStoreStrategy;
import org.rippleosi.patient.documents.model.DocumentDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;

/**
 */
@Service
public class OpenEHRDocumentStore extends AbstractOpenEhrService implements DocumentStore {

    @Value("${c4hOpenEHR.referralDocumentTemplate}")
    private String referralDocumentTemplate;

    private static final Logger logger = LoggerFactory.getLogger(OpenEHRDocumentStore.class);
        
    @Consume(uri = "activemq:Consumer.C4HOpenEHR.VirtualTopic.Ripple.Documents.Create")
    public void create(String patientId, DocumentDetails document) {

        Map<String,Object> content = null;
        String documentTemplate = null;
        
        if("hl7Referral".equalsIgnoreCase(document.getDocumentType())){
            content = createReferralFlatJsonContent(document);
            documentTemplate = referralDocumentTemplate;
        } else if ("hl7Discharge".equalsIgnoreCase(document.getDocumentType())){
            // Create and save the discharge data
        }
        
        if(documentTemplate != null && content != null){
            CreateStrategy createStrategy = new DefaultStoreStrategy(patientId, documentTemplate, content);
            //createData(createStrategy);   // Commented out until we have the template to save to openEHR
        }
    }

    private Map<String,Object> createReferralFlatJsonContent(DocumentDetails document) {
    
        final String REFERRAL_REQUEST_PREFIX = "referral/referral_details/service_request/";
        final String REFERRAL_STATUS_PREFIX = "referral/referral_details/referral_status/";
    
        String hl7DocumentXML = (String)document.getDocumentContent();
    
        Map<String, Object> content = null;
        
        try {
            DocumentBuilder xmlDocumentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document hl7Document = xmlDocumentBuilder.parse(new ByteArrayInputStream(hl7DocumentXML.getBytes(StandardCharsets.UTF_8)));
            XPath xPath = XPathFactory.newInstance().newXPath();
            
            content = new HashMap<>();
            
            content.put("ctx/language", "en");
            content.put("ctx/territory", "GB");
            content.put("ctx/composer_name", (String)xPath.compile("//MSH.4/HD.1").evaluate(hl7Document));
            content.put("ctx/health_care_facility|id", (String)xPath.compile("//MSH.4/HD.2").evaluate(hl7Document));
            content.put("ctx/time", (String)xPath.compile("//MSH.7/TS.1").evaluate(hl7Document));
            
            content.put(REFERRAL_REQUEST_PREFIX + "referred_to_provider/identifier", (String)xPath.compile("//MSH.6/HD.1").evaluate(hl7Document));
            content.put(REFERRAL_REQUEST_PREFIX + "referral_control_number", (String)xPath.compile("//MSH.10").evaluate(hl7Document));
            
            
        } catch (Exception e){
            content = null;
            logger.error(e.getMessage());
        }
        return content;
    }

}
