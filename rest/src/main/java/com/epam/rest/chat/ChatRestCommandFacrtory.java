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
    private Map<String, String> headers;

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
        String urlSuffix = context.getParam("urlSuffix").toString();
        Object param = context.getParam("entity");
        urlSuffix += param.toString();
        String rootUrl = context.getRootUrl();

        DefaultRequestFormer requestFormer = new DefaultRequestFormer(rootUrl, urlSuffix, headers, null);
        HttpRequestBase request = requestFormer.formRequest();

        DefaultRestCommand command = new DefaultRestCommand();
        command.setContext(context);
        command.setRequest(request);
        return command;
    }

    private RestCommand createCommand(HttpRequestBase request, RestContext context) {
        DefaultRestCommand command = new DefaultRestCommand();

        DefaultRequestFormer requestFormer = new DefaultRequestFormer();
        Object param = context.getParam("entity");
        String params = entityProcessor.serialize(param);
        requestFormer.setHeaders(headers);
        requestFormer.setRequest(request);
        requestFormer.setParams(params);
        requestFormer.setRootUrl(context.getRootUrl());
        String urlSuffix = context.getParam("urlSuffix").toString();
        requestFormer.setUrlSuffix(urlSuffix);
        request = requestFormer.formRequest();

        command.setContext(context);
        command.setRequest(request);

        return command;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }
}
