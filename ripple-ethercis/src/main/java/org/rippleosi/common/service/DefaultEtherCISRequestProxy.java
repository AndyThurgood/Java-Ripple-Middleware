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
package org.rippleosi.common.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.rippleosi.common.exception.InvalidDataException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class DefaultEtherCISRequestProxy implements EtherCISRequestProxy {

    @Value("${etherCIS.address}")
    private String etherCISAddress;

    @Value("${etherCIS.user}")
    private String etherCISUsername;

    @Value("${etherCIS.password}")
    private String etherCISPassword;

    @Value("${network.proxy.enabled}")
    private boolean proxyEnabled;

    @Value("${network.proxy.scheme}")
    private String proxyScheme;

    @Value("${network.proxy.host}")
    private String proxyHost;

    @Value("${network.proxy.port}")
    private Integer proxyPort;

    @Value("${network.proxy.user}")
    private String proxyUser;

    @Value("${network.proxy.password}")
    private String proxyPassword;

    @Override
    public <T> ResponseEntity<T> getWithSession(String uri, Class<T> cls, String ehrSessionId) {

        HttpEntity<String> request = buildRequestWithSession(null, ehrSessionId);

        return restTemplate().exchange(uri, HttpMethod.GET, request, cls);
    }

    @Override
    public <T> ResponseEntity<T> postWithSession(String uri, Class<T> cls, String ehrSessionId, Object body) {

        String json = convertToJson(body);

        HttpEntity<String> request = buildRequestWithSession(json, ehrSessionId);

        return restTemplate().exchange(uri, HttpMethod.POST, request, cls);
    }

    @Override
    public <T> ResponseEntity<T> putWithSession(String uri, Class<T> cls, String ehrSessionId, Object body) {

        String json = convertToJson(body);

        HttpEntity<String> request = buildRequestWithSession(json, ehrSessionId);

        return restTemplate().exchange(uri, HttpMethod.PUT, request, cls);
    }

    @Override
    public <T> ResponseEntity<T> createSession(String uri, Class<T> cls) {

        HttpHeaders headers = basicHeaders();

        HttpEntity<Object> request = new HttpEntity<>(null, headers);

        return restTemplate().exchange(uri, HttpMethod.POST, request, cls);
    }

    @Override
    public <T> ResponseEntity<T> killSession(String uri, Class<T> cls, String sessionId) {

        HttpHeaders headers = basicHeaders();
        headers.add("Ehr-Session", sessionId);

        HttpEntity<Object> request = new HttpEntity<>(null, headers);

        return restTemplate().exchange(uri, HttpMethod.DELETE, request, cls);
    }

    private HttpEntity<String> buildRequestWithSession(String body, String ehrSessionId) {

        HttpHeaders headers = basicHeaders();
        headers.add("Ehr-Session", ehrSessionId);

        return new HttpEntity<>(body, headers);
    }

    private HttpHeaders basicHeaders() {

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

        return headers;
    }

    private RippleRestTemplate restTemplate() {
        RippleRestTemplate template;

        if (proxyEnabled) {
            template = new RippleRestTemplate(proxyScheme, proxyHost, proxyPort, proxyUser, proxyPassword);
        }
        else {
            template = new RippleRestTemplate();
        }

        template.setUriTemplateHandler(new DefaultEtherCISURITemplateHandler());
        return template;
    }

    private String convertToJson(Object body) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.writeValueAsString(body);
        }
        catch (JsonProcessingException ex) {
            throw new InvalidDataException(ex.getMessage(), ex);
        }
    }
}
