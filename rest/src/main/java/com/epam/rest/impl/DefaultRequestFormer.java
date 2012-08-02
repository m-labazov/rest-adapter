package com.epam.rest.impl;

import com.epam.rest.RequestFormer;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

public class DefaultRequestFormer implements RequestFormer {

    public final static String URL_PREFIX_SECURED = "https://";
    public static final String URL_PREFIX_UNSECURED = "http://";

    protected Map<String, String> headers = new HashMap<String, String>();
    protected String rootUrl;
    protected String urlSuffix;
    protected String params;
    protected HttpRequestBase request;
    protected boolean securedRequest = true;

    public DefaultRequestFormer() {
    }

    public DefaultRequestFormer(String rootUrl, String urlSuffix, Map<String, String> headers, String params) {
        this.rootUrl = rootUrl;
        this.urlSuffix = urlSuffix;
        this.headers = headers;
        this.params = params;
    }

    public void fillHeader(HttpRequestBase request) {
        for (Map.Entry<String, String> entry : headers.entrySet()) {
            request.addHeader(entry.getKey(), entry.getValue());
        }
    }

    public void fillUri(HttpRequestBase request, String rootUrl, String urlSuffix) {
        URI uri = null;
        try {
            String defaultUri = getDefaultUri(rootUrl, urlSuffix);
            uri = new URI(defaultUri);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        request.setURI(uri);
    }

    public void fillParams(String params) {
        if (params != null && request instanceof HttpEntityEnclosingRequestBase) {
            HttpEntityEnclosingRequestBase entityRequest = (HttpEntityEnclosingRequestBase) request;
            try {
                HttpEntity entity = new StringEntity(params);
                entityRequest.setEntity(entity);
            } catch (UnsupportedEncodingException e) {
                // TODO process error
            }
        }
    }

    public String getDefaultUri(String rootUrl, String urlSuffix) {
        StringBuilder sb = new StringBuilder();
        if (securedRequest) {
            sb.append(URL_PREFIX_SECURED);
        } else {
            sb.append(URL_PREFIX_UNSECURED);
        }
        sb.append(rootUrl);
        urlSuffix = urlSuffix.replaceAll("//", "/");
        if (StringUtils.isNotBlank(urlSuffix)) {
            if (!urlSuffix.startsWith("/")) {
                sb.append("/");
            }
            sb.append(urlSuffix);
        }
        return sb.toString();
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public void setSecuredRequest(boolean securedRequest) {
        this.securedRequest = securedRequest;
    }

    @Override
    public HttpRequestBase formRequest() {
        fillHeader(request);
        fillUri(request, rootUrl, urlSuffix);
        fillParams(params);
        return request;
    }

    public String getRootUrl() {
        return rootUrl;
    }

    public void setRootUrl(String rootUrl) {
        this.rootUrl = rootUrl;
    }

    public String getUrlSuffix() {
        return urlSuffix;
    }

    public void setUrlSuffix(String urlSuffix) {
        this.urlSuffix = urlSuffix;
    }

    public String getParams() {
        return params;
    }

    public void setParams(String params) {
        this.params = params;
    }

    public HttpRequestBase getRequest() {
        return request;
    }

    public void setRequest(HttpRequestBase request) {
        this.request = request;
    }

}
