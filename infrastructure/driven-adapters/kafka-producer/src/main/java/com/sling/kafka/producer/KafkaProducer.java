package com.sling.kafka.producer;

import com.sling.model.search.Search;
import com.sling.model.search.port.EventPublisherPort;
import org.springframework.stereotype.Component;

@Component
public class KafkaProducer implements EventPublisherPort {

    @Override
    public void publish(Search search) {

    }
}
