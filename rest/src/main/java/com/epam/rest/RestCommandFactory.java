package com.epam.rest;

public interface RestCommandFactory {

    RestCommand createCommand(String type, RestContext context);

}
