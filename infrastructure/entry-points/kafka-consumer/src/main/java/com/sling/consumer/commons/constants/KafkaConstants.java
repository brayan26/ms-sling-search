package com.sling.consumer.commons.constants;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class KafkaConstants {
    public static final String SASL_JAAS_CONFIG_PROP = "org.apache.kafka.common.security.scram.ScramLoginModule required username=\"%s\" password=\"%s\";";
    public static final String ACH_REVERSAL_CONSUMER_GROUP = "ach-reversal-consumer-group";
    public static final String AUTO_OFFSET_RESET_CONFIG_PROP = "earliest";

    public static final String ERROR_MESSAGE_KAFKA_CONSUMER = "Error in kafka consuming";
    public static final String MESSAGE_KAFKA_CONSUMER_REQUEST = "Message from kafka consumer topic";
    public static final String OPERATION = "KafkaConsumer";
    public static final String KAFKA_CONSUMER_OPERATION = "KafkaConsumerPayload";
    public static final String KAFKA_PAYLOAD_REQUEST = "PayloadRequest";
}
