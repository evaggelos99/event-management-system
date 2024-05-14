package org.com.ems.db.impl;

import static java.util.Objects.requireNonNull;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;

import org.com.ems.api.domainobjects.Attendee;
import org.com.ems.api.dto.AttendeeDto;
import org.com.ems.db.IAttendeeRepository;
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

    private static final String SAVE = "INSERT INTO attendee (uuid, last_updated, first_name, last_name, ticket_ids) values(?,?,?,?,?)";
    private static final String EDIT = "UPDATE attendee " + "SET uuid = ?, l" + "ast_updated = ?, " + "first_name = ?, "
	    + "last_name = ?, " + "ticket_ids = ? " + "WHERE uuid = ?";
    private static final String GET_ALL = "SELECT * FROM attendee";
    private static final String GET_ID = "SELECT * FROM attendee WHERE uuid = ?";
    private static final String DELETE_STRING = "DELETE FROM attendee WHERE uuid = ?";

    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<Attendee> attendeeRowMapper;
    private final Function<AttendeeDto, Attendee> attendeeDtoToAttendeeConverter;

    public AttendeeRepository(@Autowired final JdbcTemplate jdbcTemplate,
			      @Autowired @Qualifier("attendeeRowMapper") final RowMapper<Attendee> attendeeRowMapper,
			      @Autowired @Qualifier("attendeeDtoToAttendeeConverter") final Function<AttendeeDto,
				      Attendee> attendeeDtoToAttendeeConverter) {

	this.jdbcTemplate = requireNonNull(jdbcTemplate);
	this.attendeeRowMapper = requireNonNull(attendeeRowMapper);
	this.attendeeDtoToAttendeeConverter = requireNonNull(attendeeDtoToAttendeeConverter);

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

	LOGGER.trace("Saved an Attendee: " + attendee);

	return attendee;

    }

    @Override
    public Optional<Attendee> findById(final UUID uuid) {

	try {

	    final Attendee attendee = this.jdbcTemplate.queryForObject(GET_ID, this.attendeeRowMapper, uuid);
	    return Optional.of(attendee);
	} catch (final EmptyResultDataAccessException e) {

	    LOGGER.warn("Attendee with UUID: {} was not found", uuid);
	    return Optional.empty();
	}

    }

    @Override
    public boolean deleteById(final UUID uuid) {

	final int rows = this.jdbcTemplate.update(DELETE_STRING, uuid);

	final boolean deleted = rows == 1 ? true : false;

	if (deleted) {

	    LOGGER.trace("Deleted attendee with uuid: " + uuid);
	} else {

	    LOGGER.trace("Could not delete attendee with uuid: " + uuid);
	}

	return deleted;

    }

    @Override
    public boolean existsById(final UUID uuid) {

	return this.findById(uuid).isPresent();

    }

    @Override
    public Collection<Attendee> findAll() {

	return this.jdbcTemplate.query(GET_ALL, this.attendeeRowMapper);

    }

    private Attendee saveAttendee(final AttendeeDto attendee) {

	final UUID attendeeUuid = attendee.uuid();
	final List<UUID> ticketIds = attendee.ticketIDs();
	final Timestamp timestamp = Timestamp.from(Instant.now());
	final String firstName = attendee.firstName();
	final String lastName = attendee.lastName();
	final UUID[] uuids = ticketIds != null ? this.convertToArray(ticketIds) : new UUID[] {};
	final UUID uuid = attendeeUuid != null ? attendeeUuid : UUID.randomUUID();

	this.jdbcTemplate.update(SAVE, uuid, timestamp, firstName, lastName, uuids);

	return this.attendeeDtoToAttendeeConverter
		.apply(new AttendeeDto(uuid, timestamp, firstName, lastName, ticketIds));

    }

    private Attendee editAttendee(final AttendeeDto attendee) {

	final UUID uuid = attendee.uuid();
	final List<UUID> ticketIds = attendee.ticketIDs();
	final Timestamp timestamp = Timestamp.from(Instant.now());
	final String firstName = attendee.firstName();
	final String lastName = attendee.lastName();
	final UUID[] uuids = ticketIds != null ? this.convertToArray(ticketIds) : new UUID[] {};

	this.jdbcTemplate.update(EDIT, uuid, timestamp, firstName, lastName, uuids, uuid);

	return this.attendeeDtoToAttendeeConverter
		.apply(new AttendeeDto(uuid, timestamp, firstName, lastName, ticketIds));

    }

    private UUID[] convertToArray(final List<UUID> ticketIds) {

	final UUID[] uuids = new UUID[ticketIds.size()];

	for (int i = 0; i < uuids.length; i++) {

	    uuids[i] = ticketIds.get(i);
	}
	return uuids;

    }

}
