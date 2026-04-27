package com.sling.jsonhelper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JsonUtilTest {

    private static final String VALID_JSON = "{\"name\":\"test\",\"value\":42}";
    private static final String INVALID_JSON = "not-a-json";
    private static final String EXPECTED_NAME = "test";
    private static final int EXPECTED_VALUE = 42;
    private static final String ERROR_PREFIX_TO_CLASS = "Error transforming to Class:";
    private static final String ERROR_PREFIX_TO_STRING = "Error casting to String:";

    @Mock
    private ObjectMapper objectMapper;

    private JsonUtil jsonUtil;

    @BeforeEach
    void setUp() {
        jsonUtil = new JsonUtil(objectMapper);
    }

    @Nested
    @DisplayName("jsonStringToObject - Deserializes a JSON string into an object")
    class JsonStringToObjectTests {

        @Test
        @DisplayName("Should deserialize a valid JSON string into the target class")
        void shouldDeserializeValidJsonStringIntoTargetClass() throws Exception {
            SampleDto expected = new SampleDto(EXPECTED_NAME, EXPECTED_VALUE);
            when(objectMapper.readValue(VALID_JSON, SampleDto.class)).thenReturn(expected);

            SampleDto result = jsonUtil.jsonStringToObject(VALID_JSON, SampleDto.class);

            assertNotNull(result);
            assertEquals(EXPECTED_NAME, result.name());
            assertEquals(EXPECTED_VALUE, result.value());
            verify(objectMapper).readValue(VALID_JSON, SampleDto.class);
        }

        @Test
        @DisplayName("Should throw RuntimeException when JSON string is invalid")
        void shouldThrowRuntimeExceptionWhenJsonIsInvalid() throws Exception {
            when(objectMapper.readValue(INVALID_JSON, SampleDto.class))
                    .thenThrow(new JsonProcessingException("Unrecognized token") {});

            RuntimeException exception = assertThrows(RuntimeException.class,
                                                      () -> jsonUtil.jsonStringToObject(INVALID_JSON, SampleDto.class));

            assertTrue(exception.getMessage().startsWith(ERROR_PREFIX_TO_CLASS));
            verify(objectMapper).readValue(INVALID_JSON, SampleDto.class);
        }
    }

    @Nested
    @DisplayName("objectToString - Serializes an object into a JSON string")
    class ObjectToStringTests {

        @Test
        @DisplayName("Should serialize an object into a valid JSON string")
        void shouldSerializeObjectIntoJsonString() throws Exception {
            SampleDto input = new SampleDto(EXPECTED_NAME, EXPECTED_VALUE);
            when(objectMapper.writeValueAsString(input)).thenReturn(VALID_JSON);

            String result = jsonUtil.objectToString(input);

            assertEquals(VALID_JSON, result);
            verify(objectMapper).writeValueAsString(input);
        }

        @Test
        @DisplayName("Should throw RuntimeException when serialization fails")
        void shouldThrowRuntimeExceptionWhenSerializationFails() throws Exception {
            Object unserializable = new Object();
            when(objectMapper.writeValueAsString(unserializable))
                    .thenThrow(new JsonProcessingException("Cannot serialize") {});

            RuntimeException exception = assertThrows(RuntimeException.class,
                                                      () -> jsonUtil.objectToString(unserializable));

            assertTrue(exception.getMessage().startsWith(ERROR_PREFIX_TO_STRING));
            verify(objectMapper).writeValueAsString(unserializable);
        }
    }

    record SampleDto(String name, int value) {}
}
