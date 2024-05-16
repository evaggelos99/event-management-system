package org.com.ems.db.impl;

import static java.util.Objects.requireNonNull;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Collection;
import java.util.Optional;
import java.util.Properties;
import java.util.UUID;
import java.util.function.Function;

import org.com.ems.api.domainobjects.SeatingInformation;
import org.com.ems.api.domainobjects.Ticket;
import org.com.ems.api.domainobjects.TicketType;
import org.com.ems.api.dto.TicketDto;
import org.com.ems.db.ITicketRepository;
import org.com.ems.db.queries.Queries.CrudQueriesOperations;
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

	    final Ticket sponsor = this.jdbcTemplate.queryForObject(
		    this.ticketQueriesProperties.getProperty(CrudQueriesOperations.GET_ID.name()), this.ticketRowMapper,
		    uuid);
	    return Optional.of(sponsor);
	} catch (final EmptyResultDataAccessException e) {

	    LOGGER.warn("Ticket with UUID: {} was not found", uuid);
	    return Optional.empty();
	}

    }

    @Override
    public boolean deleteById(final UUID uuid) {

	final int rows = this.jdbcTemplate
		.update(this.ticketQueriesProperties.getProperty(CrudQueriesOperations.DELETE_ID.name()), uuid);

	final boolean deleted = rows == 1 ? true : false;

	if (deleted) {

	    LOGGER.trace("Deleted sponsor with uuid: " + uuid);
	} else {

	    LOGGER.trace("Could not delete sponsor with uuid: " + uuid);
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
	final Timestamp timestamp = Timestamp.from(Instant.now());
	final UUID eventId = ticket.eventID();
	final TicketType ticketType = ticket.ticketType();
	final Integer price = ticket.price();
	final Boolean isTransferable = ticket.transferable();
	final SeatingInformation seatInformation = ticket.seatInfo();

	final UUID uuid = ticketUuid != null ? ticketUuid : UUID.randomUUID();

	this.jdbcTemplate.update(this.ticketQueriesProperties.getProperty(CrudQueriesOperations.SAVE.name()), uuid,
		timestamp, eventId, ticketType.name(), price, isTransferable, seatInformation.getSeat(),
		seatInformation.getSection());

	return this.ticketDtoToTicketConverter
		.apply(new TicketDto(uuid, timestamp, eventId, ticketType, price, isTransferable, seatInformation));

    }

    private Ticket editTicket(final TicketDto ticket) {

	final UUID uuid = ticket.uuid();
	final Timestamp timestamp = Timestamp.from(Instant.now());
	final UUID eventId = ticket.eventID();
	final TicketType ticketType = ticket.ticketType();
	final Integer price = ticket.price();
	final Boolean isTransferable = ticket.transferable();
	final SeatingInformation seatInformation = ticket.seatInfo();

	this.jdbcTemplate.update(this.ticketQueriesProperties.getProperty(CrudQueriesOperations.EDIT.name()), uuid,
		timestamp, eventId, ticketType.name(), price, isTransferable, seatInformation.getSeat(),
		seatInformation.getSection());

	return this.ticketDtoToTicketConverter
		.apply(new TicketDto(uuid, timestamp, eventId, ticketType, price, isTransferable, seatInformation));

    }

}
