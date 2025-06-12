package com.codlibs.tenflo.deploydisaptch.services;

import com.codlibs.tenflo.deploydisaptch.dto.WorkflowRequestBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.reactive.ReactiveKafkaProducerTemplate;
import reactor.core.publisher.Mono;

public class DeploymentKafkaPublisher {

    private static final Logger log = LoggerFactory.getLogger(DeploymentKafkaPublisher.class);

    private final ReactiveKafkaProducerTemplate<String, Object> kafkaProducerTemplate;

    private final String topic;

    public DeploymentKafkaPublisher(ReactiveKafkaProducerTemplate<String, Object> kafkaProducerTemplate, String topic) {
        this.kafkaProducerTemplate = kafkaProducerTemplate;
        this.topic = topic;
    }

    public Mono<Boolean> publish(String tenantId, WorkflowRequestBody requestBody) {
        return kafkaProducerTemplate.send(topic, tenantId, requestBody)
                .doOnSuccess(result -> log.info("Published to Kafka topic: {}, partition: {}, offset: {}",
                        topic, result.recordMetadata().partition(), result.recordMetadata().offset()))
                .doOnError(err -> log.error("Failed to publish to Kafka topic: {}", topic, err))
                .thenReturn(true);
    }
}
