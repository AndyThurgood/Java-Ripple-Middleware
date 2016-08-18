package org.rippleosi.common.service;

import java.util.Arrays;

import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.config.AuthSchemes;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.ProxyAuthenticationStrategy;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

public class RippleRestTemplate extends RestTemplate {

    private String proxyScheme;
    private String proxyHost;
    private Integer proxyPort;
    private String proxyUser;
    private String proxyPassword;

    public RippleRestTemplate() {
        super();
        setRequestFactory(new HttpComponentsClientHttpRequestFactory());
    }

    public RippleRestTemplate(String proxyScheme, String proxyHost, Integer proxyPort, String proxyUser, String proxyPassword) {
        super();

        this.proxyScheme = proxyScheme;
        this.proxyHost = proxyHost;
        this.proxyPort = proxyPort;
        this.proxyUser = proxyUser;
        this.proxyPassword = proxyPassword;

        setRequestFactory(requestFactory());
    }

    private HttpComponentsClientHttpRequestFactory requestFactory() {
        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();

        AuthScope authScope = new AuthScope(proxyHost, proxyPort, proxyScheme);
        UsernamePasswordCredentials credentials = new UsernamePasswordCredentials(proxyUser, proxyPassword);

        CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(authScope, credentials);

        CloseableHttpClient httpClient = buildHttpClient(credentialsProvider);
        requestFactory.setHttpClient(httpClient);

        return requestFactory;
    }

    private CloseableHttpClient buildHttpClient(CredentialsProvider credentialsProvider) {
        HttpClientBuilder clientBuilder = HttpClientBuilder.create();

        RequestConfig config = RequestConfig.custom()
                                            .setProxyPreferredAuthSchemes(Arrays.asList(AuthSchemes.KERBEROS, AuthSchemes.BASIC))
                                            .build();

        clientBuilder.useSystemProperties()
                     .setProxy(new HttpHost(proxyHost, proxyPort, proxyScheme))
                     .setDefaultCredentialsProvider(credentialsProvider)
                     .setProxyAuthenticationStrategy(new ProxyAuthenticationStrategy())
                     .setDefaultRequestConfig(config);

        return clientBuilder.build();
    }
}
