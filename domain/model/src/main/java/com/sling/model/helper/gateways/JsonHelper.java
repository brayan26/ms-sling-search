package com.sling.model.helper.gateways;

public interface JsonHelper {
    <T> T jsonStringToObject(String jsonString, Class<T> clazz);

    String objectToString(Object object);
}
