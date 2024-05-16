package org.com.ems.db.exceptions;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import org.postgresql.util.PSQLException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * TODO must complete
 *
 * @author Evangelos Georgiou
 *
 */
@ControllerAdvice
public class PostgresExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(PostgresExceptionHandler.class);

    @ExceptionHandler(PSQLException.class)
    public ResponseEntity<Map<String, Object>> handlePSQLExceptionHandler(final PSQLException exc) {

	final Map<String, Object> errorResponse = new HashMap<>();

	return new ResponseEntity<>(errorResponse, new HttpHeaders(), 500);

    }

    @ExceptionHandler(DuplicateKeyException.class)
    public ResponseEntity<Map<String, Object>> handleDuplicateKeyException(final DuplicateKeyException exc) {

	final Map<String, Object> errorResponse = new HashMap<>();

	errorResponse.put("message", "The uuid key that was provided already exists.");
	errorResponse.put("timeStamp", Instant.now());
	errorResponse.put("status", 500);

	LOGGER.trace("Duplicate key found: ", exc);

	return new ResponseEntity<>(errorResponse, new HttpHeaders(), 500);

    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<
	    Map<String, Object>> handleDataIntegrityViolationException(final DataIntegrityViolationException exc) {

	final Map<String, Object> errorResponse = new HashMap<>();

	errorResponse.put("message", exc.getMessage());
	errorResponse.put("timeStamp", Instant.now());
	errorResponse.put("status", 500);

	return new ResponseEntity<>(errorResponse, new HttpHeaders(), 500);

    }

}
