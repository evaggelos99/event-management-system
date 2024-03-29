package org.com.ems.controller.exceptions;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value=HttpStatus.NOT_FOUND, reason="No such Object found")
public class ObjectNotFoundException extends GenericRestOperationFailedException {

	private static final int CODE = 404;

	private static final long serialVersionUID = -4696167699698324726L;

	private static final HttpStatusCode STATUS_CODE = HttpStatusCode.valueOf(CODE);
	
	private final UUID uuid;
	
	private final Class<?> classOfObject;
	
	public ObjectNotFoundException(UUID uuid, Class<?> classOfObject) {
		this.uuid = uuid;
		this.classOfObject = classOfObject;
	}

	public UUID getUuid() {
		return uuid;
	}

	public String getClassOfObject() {
		return classOfObject.getSimpleName();
	}

	public HttpStatusCode getStatusCode() {
		return STATUS_CODE;
	}
	
	
}
