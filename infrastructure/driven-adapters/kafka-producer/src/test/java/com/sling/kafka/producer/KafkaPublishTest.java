package com.sling.kafka.producer;

import com.sling.model.helper.gateways.JsonHelper;
import com.sling.model.search.Search;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class KafkaPublishTest {

    private static final String PRODUCER_KEY = "search-key";
    private static final String TOPIC_NAME = "search-topic";
    private static final String HOTEL_ID = "1234";
    private static final String CHECK_IN = "2026-05-01";
    private static final String CHECK_OUT = "2026-05-05";
    private static final List<Integer> AGES = List.of(30, 25);
    private static final String GENERATED_HASH = "abc123hash";
    private static final String SERIALIZED_JSON = "{\"hotelId\":\"1234\",\"hash\":\"abc123hash\"}";

    @Mock
    private KafkaTemplate<String, String> kafkaTemplate;

    @Mock
    private JsonHelper jsonHelper;

    private KafkaPublish kafkaPublish;

    @BeforeEach
    void setUp() {
        kafkaPublish = new KafkaPublish(PRODUCER_KEY, TOPIC_NAME, kafkaTemplate, jsonHelper);
    }

    @Test
    @DisplayName("Should serialize the search and send it to the configured Kafka topic with the producer key")
    void shouldSerializeSearchAndSendToKafkaTopic() {
        Search search = Search.builder()
                .hotelId(HOTEL_ID)
                .checkIn(CHECK_IN)
                .checkOut(CHECK_OUT)
                .ages(AGES)
                .hash(GENERATED_HASH)
                .build();
        when(jsonHelper.objectToString(search)).thenReturn(SERIALIZED_JSON);

        kafkaPublish.publish(search);

        verify(jsonHelper).objectToString(search);
        verify(kafkaTemplate).send(TOPIC_NAME, PRODUCER_KEY, SERIALIZED_JSON);
    }
}
