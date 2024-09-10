package io.github.evaggelos99.ems.common.api.controller.exceptions;

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
@ResponseStatus(value = HttpStatus.CONFLICT, reason = "The TicketId is already in the attendee's tickets")
public class DuplicateTicketIdInAttendeeException extends RuntimeException {

	private static final int CODE = 409;

	private static final long serialVersionUID = -4696167699698324726L;

	private static final HttpStatusCode STATUS_CODE = HttpStatusCode.valueOf(CODE);

	private final UUID uuid;

	/**
	 * C-or
	 *
	 * @param uuid          cannot be null
	 * @param classOfObject cannot be null
	 */
	public DuplicateTicketIdInAttendeeException(final UUID uuid) {

		this.uuid = requireNonNull(uuid);
	}

	public UUID getUuid() {

		return uuid;

	}

	public HttpStatusCode getStatusCode() {

		return STATUS_CODE;

	}

}
