package com.epam.rest.impl;

import com.epam.rest.RestContext;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DefaultRestContext implements RestContext {

    protected List<String> errors;
    protected List<String> logRecords;
    protected Map<String, Object> params;

    public DefaultRestContext() {
        errors = new ArrayList<String>();
        logRecords = new ArrayList<String>();
        params = new HashMap<String, Object>();
    }

    @Override
    public void addError(String error) {
        errors.add(error);
    }

    @Override
    public void addLogRecord(String record) {
        logRecords.add(record);
    }

    @Override
    public List<String> getErrors() {
        return errors;
    }

    @Override
    public List<String> getLogRecords() {
        return logRecords;
    }

    @Override
    public boolean hasErrors() {
        return errors.isEmpty();
    }

    @Override
    public void addParam(String name, Object param) {
        params.put(name, param);
    }

    @Override
    public void setParams(Map<String, Object> params) {
        this.params = params;
    }

    @Override
    public Map<String, Object> getParams() {
        return params;
    }

}
