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
    public Optional<Ticket> findById(final UUID uuid) {

	try {

	    final Ticket ticket = this.jdbcTemplate.queryForObject(
		    this.ticketQueriesProperties.getProperty(CrudQueriesOperations.GET_ID.name()), this.ticketRowMapper,
		    uuid);
	    return Optional.of(ticket);
	} catch (final EmptyResultDataAccessException e) {

	    LOGGER.warn("Ticket with UUID: {} was not found", uuid);
	    return Optional.empty();
	}

    }

    @Override
    public boolean deleteById(final UUID uuid) {

	final int rows = this.jdbcTemplate
		.update(this.ticketQueriesProperties.getProperty(CrudQueriesOperations.DELETE_ID.name()), uuid);

	final boolean deleted = rows == 1;

	if (deleted) {

	    LOGGER.trace("Deleted Ticket with uuid: " + uuid);
	} else {

	    LOGGER.trace("Could not delete Ticket with uuid: " + uuid);
	}

	return deleted;

    }

    @Override
    public boolean existsById(final UUID uuid) {

	return this.findById(uuid).isPresent();

    }

    @Override
    public Collection<Ticket> findAll() {

	return this.jdbcTemplate.query(this.ticketQueriesProperties.getProperty(CrudQueriesOperations.GET_ALL.name()),
		this.ticketRowMapper);

    }

    private Ticket saveTicket(final TicketDto ticket) {

	final UUID ticketUuid = ticket.uuid();
	final Instant now = Instant.now();
	final Timestamp createdAt = Timestamp.from(now);
	final Timestamp timestamp = Timestamp.from(now);
	final UUID eventId = ticket.eventID();
	final TicketType ticketType = ticket.ticketType();
	final Integer price = ticket.price();
	final Boolean isTransferable = ticket.transferable();
	final SeatingInformation seatInformation = ticket.seatInformation();

	final UUID uuid = ticketUuid != null ? ticketUuid : UUID.randomUUID();

	this.jdbcTemplate.update(this.ticketQueriesProperties.getProperty(CrudQueriesOperations.SAVE.name()), uuid,
		createdAt, timestamp, eventId, ticketType.name(), price, isTransferable, seatInformation.getSeat(),
		seatInformation.getSection());

	return this.ticketDtoToTicketConverter.apply(
		new TicketDto(uuid, createdAt, timestamp, eventId, ticketType, price, isTransferable, seatInformation));

    }

    private Ticket editTicket(final TicketDto ticket) {

	final UUID uuid = ticket.uuid();
	final Timestamp createdAt = this.getCreatedAt(uuid);
	final Timestamp timestamp = Timestamp.from(Instant.now());
	final UUID eventId = ticket.eventID();
	final TicketType ticketType = ticket.ticketType();
	final Integer price = ticket.price();
	final Boolean isTransferable = ticket.transferable();
	final SeatingInformation seatInformation = ticket.seatInformation();

	this.jdbcTemplate.update(this.ticketQueriesProperties.getProperty(CrudQueriesOperations.EDIT.name()), uuid,
		createdAt, timestamp, eventId, ticketType.name(), price, isTransferable, seatInformation.getSeat(),
		seatInformation.getSection(), uuid);

	return this.ticketDtoToTicketConverter.apply(
		new TicketDto(uuid, createdAt, timestamp, eventId, ticketType, price, isTransferable, seatInformation));

    }

    private Timestamp getCreatedAt(final UUID uuid) {

	return Timestamp.from(this.findById(uuid).map(AbstractDomainObject::getCreatedAt).orElse(Instant.now()));

    }

}
