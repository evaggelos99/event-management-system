package io.github.evaggelos99.ems.common.api.controller;

import io.github.evaggelos99.ems.common.api.controller.exceptions.DuplicateTicketIdInAttendeeException;
import io.github.evaggelos99.ems.common.api.controller.exceptions.ObjectNotFoundException;
import io.github.evaggelos99.ems.common.api.controller.exceptions.UnauthorizedRoleException;
import io.r2dbc.spi.R2dbcNonTransientException;
import jakarta.validation.ValidationException;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ServerWebInputException;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ControllerExceptionAdvice {

    private static final String TIME_STAMP = "timestamp";

    /**
     * Handle JSON responses that are not valid
     *
     * @param exception
     * @return {@link ResponseEntity} of a JSON response
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<Map<String, Object>> handleMethodArgumentNotValidException(
            final MethodArgumentNotValidException exception) {

        final HttpStatusCode statusCode = exception.getStatusCode();
        final var fieldError = exception.getBindingResult().getFieldError();
        final String field = fieldError.getField();
        final Object valueGiven = fieldError.getRejectedValue();
        final String message = fieldError.getDefaultMessage();

        final Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put(TIME_STAMP, Instant.now());
        errorResponse.put("status", statusCode.value());
        errorResponse.put("error",
                String.format("The field: '%s' %s. Does not accept value: %s", field, message, valueGiven));

        return new ResponseEntity<>(errorResponse, new HttpHeaders(), statusCode);
    }

    /**
     * Handle 404 Errors
     *
     * @param exception
     * @return {@link ResponseEntity} of a JSON response
     */
    @ExceptionHandler(ObjectNotFoundException.class)
    protected ResponseEntity<Map<String, Object>> handleObjectNotFoundException(
            final ObjectNotFoundException exception) {

        final HttpStatusCode statusCode = exception.getStatusCode();

        final Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put(TIME_STAMP, Instant.now());
        errorResponse.put("status", statusCode.value());
        errorResponse.put("error", String.format("The object of class: '%s' with ID: '%s' cannot be found",
                exception.getClassOfObject(), exception.getUuid()));

        return new ResponseEntity<>(errorResponse, new HttpHeaders(), statusCode);
    }

    /**
     * Handle duplicate ticketIds in the same list
     *
     * @param exception
     * @return {@link ResponseEntity} of a JSON response
     */
    @ExceptionHandler(DuplicateTicketIdInAttendeeException.class)
    protected ResponseEntity<Map<String, Object>> handleDuplicateTicketIdInAttendeeException(
            final DuplicateTicketIdInAttendeeException exception) {

        final HttpStatusCode statusCode = exception.getStatusCode();

        final Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put(TIME_STAMP, Instant.now());
        errorResponse.put("status", statusCode.value());
        errorResponse.put("error",
                String.format("The ticket id with ID: '%s' cannot be inserted twice in the attendee's ticketIds",
                        exception.getUuid()));

        return new ResponseEntity<>(errorResponse, new HttpHeaders(), statusCode);
    }

    /**
     * Handle Unauthorized exceptions
     *
     * @param exception
     * @return {@link ResponseEntity} of a JSON response
     */
    @ExceptionHandler(UnauthorizedRoleException.class)
    protected ResponseEntity<Map<String, Object>> handleUnauthorizedRoleException(
            final UnauthorizedRoleException exception) {

        final HttpStatusCode statusCode = exception.getStatusCode();

        final Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put(TIME_STAMP, Instant.now());
        errorResponse.put("status", statusCode.value());
        errorResponse.put("error", exception.getMessage());

        return new ResponseEntity<>(errorResponse, new HttpHeaders(), statusCode);
    }

    @ExceptionHandler(R2dbcNonTransientException.class)
    protected ResponseEntity<Map<String, Object>> handleR2dbcNonTransientException(R2dbcNonTransientException e) {

        final Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put(TIME_STAMP, Instant.now());
        errorResponse.put("status", 400);
        errorResponse.put("error", e.getMessage());
        errorResponse.put("postgres_error", Map.of(
                "sql_state", ObjectUtils.defaultIfNull(e.getSqlState(), "null"),
                "sql", ObjectUtils.defaultIfNull(e.getSql(), "null"),
                "error_code", e.getErrorCode())
        );

        return new ResponseEntity<>(errorResponse, new HttpHeaders(), 400);
    }

    @ExceptionHandler(ServerWebInputException.class)
    protected ResponseEntity<Map<String, Object>> handleValidationException(final ServerWebInputException exception) {

        final String message = exception.getMessage();
        final HttpStatusCode statusCode = exception.getStatusCode();

        final Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put(TIME_STAMP, Instant.now());
        errorResponse.put("status", statusCode.value());
        errorResponse.put("error", message);

        return new ResponseEntity<>(errorResponse, new HttpHeaders(), statusCode);
    }

}
