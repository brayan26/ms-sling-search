package com.sling.model.exceptions;

import com.sling.model.commons.ErrorResponse;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class GenericNotFoundException extends RuntimeException {
    private final ErrorResponse error;

    public GenericNotFoundException(String message, ErrorResponse error) {
        super(message);
        this.error = error;
    }
}
