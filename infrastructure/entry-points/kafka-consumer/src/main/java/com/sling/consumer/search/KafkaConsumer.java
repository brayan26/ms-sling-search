package com.sling.consumer.search;

import com.sling.consumer.search.dto.SearchEvent;
import com.sling.model.helper.gateways.JsonHelper;
import com.sling.model.search.port.SearchCountRepositoryPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaConsumer {
    private final SearchCountRepositoryPort searchCountRepositoryPort;
    private final ExecutorService executor;
    private final JsonHelper jsonHelper;

    @KafkaListener(topics = "${adapters.kafka.topic}", containerFactory = "kafkaListenerContainerFactory")
    public void consume(ConsumerRecord<String, String> record, Acknowledgment ack) {
        executor.submit(() -> {
            try {
                log.info("Received kafka record: {}", record.value());
                SearchEvent event = jsonHelper.jsonStringToObject(record.value(), SearchEvent.class);
                searchCountRepositoryPort.increment(event.hash());
                ack.acknowledge();
                log.info("Record processed successfully for hash: {}", event.hash());
            } catch (Exception e) {
                log.error("Error processing kafka record: {}", e.getMessage());
                throw new RuntimeException(e);
            }
        });
    }
}