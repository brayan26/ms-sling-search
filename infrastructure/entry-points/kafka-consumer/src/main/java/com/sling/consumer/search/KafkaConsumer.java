package com.sling.consumer.search;

import com.sling.consumer.search.dto.SearchEvent;
import com.sling.model.search.port.SearchCountRepositoryPort;
import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;

@Component
//@Slf4j
@RequiredArgsConstructor
public class KafkaConsumer {
    private final SearchCountRepositoryPort searchCountRepositoryPort;
    private final ExecutorService executor;

    @KafkaListener(
            topics = "${adapters.kafka.topic}",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void consume(ConsumerRecord<String, SearchEvent> record, Acknowledgment ack) {
        executor.submit(() -> {
            try {
                SearchEvent event = record.value();
                searchCountRepositoryPort.increment(event.hash());
                ack.acknowledge();
            } catch (Exception e) {
                System.out.println(e.getMessage());
//                log.error("{}", e.getMessage());
                throw new RuntimeException(e);
            }
        });
    }
}
