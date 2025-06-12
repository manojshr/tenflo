package com.codlibs.tenflo.deploydisaptch.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.reactive.ReactiveKafkaProducerTemplate;
import org.springframework.kafka.support.serializer.JsonSerializer;
import reactor.kafka.sender.SenderOptions;

@Configuration
@EnableConfigurationProperties(KafkaProperties.class)
public class KafkaConfig {

    @Bean
    public ReactiveKafkaProducerTemplate<String, Object> producerTemplate(JsonSerializer<Object> jsonSerializer, KafkaProperties kafkaProperties) {
        var options = SenderOptions.<String, Object>create(kafkaProperties.buildProducerProperties()).withValueSerializer(jsonSerializer);
        return new ReactiveKafkaProducerTemplate<>(options);
    }

    @Bean
    public JsonSerializer<Object> jsonSerializer(ObjectMapper objectMapper) {
        return new JsonSerializer<>(objectMapper);
    }
}
