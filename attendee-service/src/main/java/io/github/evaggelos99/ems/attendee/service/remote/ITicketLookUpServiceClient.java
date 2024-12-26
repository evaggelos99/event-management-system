package io.github.evaggelos99.ems.attendee.service.remote;

import java.util.UUID;

import io.github.evaggelos99.ems.ticket.api.TicketDto;
import reactor.core.publisher.Mono;

public interface ITicketLookUpServiceClient extends IRemoteServiceClient {

	/**
	 * Returns the Ticket matching the provided UUID
	 *
	 * @param id the UUID of the ticket
	 * @return the TicketDto based on the ID
	 */
	public Mono<TicketDto> lookUpTicket(final UUID id);
}
