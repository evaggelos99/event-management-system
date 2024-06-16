package org.com.ems.db.impl;

import static java.util.Objects.requireNonNull;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.Properties;
import java.util.UUID;
import java.util.function.Function;

import org.com.ems.api.converters.AttendeeDtoToAttendeeConverter;
import org.com.ems.api.domainobjects.AbstractDomainObject;
import org.com.ems.api.domainobjects.Attendee;
import org.com.ems.api.dto.AttendeeDto;
import org.com.ems.db.IAttendeeRepository;
import org.com.ems.db.queries.Queries.CrudQueriesOperations;
import org.com.ems.db.rowmappers.AttendeeRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Component;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class AttendeeRepository implements IAttendeeRepository {

    private final DatabaseClient databaseClient;
    private final AttendeeRowMapper attendeeRowMapper;
    private final Function<AttendeeDto, Attendee> attendeeDtoToAttendeeConverter;
    private final Properties attendeeQueriesProperties;

    /**
     * C-or
     *
     * @param jdbcTemplate                   the {@link DatabaseClient} used for
     *                                       connecting to the database for the
     *                                       Attendee objects
     * @param databaseClient                 the {@link AttendeeRowMapper} used for
     *                                       returning Attendee objects from the
     *                                       database
     * @param attendeeDtoToAttendeeConverter the
     *                                       {@link AttendeeDtoToAttendeeConverter}
     *                                       used for converting {@link AttendeeDto}
     *                                       to {@link Attendee}
     * @param attendeeQueriesProperties      the {@link Properties} which are used
     *                                       for getting the right query CRUD
     *                                       database operations
     */
    public AttendeeRepository(@Autowired final DatabaseClient jdbcTemplate,
			      @Autowired @Qualifier("attendeeRowMapper") final AttendeeRowMapper databaseClient,
			      @Autowired @Qualifier("attendeeDtoToAttendeeConverter") final Function<AttendeeDto,
				      Attendee> attendeeDtoToAttendeeConverter,
			      @Autowired @Qualifier("attendeeQueriesProperties") final Properties attendeeQueriesProperties) {

	this.databaseClient = requireNonNull(jdbcTemplate);
	this.attendeeRowMapper = requireNonNull(databaseClient);
	this.attendeeDtoToAttendeeConverter = requireNonNull(attendeeDtoToAttendeeConverter);
	this.attendeeQueriesProperties = requireNonNull(attendeeQueriesProperties);

    }

    @Override
    public Mono<Attendee> save(final AttendeeDto attendeeDto) {

	return this.saveAttendee(attendeeDto);

    }

    @Override
    public Mono<Attendee> edit(final AttendeeDto attendeeDto) {

	return this.editAttendee(attendeeDto);

    }

    @Override
    public Mono<Attendee> findById(final UUID uuid) {

	return this.databaseClient.sql(this.attendeeQueriesProperties.getProperty(CrudQueriesOperations.GET_ID.name()))
		.bind(0, uuid).map(this.attendeeRowMapper::apply).one();

    }

    @Override
    public Mono<Boolean> deleteById(final UUID uuid) {

	return this.databaseClient.sql(this.attendeeQueriesProperties.getProperty(CrudQueriesOperations.SAVE.name()))
		.bind(0, uuid).fetch().rowsUpdated().map(this::rowsAffectedAreMoreThanOne);

    }

    @Override
    public Mono<Boolean> existsById(final UUID uuid) {

	return this.findById(uuid).map(Objects::nonNull);

    }

    @Override
    public Flux<Attendee> findAll() {

	return this.databaseClient.sql(this.attendeeQueriesProperties.getProperty(CrudQueriesOperations.GET_ALL.name()))
		.map(this.attendeeRowMapper::apply).all();

    }

    private Mono<Attendee> saveAttendee(final AttendeeDto attendee) {

	final UUID attendeeId = attendee.uuid();
	final Instant now = Instant.now();
	final Timestamp createdAt = Timestamp.from(now);
	final Timestamp updatedAt = Timestamp.from(now);
	final UUID uuid = attendeeId != null ? attendeeId : UUID.randomUUID();

	final List<UUID> ticketIds = attendee.ticketIDs() != null ? attendee.ticketIDs() : List.of();
	final UUID[] uuids = this.convertToArray(ticketIds);

	final Mono<Long> rowsAffected = this.databaseClient
		.sql(this.attendeeQueriesProperties.getProperty(CrudQueriesOperations.SAVE.name())).bind(0, uuid)
		.bind(1, createdAt).bind(2, updatedAt).bind(3, attendee.firstName()).bind(4, attendee.lastName())
		.bind(5, uuids).fetch().rowsUpdated();

	return rowsAffected.filter(this::rowsAffectedAreMoreThanOne)
		.map(n_ -> this.attendeeDtoToAttendeeConverter.apply(new AttendeeDto(uuid, createdAt, updatedAt,
			attendee.firstName(), attendee.lastName(), ticketIds)));

    }

    private Mono<Attendee> editAttendee(final AttendeeDto attendee) {

	final UUID uuid = attendee.uuid();
	final Timestamp updatedAt = Timestamp.from(Instant.now());

	final List<UUID> ticketIds = attendee.ticketIDs() != null ? attendee.ticketIDs() : List.of();
	final UUID[] uuids = this.convertToArray(ticketIds);

	final Mono<Long> rowsAffected = this.databaseClient
		.sql(this.attendeeQueriesProperties.getProperty(CrudQueriesOperations.EDIT.name())).bind(0, uuid)
		.bind(1, updatedAt).bind(2, attendee.firstName()).bind(3, attendee.lastName()).bind(4, uuids)
		.bind(5, uuid).fetch().rowsUpdated();

	return rowsAffected.filter(this::rowsAffectedAreMoreThanOne).flatMap(n_ -> this.findById(uuid))
		.map(AbstractDomainObject::getCreatedAt)
		.map(createdAt -> this.attendeeDtoToAttendeeConverter.apply(new AttendeeDto(uuid,
			Timestamp.from(createdAt), updatedAt, attendee.firstName(), attendee.lastName(), ticketIds)));

    }

    private UUID[] convertToArray(final List<UUID> ticketIds) {

	if (null == ticketIds) {

	    return new UUID[] {};
	}

	final UUID[] uuids = new UUID[ticketIds.size()];

	for (int i = 0; i < uuids.length; i++) {

	    uuids[i] = ticketIds.get(i);
	}
	return uuids;

    }

    private boolean rowsAffectedAreMoreThanOne(final Long x) {

	return x >= 1;

    }

}
