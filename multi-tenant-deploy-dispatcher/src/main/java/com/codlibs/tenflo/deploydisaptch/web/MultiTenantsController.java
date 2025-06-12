package com.codlibs.tenflo.deploydisaptch.web;

import com.codlibs.tenflo.deploydisaptch.dto.DeploymentResponse;
import com.codlibs.tenflo.deploydisaptch.dto.WorkflowRequestBody;
import com.codlibs.tenflo.deploydisaptch.exception.AwsException;
import com.codlibs.tenflo.deploydisaptch.exception.SignatureVerificationFailedException;
import com.codlibs.tenflo.deploydisaptch.services.AwsService;
import com.codlibs.tenflo.deploydisaptch.services.DeploymentKafkaPublisher;
import com.codlibs.tenflo.deploydisaptch.services.SignatureVerifyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.KafkaException;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
public class MultiTenantsController {

    private static final Logger log = LoggerFactory.getLogger(MultiTenantsController.class);

    private final AwsService awsService;

    private final SignatureVerifyService signatureVerifyService;

    private final DeploymentKafkaPublisher deploymentKafkaPublisher;

    public MultiTenantsController(AwsService awsService,
                                  SignatureVerifyService signatureVerifyService,
                                  DeploymentKafkaPublisher deploymentKafkaPublisher) {
        this.awsService = awsService;
        this.signatureVerifyService = signatureVerifyService;
        this.deploymentKafkaPublisher = deploymentKafkaPublisher;
    }

    @PostMapping("/tenants/{tenantId}/deploy")
    public Mono<DeploymentResponse> deploy(@PathVariable("tenantId") String tenantId,
                                           @RequestHeader("X-Tenflo-Signature") String xTenfloSignature,
                                           @RequestBody WorkflowRequestBody requestBody) {
        return awsService.retrieveSecret(tenantId, "webhookSecret")
                .map(webhookSecret -> signatureVerifyService.verify(xTenfloSignature, webhookSecret, requestBody))
                .flatMap(bool -> deploymentKafkaPublisher.publish(tenantId, requestBody))
                .map(bool -> {
                    log.info("Deployment request processed for tenantId: {}", tenantId);
                    return new DeploymentResponse("Deployment initiated for tenantId: " + tenantId, true);
                })
                .onErrorReturn(AwsException.class,
                        new DeploymentResponse("Failed to retrieve webhook secret for verifying signature", false))
                .onErrorReturn(SignatureVerificationFailedException.class,
                        new DeploymentResponse("Signature verification failed", false))
                .onErrorReturn(KafkaException.class,
                        new DeploymentResponse("Failed to publish on Kafka", true));
    }
}
