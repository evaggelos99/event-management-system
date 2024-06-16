package org.com.ems.db.impl;

import static java.util.Objects.requireNonNull;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Objects;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Component;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class TicketRepository implements ITicketRepository {

    private final DatabaseClient databaseClient;
    private final TicketRowMapper ticketRowMapper;
    private final Function<TicketDto, Ticket> ticketDtoToTicketConverter;
    private final Properties ticketQueriesProperties;

    /**
     * C-or
     *
     * @param databaseClient             the {@link DatabaseClient} used for
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
    public TicketRepository(@Autowired final DatabaseClient databaseClient,
			    @Autowired @Qualifier("ticketRowMapper") final TicketRowMapper ticketRowMapper,
			    @Autowired @Qualifier("ticketDtoToTicketConverter") final Function<TicketDto,
				    Ticket> ticketDtoToTicketConverter,
			    @Autowired @Qualifier("ticketQueriesProperties") final Properties ticketQueriesProperties) {

	this.databaseClient = requireNonNull(databaseClient);
	this.ticketRowMapper = requireNonNull(ticketRowMapper);
	this.ticketDtoToTicketConverter = requireNonNull(ticketDtoToTicketConverter);
	this.ticketQueriesProperties = requireNonNull(ticketQueriesProperties);

    }

    @Override
    public Mono<Ticket> save(final TicketDto organizerDto) {

	return this.saveTicket(organizerDto);

    }

    @Override
    public Mono<Ticket> edit(final TicketDto organizerDto) {

	return this.editTicket(organizerDto);

    }

    @Override
    public Mono<Ticket> findById(final UUID uuid) {

	return this.databaseClient.sql(this.ticketQueriesProperties.getProperty(CrudQueriesOperations.GET_ID.name()))
		.bind(0, uuid).map(this.ticketRowMapper::apply).one();

    }

    @Override
    public Mono<Boolean> deleteById(final UUID uuid) {

	final Mono<Long> rows = this.databaseClient
		.sql(this.ticketQueriesProperties.getProperty(CrudQueriesOperations.DELETE_ID.name())).bind(0, uuid)
		.fetch().rowsUpdated();

	return rows.map(this::rowsAffectedAreMoreThanOne);

    }

    @Override
    public Mono<Boolean> existsById(final UUID uuid) {

	return this.findById(uuid).map(Objects::nonNull);

    }

    @Override
    public Flux<Ticket> findAll() {

	return this.databaseClient.sql(this.ticketQueriesProperties.getProperty(CrudQueriesOperations.GET_ALL.name()))
		.map(this.ticketRowMapper::apply).all();

    }

    private Mono<Ticket> saveTicket(final TicketDto ticket) {

	final UUID ticketUuid = ticket.uuid();
	final Instant now = Instant.now();
	final Timestamp createdAt = Timestamp.from(now);
	final Timestamp updatedAt = Timestamp.from(now);
	final UUID uuid = ticketUuid != null ? ticketUuid : UUID.randomUUID();

	final UUID eventId = ticket.eventID();
	final TicketType ticketType = ticket.ticketType();
	final Integer price = ticket.price();
	final Boolean isTransferable = ticket.transferable();
	final SeatingInformation seatInformation = ticket.seatInformation();

	final Mono<Long> rowsAffected = this.databaseClient
		.sql(this.ticketQueriesProperties.getProperty(CrudQueriesOperations.SAVE.name())).bind(0, uuid)
		.bind(1, createdAt).bind(2, updatedAt).bind(3, eventId).bind(4, ticketType).bind(5, price)
		.bind(6, isTransferable).bind(7, seatInformation.seat()).bind(8, seatInformation.section()).fetch()
		.rowsUpdated();

	return rowsAffected.filter(this::rowsAffectedAreMoreThanOne)
		.map(n_ -> this.ticketDtoToTicketConverter.apply(new TicketDto(uuid, createdAt, updatedAt, eventId,
			ticketType, price, isTransferable, seatInformation)));

    }

    private Mono<Ticket> editTicket(final TicketDto ticket) {

	final UUID uuid = ticket.uuid();
	final Timestamp updatedAt = Timestamp.from(Instant.now());

	final UUID eventId = ticket.eventID();
	final TicketType ticketType = ticket.ticketType();
	final Integer price = ticket.price();
	final Boolean isTransferable = ticket.transferable();
	final SeatingInformation seatInformation = ticket.seatInformation();

	final Mono<Long> rowsAffected = this.databaseClient
		.sql(this.ticketQueriesProperties.getProperty(CrudQueriesOperations.EDIT.name())).bind(0, uuid)
		.bind(1, updatedAt).bind(2, eventId).bind(3, ticketType).bind(4, price).bind(5, isTransferable)
		.bind(6, seatInformation.seat()).bind(7, seatInformation.section()).bind(8, uuid).fetch().rowsUpdated();

	return rowsAffected.filter(this::rowsAffectedAreMoreThanOne).flatMap(n_ -> this.findById(uuid))
		.map(AbstractDomainObject::getCreatedAt)
		.map(monoCreatedAt -> this.ticketDtoToTicketConverter
			.apply(new TicketDto(uuid, Timestamp.from(monoCreatedAt), updatedAt, eventId, ticketType, price,
				isTransferable, seatInformation)));

    }

    private boolean rowsAffectedAreMoreThanOne(final Long x) {

	return x >= 1;

    }

}
