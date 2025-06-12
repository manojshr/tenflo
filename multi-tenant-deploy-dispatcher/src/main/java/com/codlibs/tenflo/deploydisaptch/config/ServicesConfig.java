package com.codlibs.tenflo.deploydisaptch.config;

import com.codlibs.tenflo.deploydisaptch.services.AwsService;
import com.codlibs.tenflo.deploydisaptch.services.DeploymentKafkaPublisher;
import com.codlibs.tenflo.deploydisaptch.services.SignatureVerifyService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.reactive.ReactiveKafkaProducerTemplate;
import software.amazon.awssdk.services.secretsmanager.SecretsManagerAsyncClient;
import software.amazon.awssdk.services.sts.StsAsyncClient;

@Configuration
public class ServicesConfig {

    @Bean
    public AwsService awsService(StsAsyncClient stsAsyncClient, SecretsManagerAsyncClient secretsManagerAsyncClient) {
        return new AwsService(stsAsyncClient, secretsManagerAsyncClient);
    }

    @Bean
    public SignatureVerifyService signatureVerifyService(ObjectMapper objectMapper) {
        return new SignatureVerifyService(objectMapper);
    }

    @Bean
    public DeploymentKafkaPublisher deploymentKafkaPublisher(ReactiveKafkaProducerTemplate<String, Object> kafkaProducerTemplate) {
        return new DeploymentKafkaPublisher(kafkaProducerTemplate, "deployments");
    }
}
