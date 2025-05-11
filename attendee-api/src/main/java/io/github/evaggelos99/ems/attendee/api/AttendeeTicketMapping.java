package io.github.evaggelos99.ems.attendee.api;

import io.github.evaggelos99.ems.common.api.domainobjects.IMapping;

import java.util.UUID;

public record AttendeeTicketMapping(UUID attendeeId, UUID ticketId) implements IMapping {

}
