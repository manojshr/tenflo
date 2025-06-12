package com.codlibs.tenflo.deploydisaptch.services;

import com.codlibs.tenflo.deploydisaptch.exception.SignatureVerificationFailedException;
import com.codlibs.tenflo.deploydisaptch.dto.WorkflowRequestBody;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.codec.digest.HmacAlgorithms;
import org.apache.commons.codec.digest.HmacUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;

public class SignatureVerifyService {
    private final Logger log = LoggerFactory.getLogger(SignatureVerifyService.class);

    private final ObjectMapper objectMapper;

    public SignatureVerifyService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public boolean verify(String xTenfloSignature, String webhookSecret, WorkflowRequestBody requestBody) {
        try {
            log.info("requestBody: {}", requestBody);
            String computedSignature = new HmacUtils(HmacAlgorithms.HMAC_SHA_256, webhookSecret)
                    .hmacHex(objectMapper.writeValueAsString(requestBody));
            log.info("Computed signature: {}", computedSignature);
            if (!computedSignature.equals(xTenfloSignature)) {
                throw new SignatureVerificationFailedException("Signature verification failed: " +
                        "computed signature does not match the provided signature");
            }
            log.info("Signature verified successfully");
            return true;
        } catch (JsonProcessingException e) {
            throw new SignatureVerificationFailedException("Failed to convert request body to bytes", e);
        }
    }
}
