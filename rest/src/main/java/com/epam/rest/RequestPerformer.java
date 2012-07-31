package com.epam.rest;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpRequestBase;

public interface RequestPerformer {

    HttpResponse performRequest(HttpRequestBase request, RestContext context);

}
