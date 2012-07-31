package com.epam.rest;

public interface RestService<T> {

    RestCommandPerformer getCommandPerformer();

    ResponseProcessor<T> getResponseProcessor();

    RestCommandFactory getCommandFactory();

}
