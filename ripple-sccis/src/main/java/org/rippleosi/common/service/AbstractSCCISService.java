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
package org.rippleosi.common.service;

import java.io.IOException;
import java.io.StringReader;
import org.apache.commons.collections4.Transformer;
import org.rippleosi.common.repo.Repository;
import org.rippleosi.common.service.proxies.SCCISRequestProxy;
import org.rippleosi.common.types.RepoSourceType;
import org.rippleosi.common.types.RepoSourceTypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;


public abstract class AbstractSCCISService implements Repository {

    @Value("${repository.config.c4hOpenEHR:2000}")
    private int priority;

    @Value("${scCIS.address}")
    private String scCISAddress;

    @Autowired
    private SCCISRequestProxy requestProxy;

    @Override
    public RepoSourceType getSource() {
        return RepoSourceTypes.SCCIS;
    }

    @Override
    public int getPriority() {
        return priority;
    }

    protected <T> T findData(String patientId, Transformer transformer) {

        ResponseEntity<String> response;

        response = queryByHttpGet(patientId);

          Document xml = null;
        if (response.getStatusCode() == HttpStatus.OK) {

            try {
                xml = DocumentBuilderFactory.newInstance().newDocumentBuilder()
                        .parse(new InputSource(new StringReader( response.getBody())));
                xml.normalize();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (SAXException e) {
                e.printStackTrace();
            } catch (ParserConfigurationException e) {
                e.printStackTrace();
            }
            transformer.transform(xml);

        }

        return (T) transformer.transform(xml);
    }

    private ResponseEntity<String> queryByHttpGet(String patientId) {

        return requestProxy.getQueryWithoutSession(getQueryByGetUri(patientId));
    }

    private String getQueryByGetUri(String query) {
        UriComponents components = UriComponentsBuilder
                .fromHttpUrl(scCISAddress)
                .queryParam("NHS", query)
                .build();

        return components.toUriString();
    }

    private String getQueryByPostUri() {
        UriComponents components = UriComponentsBuilder
                .fromHttpUrl(scCISAddress + "/query")
                .build();

        return components.toUriString();
    }

}
