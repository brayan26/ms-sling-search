package com.sling.consumer.search.config;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ContainerProperties;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableKafka
public class KafkaConfig {
    private final String servers;
    private final String consumerGroupId;
    private final int sessionTimeoutMs;
    private final int maxPollIntervalMs;
    private final int maxPullRecords;

    public KafkaConfig(@Value("${adapters.kafka.servers}") String servers,
                       @Value("${adapters.kafka.consumer.groupId}") String consumerGroupId,
                       @Value("${adapters.kafka.consumer.max.poll.interval.ms}") int consumerPollIntervalMs,
                       @Value("${adapters.kafka.consumer.max.poll.records}") int maxPullRecords,
                       @Value("${adapters.kafka.consumer.session.timeout}") int sessionTimeout) {
        this.servers = servers;
        this.consumerGroupId = consumerGroupId;
        this.sessionTimeoutMs = sessionTimeout;
        this.maxPullRecords = maxPullRecords;
        this.maxPollIntervalMs = consumerPollIntervalMs;
    }

    @Bean
    public Map<String, Object> consumerProps() throws UnknownHostException {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.CLIENT_ID_CONFIG, InetAddress.getLocalHost().getHostName());
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, servers);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, consumerGroupId);
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
        props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, sessionTimeoutMs);
        props.put(ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG, maxPollIntervalMs);
        props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, maxPullRecords);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        return props;
    }

    @Bean
    public ConsumerFactory<String, String> consumerFactory() throws UnknownHostException {
        return new DefaultKafkaConsumerFactory<>(consumerProps());
    }

    @Bean(name = "kafkaListenerContainerFactory")
    public ConcurrentKafkaListenerContainerFactory<String, String> listenerContainerFactory() throws UnknownHostException {
        ConcurrentKafkaListenerContainerFactory<String, String> listenerContainerFactory = new ConcurrentKafkaListenerContainerFactory<>();
        listenerContainerFactory.setConsumerFactory(consumerFactory());
        listenerContainerFactory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL);
        return listenerContainerFactory;
    }
}