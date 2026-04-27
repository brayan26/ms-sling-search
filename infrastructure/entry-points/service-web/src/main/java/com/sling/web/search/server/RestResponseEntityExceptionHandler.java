package com.sling.web.search.server;

import com.sling.model.exceptions.GenericBadRequestException;
import com.sling.model.exceptions.GenericNotFoundException;
import com.sling.web.search.dto.response.ErrorResponse;
import com.sling.web.search.dto.response.FieldErrorDetail;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.List;

@Slf4j
@RestControllerAdvice
public class RestResponseEntityExceptionHandler {

    @ExceptionHandler(value = {IllegalArgumentException.class, IllegalStateException.class})
    public ResponseEntity<?> handleConflict(RuntimeException ex, WebRequest request) {
        ErrorResponse response = new ErrorResponse(
                "INVALID_VALUE_OBJECT",
                ex.getMessage(),
                List.of()
        );
        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(GenericNotFoundException.class)
    public ResponseEntity<?> notFoundError(GenericNotFoundException ex, WebRequest request) {
        log.error(ex.getMessage());
        return new ResponseEntity<>(ex.getError(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(GenericBadRequestException.class)
    public ResponseEntity<?> badRequestError(GenericBadRequestException ex, WebRequest request) {
        log.error(ex.getMessage());
        return new ResponseEntity<>(ex.getError(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> internalError(Exception ex, WebRequest request) {
        log.error(ex.getMessage());
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationErrors(MethodArgumentNotValidException ex, WebRequest request) {

        List<FieldErrorDetail> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> new FieldErrorDetail(
                        error.getField(),
                        error.getDefaultMessage()
                ))
                .toList();

        return new ResponseEntity<>(new ErrorResponse(
                "VALIDATION_ERROR",
                "Invalid request",
                errors
        ), HttpStatus.BAD_REQUEST);
    }

}
