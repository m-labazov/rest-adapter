package com.epam.rest;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpRequestBase;

public interface RestCommand extends Runnable {

    RestContext getContext();

    void setContext(RestContext context);

    String getName();

    String getResponseText();

    long getResponseCode();

    HttpRequestBase getRequest();

    void setRequest(HttpRequestBase request);

    HttpResponse getResponse();

    RequestPerformer getRequestPerformer();

    void setRequestPerformer(RequestPerformer performer);

}
