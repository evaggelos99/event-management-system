package org.com.ems.db.impl;

import static java.util.Objects.requireNonNull;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

@Component
public class AttendeeRepository implements IAttendeeRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(AttendeeRepository.class);

    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<Attendee> attendeeRowMapper;
    private final Function<AttendeeDto, Attendee> attendeeDtoToAttendeeConverter;
    private final Properties attendeeQueriesProperties;

    /**
     * C-or
     *
     * @param jdbcTemplate                   the {@link JdbcTemplate} used for
     *                                       connecting to the database for the
     *                                       Attendee objects
     * @param attendeeRowMapper              the {@link AttendeeRowMapper} used for
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
    public AttendeeRepository(@Autowired final JdbcTemplate jdbcTemplate,
			      @Autowired @Qualifier("attendeeRowMapper") final RowMapper<Attendee> attendeeRowMapper,
			      @Autowired @Qualifier("attendeeDtoToAttendeeConverter") final Function<AttendeeDto,
				      Attendee> attendeeDtoToAttendeeConverter,
			      @Autowired @Qualifier("attendeeQueriesProperties") final Properties attendeeQueriesProperties) {

	this.jdbcTemplate = requireNonNull(jdbcTemplate);
	this.attendeeRowMapper = requireNonNull(attendeeRowMapper);
	this.attendeeDtoToAttendeeConverter = requireNonNull(attendeeDtoToAttendeeConverter);
	this.attendeeQueriesProperties = requireNonNull(attendeeQueriesProperties);

    }

    @Override
    public Attendee save(final AttendeeDto attendeeDto) {

	final Attendee attendee = this.saveAttendee(attendeeDto);
	LOGGER.trace("Saved an Attendee: " + attendee);

	return attendee;

    }

    @Override
    public Attendee edit(final AttendeeDto attendeeDto) {

	final Attendee attendee = this.editAttendee(attendeeDto);
	LOGGER.trace("Edited an Attendee: " + attendee);

	return attendee;

    }

    @Override
    public Optional<Attendee> findById(final UUID id) {

	try {

	    final Attendee attendee = this.jdbcTemplate.queryForObject(
		    this.attendeeQueriesProperties.getProperty(CrudQueriesOperations.GET_ID.name()),
		    this.attendeeRowMapper, id);
	    return Optional.of(attendee);
	} catch (final EmptyResultDataAccessException e) {

	    LOGGER.warn("Attendee with UUID: {} was not found", id);
	    return Optional.empty();
	}

    }

    @Override
    public boolean deleteById(final UUID id) {

	final int rows = this.jdbcTemplate
		.update(this.attendeeQueriesProperties.getProperty(CrudQueriesOperations.DELETE_ID.name()), id);

	final boolean deleted = rows == 1;

	if (deleted) {

	    LOGGER.trace("Deleted attendee with id: " + id);
	} else {

	    LOGGER.trace("Could not delete attendee with id: " + id);
	}

	return deleted;

    }

    @Override
    public boolean existsById(final UUID id) {

	return this.findById(id).isPresent();

    }

    @Override
    public Collection<Attendee> findAll() {

	return this.jdbcTemplate.query(this.attendeeQueriesProperties.getProperty(CrudQueriesOperations.GET_ALL.name()),
		this.attendeeRowMapper);

    }

    private Attendee saveAttendee(final AttendeeDto attendee) {

	final UUID attendeeId = attendee.id();
	final Instant now = Instant.now();
	final Timestamp createdAt = Timestamp.from(now);
	final Timestamp updatedAt = Timestamp.from(now);
	final UUID id = attendeeId != null ? attendeeId : UUID.randomUUID();

	return this.saveOrEditCommand(id, createdAt, updatedAt, attendee, true);

    }

    private Attendee editAttendee(final AttendeeDto attendee) {

	final UUID id = attendee.id();
	final Timestamp createdAt = this.getCreatedAt(id);
	final Timestamp updatedAt = Timestamp.from(Instant.now());

	return this.saveOrEditCommand(id, createdAt, updatedAt, attendee, false);

    }

    private Attendee saveOrEditCommand(final UUID id,
				       final Timestamp createdAt,
				       final Timestamp timestamp,
				       final AttendeeDto attendee,
				       final boolean isSaveOperation) {

	final List<UUID> ticketIds = attendee.ticketIDs() != null ? attendee.ticketIDs() : List.of();
	final UUID[] arrTicketIds = this.convertToArray(ticketIds);

	if (isSaveOperation) {

	    this.jdbcTemplate.update(this.attendeeQueriesProperties.getProperty(CrudQueriesOperations.SAVE.name()), id,
		    createdAt, timestamp, attendee.firstName(), attendee.lastName(), arrTicketIds);

	} else {

	    this.jdbcTemplate.update(this.attendeeQueriesProperties.getProperty(CrudQueriesOperations.EDIT.name()), id,
		    createdAt, timestamp, attendee.firstName(), attendee.lastName(), arrTicketIds, id);
	}

	return this.attendeeDtoToAttendeeConverter
		.apply(new AttendeeDto(id, createdAt, timestamp, attendee.firstName(), attendee.lastName(), ticketIds));

    }

    private UUID[] convertToArray(final List<UUID> ticketIds) {

	if (null == ticketIds) {

	    return new UUID[] {};
	}

	final UUID[] arrIds = new UUID[ticketIds.size()];

	for (int i = 0; i < arrIds.length; i++) {

	    arrIds[i] = ticketIds.get(i);
	}
	return arrIds;

    }

    private Timestamp getCreatedAt(final UUID id) {

	return Timestamp.from(this.findById(id).map(AbstractDomainObject::getCreatedAt).orElse(Instant.now()));

    }

}
