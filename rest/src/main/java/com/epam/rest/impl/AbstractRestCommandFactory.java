package com.epam.rest.impl;

import com.epam.rest.RestCommandFactory;
import com.epam.rest.RestContext;

import org.apache.http.client.methods.HttpRequestBase;

public abstract class AbstractRestCommandFactory implements RestCommandFactory {

    protected void initBaseCommand(DefaultRestCommand command, RestContext context) {
        DefaultRequestFormer requestFormer = new DefaultRequestFormer();
        HttpRequestBase request = requestFormer.formRequest();
        command.setRequest(request);

        command.setContext(context);
    }

}
