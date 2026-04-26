package com.sling.consumer.search;

import com.sling.consumer.search.dto.SearchEvent;
import com.sling.model.helper.gateways.JsonHelper;
import com.sling.model.search.port.SearchCountRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class KafkaConsumer {
    private final SearchCountRepositoryPort searchCountRepositoryPort;
    private final JsonHelper jsonHelper;

    @KafkaListener(
            topics = "${adapters.kafka.topic}",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void consume(ConsumerRecord<String, String> record, Acknowledgment ack) {
        try {
            System.out.println("Received kafka record: " + record.value());
            SearchEvent event = jsonHelper.jsonStringToObject(record.value(), SearchEvent.class);
            searchCountRepositoryPort.increment(event.hash());
            ack.acknowledge();
            System.out.println("Record processed successfully for hash: " + event.hash());
        } catch (Exception e) {
            System.out.println("Error processing kafka record: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }
}