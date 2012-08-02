package com.epam.rest.chat;

import com.epam.rest.RestCommand;
import com.epam.rest.RestContext;
import com.epam.rest.impl.DefaultRestService;

public class ChatService<T> extends DefaultRestService<T> {

    public T get(String id, RestContext context) {
        context.addParam("entity", id);
        RestCommand command = commandFactory.createCommand(ChatCommand.GET.name(), context);
        T result = execute(command);
        return result;
    }

    public T create(Object entity, RestContext context) {
        context.addParam("enity", entity);
        RestCommand command = commandFactory.createCommand(ChatCommand.CREATE.name(), context);
        T result = execute(command);
        return result;
    }

}
