package org.com.ems.controller.exceptions;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;

/**
 * Exception Handler
 *
 * @author Evangelos Georgiou
 *
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

	/**
	 * Handle JSON responses that are not valid
	 *
	 * @param exception
	 *
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
		errorResponse.put("timeStamp", Instant.now());
		errorResponse.put("status", statusCode.value());
		errorResponse.put("error",
				String.format("The field: '%s' %s. Does not accept value: %s", field, message, valueGiven));

		return new ResponseEntity<>(errorResponse, new HttpHeaders(), statusCode);
	}

	/**
	 * Handle 404 Errors
	 *
	 * @param exception
	 *
	 * @return {@link ResponseEntity} of a JSON response
	 */
	@ExceptionHandler(ObjectNotFoundException.class)
	protected ResponseEntity<Map<String, Object>> handleObjectNotFoundException(
			final ObjectNotFoundException exception) {

		final HttpStatusCode statusCode = exception.getStatusCode();

		final Map<String, Object> errorResponse = new HashMap<>();
		errorResponse.put("timeStamp", Instant.now());
		errorResponse.put("status", statusCode.value());
		errorResponse.put("error", String.format("The object of class: '%s' with ID: '%s' cannot be found",
				exception.getClassOfObject(), exception.getUuid()));

		return new ResponseEntity<>(errorResponse, new HttpHeaders(), statusCode);
	}

	@ExceptionHandler(ConstraintViolationException.class) // does not work FIXME
	protected ResponseEntity<Map<String, Object>> handleConstraintViolationException(
			final ConstraintViolationException ex) {

		final Set<ConstraintViolation<?>> set = ex.getConstraintViolations();

		final Map<String, Object> errorResponse = new HashMap<>();
		errorResponse.put("timeStamp", Instant.now());
		errorResponse.put("errors", set);

		return new ResponseEntity<>(errorResponse, new HttpHeaders(), 400);
	}

}
