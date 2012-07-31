package com.epam.rest;

public interface ResponseProcessor<T> {

    T process(String responseText);

}
