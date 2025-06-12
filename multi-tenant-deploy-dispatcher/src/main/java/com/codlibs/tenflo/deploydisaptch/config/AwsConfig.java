package com.codlibs.tenflo.deploydisaptch.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.secretsmanager.SecretsManagerAsyncClient;
import software.amazon.awssdk.services.sts.StsAsyncClient;

import java.net.URI;

@Configuration
public class AwsConfig {

    @Value("${spring.cloud.aws.static.region:us-east-1}")
    private String region;

    @Value("${spring.cloud.aws.endpoint}")
    private String endpoint;

    @Bean
    public StsAsyncClient stsAsyncClient() {
        return StsAsyncClient.builder()
                .credentialsProvider(DefaultCredentialsProvider.create())
                .endpointOverride(URI.create(endpoint))
                .region(Region.of(region))
                .build();
    }

    @Bean
    public SecretsManagerAsyncClient secretsManagerClient() {
        return SecretsManagerAsyncClient.builder()
                .credentialsProvider(DefaultCredentialsProvider.create())
                .endpointOverride(URI.create(endpoint))
                .region(Region.of(region))
                .build();
    }
}
