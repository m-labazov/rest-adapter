package com.epam.rest.impl;

import com.epam.rest.RestCommand;
import com.epam.rest.RestCommandPerformer;

public class DefaultRestCommandPerformer implements RestCommandPerformer {

    protected boolean async;

    @Override
    public String perfomCommand(RestCommand command) {
        String result = null;
        if (async) {
            Thread asyncCommand = new Thread(command);
            asyncCommand.start();
        } else {
            command.run();
            result = command.getResponseText();
        }

        return result;
    }

    @Override
    public boolean isAsync() {
        return async;
    }

    @Override
    public void setAsync(boolean async) {
        this.async = async;
    }

}
