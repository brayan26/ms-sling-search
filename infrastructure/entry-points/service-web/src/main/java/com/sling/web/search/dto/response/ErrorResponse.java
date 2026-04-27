package com.sling.web.search.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "Error estándar")
public record ErrorResponse(
        @Schema(example = "VALIDATION_ERROR")
        String code,
        @Schema(example = "Invalid request")
        String message,
        List<FieldErrorDetail> errors
) {}
