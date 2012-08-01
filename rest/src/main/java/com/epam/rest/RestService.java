package com.epam.rest;

public interface RestService<T> {

    RestCommandPerformer getCommandPerformer();

    RestEntityProcessor<T> getResponseProcessor();

    RestCommandFactory getCommandFactory();

}
