package io.github.evaggelos99.ems.sponsor.service.controller;

import io.github.evaggelos99.ems.common.api.controller.exceptions.DuplicateTicketIdInAttendeeException;
import io.github.evaggelos99.ems.common.api.controller.exceptions.ObjectNotFoundException;
import io.github.evaggelos99.ems.common.api.controller.exceptions.UnauthorizedRoleException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ControllerExceptionAdvice {

    private static final String TIME_STAMP = "timeStamp";

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
        System.out.println("djasdjhasdkas resolved");

        return new ResponseEntity<>(errorResponse, new HttpHeaders(), statusCode);
    }

}
