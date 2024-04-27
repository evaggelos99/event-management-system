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
import org.com.ems.controller.api.ITicketController;
import org.com.ems.controller.exceptions.ObjectNotFoundException;
import org.com.ems.services.IService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for CRUD operation for the DAO object {@link Ticket}
 *
 * @author Evangelos Georgiou
 */
@RestController
@RequestMapping("/ticket")
public class TicketController implements ITicketController {

    private final IService<Ticket> ticketService;
    private final Function<Ticket, TicketDto> ticketToTicketDtoConverter;
    private final Function<TicketDto, Ticket> ticketDtoToTicketConverter;

    /**
     * C-or
     *
     * @param ticketService              service responsible for CRUD operations
     * @param ticketToTicketDtoConverter ticket to DTO
     * @param ticketDtoToTicketConverter DTO to ticket
     */
    public TicketController(@Autowired final IService<Ticket> ticketService,
			    @Autowired @Qualifier("ticketToTicketDtoConverter") final Function<Ticket,
				    TicketDto> ticketToTicketDtoConverter,
			    @Autowired @Qualifier("ticketDtoToTicketConverter") final Function<TicketDto,
				    Ticket> ticketDtoToTicketConverter) {

	this.ticketService = requireNonNull(ticketService);
	this.ticketToTicketDtoConverter = requireNonNull(ticketToTicketDtoConverter);
	this.ticketDtoToTicketConverter = requireNonNull(ticketDtoToTicketConverter);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResponseEntity<TicketDto> postTicket(final TicketDto ticketDto) {

	final Ticket newTicket = this.ticketService.add(this.ticketDtoToTicketConverter.apply(ticketDto));

	final TicketDto newDto = this.ticketToTicketDtoConverter.apply(newTicket);

	try {

	    return ResponseEntity.created(new URI("/ticket/")).body(newDto);
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

	final Ticket newTicket = this.ticketService.edit(ticketId, this.ticketDtoToTicketConverter.apply(ticketDto));

	final TicketDto newDto = this.ticketToTicketDtoConverter.apply(newTicket);

	try {

	    return ResponseEntity.created(new URI("/ticket/" + ticketId)).body(newDto);
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
