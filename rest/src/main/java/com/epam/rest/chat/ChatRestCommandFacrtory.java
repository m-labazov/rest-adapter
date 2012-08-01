package com.epam.rest.chat;

import com.epam.rest.RestCommand;
import com.epam.rest.RestCommandFactory;
import com.epam.rest.RestContext;
import com.epam.rest.impl.DefaultRequestFormer;
import com.epam.rest.impl.DefaultRestCommand;

import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;

import java.util.Map;

public class ChatRestCommandFacrtory implements RestCommandFactory {

    private ChatEntityProcessor entityProcessor;

    @Override
    public RestCommand createCommand(String type, RestContext context) {
        ChatCommand commandEnum = ChatCommand.valueOf(type);
        RestCommand command = createCommand(commandEnum, context);
        return command;
    }

    public RestCommand createCommand(ChatCommand commandEnum, RestContext context) {
        RestCommand result = null;

        switch (commandEnum) {
        case CREATE:
            result = createCommand(new HttpPost(), context);
            break;
        case GET:
            result = createGetCommand(context);
            break;
        case UPDATE:
            result = createCommand(new HttpPost(), context);
            break;
        case DELETE:
            result = createCommand(new HttpDelete(), context);
            break;
        }
        return result;
    }

    private RestCommand createGetCommand(RestContext context) {
        return null;
    }

    private RestCommand createCommand(HttpRequestBase request, RestContext context) {
        DefaultRestCommand command = new DefaultRestCommand();

        DefaultRequestFormer requestFormer = new DefaultRequestFormer();
        Map<String, String> headers = null;
        String params = entityProcessor.serialize(context.getParam("entity"));
        requestFormer.setHeaders(headers);
        requestFormer.setRequest(request);
        requestFormer.setParams(params);
        requestFormer.setRootUrl(context.getRootUrl());
        requestFormer.setUrlSuffix(context.getParam("urlSuffix").toString());
        request = requestFormer.formRequest();

        command.setContext(context);
        command.setRequest(request);

        return command;
    }
}
