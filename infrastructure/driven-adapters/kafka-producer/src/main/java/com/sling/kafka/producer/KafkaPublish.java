package com.sling.kafka.producer;

import com.sling.model.search.Search;
import com.sling.model.search.port.EventPublisherPort;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;

@RequiredArgsConstructor
public class KafkaPublish implements EventPublisherPort {
    private final String producerKey;
    private final String topicName;
    private final KafkaTemplate<String, String> kafkaTemplate;

    @Override
    public void publish(Search search) {
        kafkaTemplate.send(topicName, producerKey, search.toString());
    }
}
