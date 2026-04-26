package com.sling.kafka.producer.config;

import com.sling.kafka.producer.KafkaPublish;
import com.sling.model.helper.gateways.JsonHelper;
import com.sling.model.search.port.EventPublisherPort;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.prometheusmetrics.PrometheusConfig;
import io.micrometer.prometheusmetrics.PrometheusMeterRegistry;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.MicrometerProducerListener;
import org.springframework.kafka.core.ProducerFactory;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaProducerConfig {
    private final String servers;
    private final String acks;
    private final int retries;
    private final Integer batchSize;
    private final int linger;
    private final long buffer;
    private final int flightRequest;
    private final boolean idempotence;

    public KafkaProducerConfig(
            @Value("${adapters.kafka.servers}") String servers,
            @Value("${adapters.kafka.producer.acks}") String acks,
            @Value("${adapters.kafka.producer.retries}") int retries,
            @Value("${adapters.kafka.producer.batch.size}") int batchSize,
            @Value("${adapters.kafka.producer.linger.ms}") int linger,
            @Value("${adapters.kafka.producer.buffer.memory}") long buffer,
            @Value("${adapters.kafka.producer.max.flight.request}") int flightRequest,
            @Value("${adapters.kafka.producer.enable.idempotence}") boolean idempotence
    ) {
        this.servers = servers;
        this.acks = acks;
        this.retries = retries;
        this.batchSize = batchSize;
        this.linger = linger;
        this.buffer = buffer;
        this.flightRequest = flightRequest;
        this.idempotence = idempotence;
    }

    @Bean
    public Map<String, Object> producerProps() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, servers);
        props.put(ProducerConfig.ACKS_CONFIG, acks);
        props.put(ProducerConfig.RETRIES_CONFIG, retries);
        props.put(ProducerConfig.BATCH_SIZE_CONFIG, batchSize);
        props.put(ProducerConfig.LINGER_MS_CONFIG, linger);
        props.put(ProducerConfig.BUFFER_MEMORY_CONFIG, buffer);
        props.put(ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION, flightRequest);
        props.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, idempotence);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        return props;
    }

    @Bean
    public ProducerFactory<String, String> producerFactory() {
        return new DefaultKafkaProducerFactory<>(producerProps());
    }

    @Bean
    public KafkaTemplate<String, String> kafkaTemplate() {
        DefaultKafkaProducerFactory<String, String> kafkaProducerFactory = new DefaultKafkaProducerFactory<>(producerProps());
        kafkaProducerFactory.addListener(new MicrometerProducerListener<>(meterRegistry()));
        return new KafkaTemplate<>(kafkaProducerFactory);
    }

    @Bean
    public MeterRegistry meterRegistry() {
        return new PrometheusMeterRegistry(PrometheusConfig.DEFAULT);
    }

    @Bean
    public EventPublisherPort eventPublisherPort(
            @Value("${adapters.kafka.producer.key}") String producerKey,
            @Value("${adapters.kafka.topic}") String topicName,
            KafkaTemplate<String, String> kafkaTemplate,
            JsonHelper jsonHelper) {
        return new KafkaPublish(producerKey, topicName, kafkaTemplate, jsonHelper);
    }
}
