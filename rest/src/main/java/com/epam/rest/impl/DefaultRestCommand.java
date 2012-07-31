package com.epam.rest.impl;

import com.epam.rest.RequestPerformer;
import com.epam.rest.RestCommand;
import com.epam.rest.RestContext;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import java.io.IOException;

public class DefaultRestCommand implements RestCommand {

    private Logger logger = Logger.getLogger(DefaultRestCommand.class);

    protected RestContext context;
    protected HttpRequestBase request;
    protected HttpResponse response;
    protected long responseCode;
    protected String responseText;
    protected RequestPerformer requestPerformer;

    @Override
    public void run() {
        logRequest(request);
        HttpResponse response = requestPerformer.performRequest(request, context);
        responseCode = response.getStatusLine().getStatusCode();
        responseText = EntityUtils.getContentCharSet(response.getEntity());
        logResponse(response, responseText, responseCode);
    }

    public RestContext getContext() {
        return context;
    }

    public void setContext(RestContext context) {
        this.context = context;
    }

    @Override
    public String getResponseText() {
        return responseText;
    }

    @Override
    public long getResponseCode() {
        return responseCode;
    }

    @Override
    public HttpRequestBase getRequest() {
        return request;
    }

    @Override
    public HttpResponse getResponse() {
        return response;
    }

    public void setRequest(HttpRequestBase request) {
        this.request = request;
    }

    public void setResponse(HttpResponse response) {
        this.response = response;
    }

    public void setResponseCode(long responseCode) {
        this.responseCode = responseCode;
    }

    public void setResponseText(String responseText) {
        this.responseText = responseText;
    }

    public RequestPerformer getRequestPerformer() {
        return requestPerformer;
    }

    public void setRequestPerformer(RequestPerformer requestPerformer) {
        this.requestPerformer = requestPerformer;
    }

    @Override
    public String getName() {
        return null;
    }

    protected void logRequest(HttpRequestBase request) {
        logger.debug(buildRequestLogRecord(request));
    }

    protected void logResponse(HttpResponse response, String responseText, long responseCode) {
        logger.debug(buildResponseLogRecord(response, responseText, responseCode));
    }

    protected String buildRequestLogRecord(HttpRequestBase request) {
        StringBuilder sb = new StringBuilder();

        sb.append("\nREQUEST:\n").append("URL:\n").append(request.getURI()).append("\nMETHOD:\n")
                .append(request.getMethod()).append("\n");

        addHeaderToLog(request.getAllHeaders(), sb);

        if (request instanceof HttpEntityEnclosingRequestBase) {
            try {
                String body = processContent(((HttpEntityEnclosingRequestBase) request).getEntity());
                if (StringUtils.isNotBlank(body)) {
                    sb.append("REQUSET BODY:\n").append(body).append("\n");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return sb.toString();
    }

    protected String buildResponseLogRecord(HttpResponse response, String responseText, long responseCode) {
        StringBuilder sb = new StringBuilder();
        sb.append("\nRESPONSE:\n").append("STATUS_CODE:\n").append(responseCode).append("\n");

        addHeaderToLog(response.getAllHeaders(), sb);

        if (StringUtils.isNotBlank(responseText)) {
            sb.append("RESPONSE_TEXT:\n").append(responseText).append("\n");
        }
        sb.append("\n");

        return sb.toString();
    }

    protected String processContent(HttpEntity entity) throws IOException {
        String result = null;
        if (entity != null) {
            result = EntityUtils.toString(entity);
        }
        return result;
    }

    protected void addHeaderToLog(Header[] headers, StringBuilder sb) {
        sb.append("HEADERS:\n");
        for (Header header : headers) {
            sb.append(header.getName()).append(": ").append(header.getValue()).append("\n");
        }
    }
}
