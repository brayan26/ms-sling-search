package com.sling.web.search.dto.response;

import java.util.List;

public record ErrorResponse(
        String code,
        String message,
        List<FieldErrorDetail> errors
) {}
