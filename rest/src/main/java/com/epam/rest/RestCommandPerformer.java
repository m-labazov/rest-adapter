package com.epam.rest;

public interface RestCommandPerformer {

    String perfomCommand(RestCommand command);

    boolean isAsync();

    void setAsync(boolean async);

}
