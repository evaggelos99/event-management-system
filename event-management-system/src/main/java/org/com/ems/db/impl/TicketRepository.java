package org.com.ems.db.impl;

import static java.util.Objects.requireNonNull;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Collection;
import java.util.Optional;
import java.util.Properties;
import java.util.UUID;
import java.util.function.Function;

import org.com.ems.api.converters.TicketDtoToTicketConverter;
import org.com.ems.api.domainobjects.AbstractDomainObject;
import org.com.ems.api.domainobjects.SeatingInformation;
import org.com.ems.api.domainobjects.Ticket;
import org.com.ems.api.domainobjects.TicketType;
import org.com.ems.api.dto.TicketDto;
import org.com.ems.db.ITicketRepository;
import org.com.ems.db.queries.Queries.CrudQueriesOperations;
import org.com.ems.db.rowmappers.TicketRowMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

@Component
public class TicketRepository implements ITicketRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(TicketRepository.class);

    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<Ticket> ticketRowMapper;
    private final Function<TicketDto, Ticket> ticketDtoToTicketConverter;
    private final Properties ticketQueriesProperties;

    /**
     * C-or
     *
     * @param jdbcTemplate               the {@link JdbcTemplate} used for
     *                                   connecting to the database for the Ticket
     *                                   objects
     * @param ticketRowMapper            the {@link TicketRowMapper} used for
     *                                   returning Ticket objects from the database
     * @param ticketDtoToTicketConverter the {@link TicketDtoToTicketConverter} used
     *                                   for converting {@link TicketDto} to
     *                                   {@link Ticket}
     * @param ticketQueriesProperties    the {@link Properties} which are used for
     *                                   getting the right query CRUD database
     *                                   operations
     */
    public TicketRepository(@Autowired final JdbcTemplate jdbcTemplate,
			    @Autowired @Qualifier("ticketRowMapper") final RowMapper<Ticket> ticketRowMapper,
			    @Autowired @Qualifier("ticketDtoToTicketConverter") final Function<TicketDto,
				    Ticket> ticketDtoToTicketConverter,
			    @Autowired @Qualifier("ticketQueriesProperties") final Properties ticketQueriesProperties) {

	this.jdbcTemplate = requireNonNull(jdbcTemplate);
	this.ticketRowMapper = requireNonNull(ticketRowMapper);
	this.ticketDtoToTicketConverter = requireNonNull(ticketDtoToTicketConverter);
	this.ticketQueriesProperties = requireNonNull(ticketQueriesProperties);

    }

    @Override
    public Ticket save(final TicketDto organizerDto) {

	final Ticket ticket = this.saveTicket(organizerDto);

	LOGGER.trace("Saved a Ticket: " + ticket);

	return ticket;

    }

    @Override
    public Ticket edit(final TicketDto organizerDto) {

	final Ticket ticket = this.editTicket(organizerDto);

	LOGGER.trace("Saved a Ticket: " + ticket);

	return ticket;

    }

    @Override
    public Optional<Ticket> findById(final UUID id) {

	try {

	    final Ticket ticket = this.jdbcTemplate.queryForObject(
		    this.ticketQueriesProperties.getProperty(CrudQueriesOperations.GET_ID.name()), this.ticketRowMapper,
		    id);
	    return Optional.of(ticket);
	} catch (final EmptyResultDataAccessException e) {

	    LOGGER.warn("Ticket with UUID: {} was not found", id);
	    return Optional.empty();
	}

    }

    @Override
    public boolean deleteById(final UUID id) {

	final int rows = this.jdbcTemplate
		.update(this.ticketQueriesProperties.getProperty(CrudQueriesOperations.DELETE_ID.name()), id);

	final boolean deleted = rows == 1;

	if (deleted) {

	    LOGGER.trace("Deleted Ticket with id: " + id);
	} else {

	    LOGGER.trace("Could not delete Ticket with id: " + id);
	}

	return deleted;

    }

    @Override
    public boolean existsById(final UUID id) {

	return this.findById(id).isPresent();

    }

    @Override
    public Collection<Ticket> findAll() {

	return this.jdbcTemplate.query(this.ticketQueriesProperties.getProperty(CrudQueriesOperations.GET_ALL.name()),
		this.ticketRowMapper);

    }

    private Ticket saveTicket(final TicketDto ticket) {

	final UUID ticketUuid = ticket.id();
	final Instant now = Instant.now();
	final Timestamp createdAt = Timestamp.from(now);
	final Timestamp updatedAt = Timestamp.from(now);
	final UUID id = ticketUuid != null ? ticketUuid : UUID.randomUUID();

	return this.saveOrEditCommand(id, createdAt, updatedAt, ticket, true);

    }

    private Ticket editTicket(final TicketDto ticket) {

	final UUID id = ticket.id();
	final Timestamp createdAt = this.getCreatedAt(id);
	final Timestamp updatedAt = Timestamp.from(Instant.now());

	return this.saveOrEditCommand(id, createdAt, updatedAt, ticket, false);

    }

    private Ticket saveOrEditCommand(final UUID id,
				     final Timestamp createdAt,
				     final Timestamp updatedAt,
				     final TicketDto ticket,
				     final boolean isSaveOperation) {

	final UUID eventId = ticket.eventID();
	final TicketType ticketType = ticket.ticketType();
	final Integer price = ticket.price();
	final Boolean isTransferable = ticket.transferable();
	final SeatingInformation seatInformation = ticket.seatInformation();

	if (isSaveOperation) {

	    this.jdbcTemplate.update(this.ticketQueriesProperties.getProperty(CrudQueriesOperations.SAVE.name()), id,
		    createdAt, updatedAt, eventId, ticketType.name(), price, isTransferable, seatInformation.seat(),
		    seatInformation.section());
	} else {

	    this.jdbcTemplate.update(this.ticketQueriesProperties.getProperty(CrudQueriesOperations.EDIT.name()), id,
		    createdAt, updatedAt, eventId, ticketType.name(), price, isTransferable, seatInformation.seat(),
		    seatInformation.section(), id);
	}

	return this.ticketDtoToTicketConverter.apply(
		new TicketDto(id, createdAt, updatedAt, eventId, ticketType, price, isTransferable, seatInformation));

    }

    private Timestamp getCreatedAt(final UUID id) {

	return Timestamp.from(this.findById(id).map(AbstractDomainObject::getCreatedAt).orElse(Instant.now()));

    }

}
