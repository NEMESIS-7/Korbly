package com.arete.korbly.modules.shared.enums;

public record UploadFileResponse(
        String fileName,
        String key,
        String contentType,
        String etag,
        Long size,
        String versionId
) {
}
