package com.sling.consumer.search;

import com.sling.consumer.search.dto.SearchEvent;
import com.sling.model.helper.gateways.JsonHelper;
import com.sling.model.search.port.SearchCountRepositoryPort;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.support.Acknowledgment;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.ExecutorService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class KafkaConsumerTest {

    private static final String TOPIC = "search-topic";
    private static final String RECORD_KEY = "key-1";
    private static final String EXPECTED_HASH = "abc123hash";
    private static final String HOTEL_ID = "1234";
    private static final LocalDate CHECK_IN = LocalDate.of(2026, 5, 1);
    private static final LocalDate CHECK_OUT = LocalDate.of(2026, 5, 5);
    private static final List<Integer> AGES = List.of(30, 25);
    private static final String VALID_JSON = """
            {"hash":"abc123hash","hotelId":"1234","checkIn":"2026-05-01","checkOut":"2026-05-05","ages":[30,25]}
            """;

    @Mock
    private SearchCountRepositoryPort searchCountRepositoryPort;

    @Mock
    private ExecutorService executor;

    @Mock
    private JsonHelper jsonHelper;

    @Mock
    private Acknowledgment acknowledgment;

    private KafkaConsumer kafkaConsumer;

    @BeforeEach
    void setUp() {
        kafkaConsumer = new KafkaConsumer(searchCountRepositoryPort, executor, jsonHelper);
    }

    private void configureExecutorToRunSynchronously() {
        doAnswer(invocation -> {
            Runnable task = invocation.getArgument(0);
            task.run();
            return null;
        }).when(executor).submit(any(Runnable.class));
    }

    @Nested
    @DisplayName("consume - Processes Kafka records and increments search count")
    class ConsumeTests {

        @Test
        @DisplayName("Should deserialize event, increment count and acknowledge when record is valid")
        void shouldDeserializeIncrementAndAcknowledgeWhenRecordIsValid() {
            configureExecutorToRunSynchronously();
            ConsumerRecord<String, String> record = new ConsumerRecord<>(TOPIC, 0, 0L, RECORD_KEY, VALID_JSON);
            SearchEvent event = new SearchEvent(EXPECTED_HASH, HOTEL_ID, CHECK_IN, CHECK_OUT, AGES);
            when(jsonHelper.jsonStringToObject(VALID_JSON, SearchEvent.class)).thenReturn(event);

            kafkaConsumer.consume(record, acknowledgment);

            verify(jsonHelper).jsonStringToObject(VALID_JSON, SearchEvent.class);
            verify(searchCountRepositoryPort).increment(EXPECTED_HASH);
            verify(acknowledgment).acknowledge();
        }

        @Test
        @DisplayName("Should throw RuntimeException and not acknowledge when deserialization fails")
        void shouldThrowExceptionAndNotAcknowledgeWhenDeserializationFails() {
            configureExecutorToRunSynchronously();
            ConsumerRecord<String, String> record = new ConsumerRecord<>(TOPIC, 0, 0L, RECORD_KEY, "invalid-json");
            when(jsonHelper.jsonStringToObject("invalid-json", SearchEvent.class))
                    .thenThrow(new RuntimeException("Deserialization error"));

            assertThrows(RuntimeException.class, () -> kafkaConsumer.consume(record, acknowledgment));

            verify(searchCountRepositoryPort, never()).increment(any());
            verify(acknowledgment, never()).acknowledge();
        }

        @Test
        @DisplayName("Should throw RuntimeException and not acknowledge when increment fails")
        void shouldThrowExceptionAndNotAcknowledgeWhenIncrementFails() {
            configureExecutorToRunSynchronously();
            ConsumerRecord<String, String> record = new ConsumerRecord<>(TOPIC, 0, 0L, RECORD_KEY, VALID_JSON);
            SearchEvent event = new SearchEvent(EXPECTED_HASH, HOTEL_ID, CHECK_IN, CHECK_OUT, AGES);
            when(jsonHelper.jsonStringToObject(VALID_JSON, SearchEvent.class)).thenReturn(event);
            doAnswer(_ -> {throw new RuntimeException("DB error");})
                    .when(searchCountRepositoryPort).increment(EXPECTED_HASH);

            assertThrows(RuntimeException.class, () -> kafkaConsumer.consume(record, acknowledgment));

            verify(acknowledgment, never()).acknowledge();
        }
    }

    @Nested
    @DisplayName("SearchEvent - Coverage for record constructor and accessors")
    class SearchEventTests {

        @Test
        @DisplayName("Should create SearchEvent with all fields accessible")
        void shouldCreateSearchEventWithAllFields() {
            SearchEvent event = new SearchEvent(EXPECTED_HASH, HOTEL_ID, CHECK_IN, CHECK_OUT, AGES);

            assertEquals(EXPECTED_HASH, event.hash());
            assertEquals(HOTEL_ID, event.hotelId());
            assertEquals(CHECK_IN, event.checkIn());
            assertEquals(CHECK_OUT, event.checkOut());
            assertEquals(AGES, event.ages());
        }
    }
}
