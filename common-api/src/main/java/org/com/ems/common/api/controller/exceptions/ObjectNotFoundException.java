package org.com.ems.common.api.controller.exceptions;

import static java.util.Objects.requireNonNull;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception thrown when an Entity cannot be found
 *
 * @author Evangelos Georgiou
 *
 */
@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "No such Object found")
public class ObjectNotFoundException extends GenericRestOperationFailedException {

    private static final int CODE = 404;

    private static final long serialVersionUID = -4696167699698324726L;

    private static final HttpStatusCode STATUS_CODE = HttpStatusCode.valueOf(CODE);

    private final UUID uuid;

    private final Class<?> classOfObject;

    /**
     * C-or
     *
     * @param uuid          cannot be null
     * @param classOfObject cannot be null
     */
    public ObjectNotFoundException(final UUID uuid,
				   final Class<?> classOfObject) {

	this.uuid = requireNonNull(uuid);
	this.classOfObject = requireNonNull(classOfObject);

    }

    public UUID getUuid() {

	return this.uuid;

    }

    public String getClassOfObject() {

	return this.classOfObject.getSimpleName();

    }

    public HttpStatusCode getStatusCode() {

	return STATUS_CODE;

    }

}
