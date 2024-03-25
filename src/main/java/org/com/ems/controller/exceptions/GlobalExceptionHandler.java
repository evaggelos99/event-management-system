package org.com.ems.controller.exceptions;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(MethodArgumentNotValidException.class)
	protected ResponseEntity<Map<String, Object>> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {

		HttpStatusCode statusCode = ex.getStatusCode();
		var fieldError = ex.getBindingResult().getFieldError();
		String field = fieldError.getField();
		Object valueGiven = fieldError.getRejectedValue();
		String message = fieldError.getDefaultMessage();

		Map<String, Object> errorResponse = new HashMap<>();
		errorResponse.put("timeStamp", Instant.now());
		errorResponse.put("status", statusCode.value());
		errorResponse.put("error", String.format("The field: '%s' %s. Does not accept value: %s", field,message, valueGiven));

		return new ResponseEntity<>(errorResponse, new HttpHeaders(), statusCode);
	}
	
	@ExceptionHandler(ObjectNotFoundException.class)
	protected ResponseEntity<Map<String, Object>> handleObjectNotFound(ObjectNotFoundException ex) {

		HttpStatusCode statusCode = ex.getStatusCode();
		
		Map<String, Object> errorResponse = new HashMap<>();
		errorResponse.put("timeStamp", Instant.now());
		errorResponse.put("status", statusCode.value());
		errorResponse.put("error", String.format("The object of class: '%s' with ID: '%s' cannot be found", ex.getClassOfObject(), ex.getUuid()));

		return new ResponseEntity<>(errorResponse, new HttpHeaders(), statusCode);
	}

}
