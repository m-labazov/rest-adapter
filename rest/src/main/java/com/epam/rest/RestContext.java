package com.epam.rest;

import java.util.List;
import java.util.Map;

public interface RestContext {

    void addError(String error);

    void addLogRecord(String record);

    List<String> getErrors();

    List<String> getLogRecords();

    boolean hasErrors();

    void setParams(Map<String, Object> params);

    Map<String, Object> getParams();

    void addParam(String name, Object param);

}
