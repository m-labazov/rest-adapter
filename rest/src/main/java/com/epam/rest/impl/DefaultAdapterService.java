package com.epam.rest.impl;

import com.epam.rest.ResponseProcessor;
import com.epam.rest.RestCommand;
import com.epam.rest.RestCommandFactory;
import com.epam.rest.RestCommandPerformer;
import com.epam.rest.RestService;

import org.apache.commons.lang3.StringUtils;

public class DefaultAdapterService<T> implements RestService<T> {

    protected RestCommandPerformer commandPerformer;
    protected RestCommandFactory commandFactory;
    protected ResponseProcessor<T> responseProcessor;

    public DefaultAdapterService() {
        init();
    }

    protected void init() {
        commandPerformer = new DefaultRestCommandPerformer();
        commandFactory = null;
        responseProcessor = null;
    }

    public T execute(RestCommand command) {
        T result = null;
        String responseText = commandPerformer.perfomCommand(command);
        if (StringUtils.isNotBlank(responseText)) {
            result = responseProcessor.process(responseText);
        }
        return result;
    }

    @Override
    public RestCommandPerformer getCommandPerformer() {
        return commandPerformer;
    }

    @Override
    public ResponseProcessor<T> getResponseProcessor() {
        return responseProcessor;
    }

    @Override
    public RestCommandFactory getCommandFactory() {
        return commandFactory;
    }

    public void setCommandPerformer(RestCommandPerformer commandPerformer) {
        this.commandPerformer = commandPerformer;
    }

    public void setCommandFactory(RestCommandFactory commandFactory) {
        this.commandFactory = commandFactory;
    }

    public void setResponseProcessor(ResponseProcessor<T> responseProcessor) {
        this.responseProcessor = responseProcessor;
    }

}
