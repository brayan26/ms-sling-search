package com.sling.model.commons;

import java.time.LocalDateTime;
import java.util.List;

public record ErrorResponse(String code, String message, LocalDateTime timestamp, List<String> errors) {
}
