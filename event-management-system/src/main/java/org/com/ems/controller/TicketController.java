package org.com.ems.controller;

import static java.util.Objects.requireNonNull;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;

import org.com.ems.api.domainobjects.Ticket;
import org.com.ems.api.dto.TicketDto;
import org.com.ems.controller.api.EmsRestController;
import org.com.ems.controller.api.ITicketController;
import org.com.ems.controller.exceptions.ObjectNotFoundException;
import org.com.ems.services.api.IService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controller for CRUD operation for the DAO object {@link Ticket}
 *
 * @author Evangelos Georgiou
 */
@EmsRestController
@RequestMapping(TicketController.TICKET_PATH)
public class TicketController implements ITicketController {

    static final String TICKET_PATH = "/ticket";
    private final IService<Ticket, TicketDto> ticketService;
    private final Function<Ticket, TicketDto> ticketToTicketDtoConverter;

    /**
     * C-or
     *
     * @param ticketService              service responsible for CRUD operations
     * @param ticketToTicketDtoConverter ticket to DTO
     */
    public TicketController(@Autowired final IService<Ticket, TicketDto> ticketService,
			    @Autowired @Qualifier("ticketToTicketDtoConverter") final Function<Ticket,
				    TicketDto> ticketToTicketDtoConverter) {

	this.ticketService = requireNonNull(ticketService);
	this.ticketToTicketDtoConverter = requireNonNull(ticketToTicketDtoConverter);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResponseEntity<TicketDto> postTicket(final TicketDto ticketDto) {

	final Ticket newTicket = this.ticketService.add(ticketDto);

	final TicketDto newDto = this.ticketToTicketDtoConverter.apply(newTicket);

	try {

	    return ResponseEntity.created(new URI(TICKET_PATH)).body(newDto);
	} catch (final URISyntaxException e) {

	    return new ResponseEntity<>(newDto, HttpStatus.CREATED);
	}

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResponseEntity<TicketDto> getTicket(final UUID ticketId) {

	final var optionalTicket = this.ticketService.get(ticketId);

	final TicketDto ticketDto = this.ticketToTicketDtoConverter
		.apply(optionalTicket.orElseThrow(() -> new ObjectNotFoundException(ticketId, TicketDto.class)));

	return ResponseEntity.ok(ticketDto);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResponseEntity<TicketDto> putTicket(final UUID ticketId,
					       final TicketDto ticketDto) {

	final Ticket newTicket = this.ticketService.edit(ticketId, ticketDto);

	final TicketDto newDto = this.ticketToTicketDtoConverter.apply(newTicket);

	try {

	    return ResponseEntity.created(new URI(TICKET_PATH + ticketId)).body(newDto);
	} catch (final URISyntaxException e) {

	    return new ResponseEntity<>(newDto, HttpStatus.CREATED);
	}

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResponseEntity<?> deleteTicket(final UUID ticketId) {

	if (!this.ticketService.existsById(ticketId)) {

	    throw new ObjectNotFoundException(ticketId, Ticket.class);
	}

	this.ticketService.delete(ticketId);

	return ResponseEntity.noContent().build();

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResponseEntity<Collection<TicketDto>> getTickets() {

	final List<TicketDto> listOfDtos = this.ticketService.getAll().stream()
		.map(this.ticketToTicketDtoConverter::apply).toList();

	return ResponseEntity.ok(listOfDtos);

    }

}
