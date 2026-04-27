package com.sling.jsonhelper;

import com.sling.model.helper.gateways.JsonHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Slf4j
@Component
@RequiredArgsConstructor
public class JsonUtil implements JsonHelper {

    private final ObjectMapper objectMapper;

    public <T> T jsonStringToObject(String jsonString, Class<T> clazz) {
        try {
            return this.objectMapper.readValue(jsonString, clazz);
        } catch (JsonProcessingException ex) {
            log.error("Error transforming to Class: {}", ex.getMessage(), ex);
            throw new RuntimeException("Error transforming to Class: " + ex.getMessage());
        }
    }

    @Override
    public String objectToString(Object clazz) {
        try {
            return this.objectMapper.writeValueAsString(clazz);
        } catch (JsonProcessingException ex) {
            log.error("Error transforming to String: {}", ex.getMessage(), ex);
            throw new RuntimeException("Error casting to String: " + ex.getMessage());
        }
    }
}
