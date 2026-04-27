package com.sling.web.search.dto.response;

public record FieldErrorDetail(
        String field,
        String message
) {}
