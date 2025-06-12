package com.codlibs.tenflo.deploydisaptch.dto;

public record WorkflowRequestBody(
        String tenantId,
        String deploymentId,
        String ref,
        String commitId,
        String repo,
        String env,
        String timestamp
) {
}
