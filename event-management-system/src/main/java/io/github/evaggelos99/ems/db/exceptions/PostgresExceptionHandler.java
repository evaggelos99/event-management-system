package io.github.evaggelos99.ems.db.exceptions;

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
 *
 * @author Evangelos Georgiou
 *
 */
@ControllerAdvice
public class PostgresExceptionHandler {

    private static final String STATUS = "status";
    private static final String TIME_STAMP = "timeStamp";
    private static final String MESSAGE = "message";
    private static final Logger LOGGER = LoggerFactory.getLogger(PostgresExceptionHandler.class);

    @ExceptionHandler(PSQLException.class)
    public ResponseEntity<Map<String, Object>> handlePSQLExceptionHandler(final PSQLException exc) {

	final Map<String, Object> errorResponse = new HashMap<>();

	errorResponse.put(MESSAGE, exc.getMessage().split("\s{2}")[0]);
	errorResponse.put(TIME_STAMP, Instant.now());
	errorResponse.put(STATUS, 400);

	return new ResponseEntity<>(errorResponse, new HttpHeaders(), 400);

    }

    @ExceptionHandler(DuplicateKeyException.class)
    public ResponseEntity<Map<String, Object>> handleDuplicateKeyException(final DuplicateKeyException exc) {

	final Map<String, Object> errorResponse = new HashMap<>();

	errorResponse.put(MESSAGE, "The uuid key that was provided already exists.");
	errorResponse.put(TIME_STAMP, Instant.now());
	errorResponse.put(STATUS, 409);

	LOGGER.trace("Duplicate key found: ", exc);

	return new ResponseEntity<>(errorResponse, new HttpHeaders(), 409);

    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<
	    Map<String, Object>> handleDataIntegrityViolationException(final DataIntegrityViolationException exc) {

	final Map<String, Object> errorResponse = new HashMap<>();

	errorResponse.put(MESSAGE, exc.getMessage().split(";")[2]);
	errorResponse.put(TIME_STAMP, Instant.now());
	errorResponse.put(STATUS, 400);

	return new ResponseEntity<>(errorResponse, new HttpHeaders(), 400);

    }

}
