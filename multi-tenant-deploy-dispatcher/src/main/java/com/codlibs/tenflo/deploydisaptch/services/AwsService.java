package com.codlibs.tenflo.deploydisaptch.services;

import com.codlibs.tenflo.deploydisaptch.exception.AwsException;
import com.codlibs.tenflo.deploydisaptch.utils.Json;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.auth.credentials.AwsSessionCredentials;
import software.amazon.awssdk.services.secretsmanager.SecretsManagerAsyncClient;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueRequest;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueResponse;
import software.amazon.awssdk.services.sts.StsAsyncClient;
import software.amazon.awssdk.services.sts.model.AssumeRoleResponse;
import software.amazon.awssdk.services.sts.model.Credentials;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import static com.codlibs.tenflo.deploydisaptch.Helper.mkRole;
import static com.codlibs.tenflo.deploydisaptch.utils.Json.json;

public class AwsService {

    private final Logger log = LoggerFactory.getLogger(AwsService.class);

    private final StsAsyncClient stsAsyncClient;

    private final SecretsManagerAsyncClient secretsManagerAsyncClient;

    public AwsService(StsAsyncClient stsAsyncClient, SecretsManagerAsyncClient secretsManagerAsyncClient) {
        this.stsAsyncClient = stsAsyncClient;
        this.secretsManagerAsyncClient = secretsManagerAsyncClient;
    }

    public Mono<String> retrieveSecret(String tenantId, String secretName) {
        return Mono.fromFuture(() -> assumeRole(tenantId))
                .flatMap(assumedRole -> Mono.fromFuture(() -> retrieveSecrets(assumedRole.credentials(), tenantId)))
                .doOnError(throwable -> log.error("Failed to assume role for tenant: {}", tenantId, throwable))
                .map(secrets -> json(secrets.secretString()).field("webhookSecret")
                        .orElseThrow(() -> new AwsException("Failed to retrieve secret: " + secretName + " for tenant: " + tenantId)));
    }

    private CompletableFuture<AssumeRoleResponse> assumeRole(String tenantId) {
        var roleArn = mkRole(tenantId);
        var roleSession = "session-%s".formatted(tenantId);
        log.info("roleArn: {}; roleSession: {}", roleArn, roleSession);
        return stsAsyncClient.assumeRole(req -> req.roleArn(roleArn).roleSessionName(roleSession));
    }

    private CompletableFuture<GetSecretValueResponse> retrieveSecrets(Credentials credentials, String tenantId) {
        AwsSessionCredentials sessionCredentials = AwsSessionCredentials.create(
                credentials.accessKeyId(),
                credentials.secretAccessKey(),
                credentials.sessionToken());
        String secretId = "/secrets/tenflo/tenant/%s".formatted(tenantId);
        log.info("Retrieving secrets for tenantId: {}, secretId: {}", tenantId, secretId);
        return secretsManagerAsyncClient
                .getSecretValue(GetSecretValueRequest.builder().secretId(secretId)
                .overrideConfiguration(config -> config.credentialsProvider(() -> sessionCredentials)).build());
    }
}
