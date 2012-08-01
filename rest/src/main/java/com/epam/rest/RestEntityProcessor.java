package com.epam.rest;

public interface RestEntityProcessor<T> {

    T deserialize(String responseText);

    String serialize(T entity);

}
