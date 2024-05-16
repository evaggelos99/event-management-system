package org.com.ems.db.impl;

import static java.util.Objects.requireNonNull;

import java.sql.Timestamp;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Properties;
import java.util.UUID;
import java.util.function.Function;

import org.com.ems.api.domainobjects.Event;
import org.com.ems.api.domainobjects.EventType;
import org.com.ems.api.dto.EventDto;
import org.com.ems.db.IEventRepository;
import org.com.ems.db.queries.Queries.CrudQueriesOperations;
import org.postgresql.util.PGInterval;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

@Component
public class EventRepository implements IEventRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(EventRepository.class);

    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<Event> eventRowMapper;
    private final Function<EventDto, Event> eventDtoToEventConverter;
    private final Properties eventQueriesProperties;

    public EventRepository(@Autowired final JdbcTemplate jdbcTemplate,
			   @Autowired @Qualifier("eventRowMapper") final RowMapper<Event> eventRowMapperFunction,
			   @Autowired @Qualifier("eventDtoToEventConverter") final Function<EventDto,
				   Event> eventDtoToEventConverter,
			   @Autowired @Qualifier("eventQueriesProperties") final Properties eventQueriesProperties) {

	this.jdbcTemplate = requireNonNull(jdbcTemplate);
	this.eventRowMapper = requireNonNull(eventRowMapperFunction);
	this.eventDtoToEventConverter = requireNonNull(eventDtoToEventConverter);
	this.eventQueriesProperties = requireNonNull(eventQueriesProperties);

    }

    @Override
    public Event save(final EventDto t) {

	final Event event = this.saveEvent(t);

	LOGGER.trace("Saved an Event: " + event);

	return event;

    }

    @Override
    public Optional<Event> findById(final UUID uuid) {

	try {

	    final Event event = this.jdbcTemplate.queryForObject(
		    this.eventQueriesProperties.getProperty(CrudQueriesOperations.GET_ID.name()), this.eventRowMapper,
		    uuid);
	    return Optional.of(event);
	} catch (final EmptyResultDataAccessException e) {

	    LOGGER.warn("Event with UUID: {} was not found", uuid);
	    return Optional.empty();
	}

    }

    @Override
    public boolean deleteById(final UUID uuid) {

	final int rows = this.jdbcTemplate
		.update(this.eventQueriesProperties.getProperty(CrudQueriesOperations.DELETE_ID.name()), uuid);

	final boolean deleted = rows == 1 ? true : false;

	if (deleted) {

	    LOGGER.trace("Deleted an event with uuid: " + uuid);
	} else {

	    LOGGER.trace("Could not delete event with uuid: " + uuid);
	}

	return deleted;

    }

    @Override
    public boolean existsById(final UUID uuid) {

	return this.findById(uuid).isPresent();

    }

    @Override
    public Collection<Event> findAll() {

	return this.jdbcTemplate.query(this.eventQueriesProperties.getProperty(CrudQueriesOperations.GET_ALL.name()),
		this.eventRowMapper);

    }

    @Override
    public Event edit(final EventDto eventDto) {

	final Event event = this.edit(eventDto);

	LOGGER.trace("Saved an Event: " + event);
	return event;

    }

    private Event saveEvent(final EventDto dto) {

	final UUID eventUuid = dto.uuid();
	final Timestamp timestamp = Timestamp.from(Instant.now());
	final String name = dto.name();
	final String place = dto.place();
	final EventType eventType = dto.eventType();
	final List<UUID> attendeesIDs = dto.attendeesIds();
	final UUID organizerIds = dto.organizerId();
	final Integer limitOfPeople = dto.limitOfPeople();
	final UUID sponsorId = dto.sponsorId();
	final LocalDateTime startTimeOfEvent = dto.startTimeOfEvent();
	final Duration duration = dto.duration();
	final PGInterval interval = this.convertInterval(duration);

	final UUID[] uuidsOfAttendees = attendeesIDs != null ? this.convertToArray(attendeesIDs) : new UUID[] {};
	final UUID uuid = eventUuid != null ? eventUuid : UUID.randomUUID();

	this.jdbcTemplate.update(this.eventQueriesProperties.getProperty(CrudQueriesOperations.SAVE.name()), uuid,
		timestamp, name, place, eventType.name(), uuidsOfAttendees, organizerIds, limitOfPeople, sponsorId,
		startTimeOfEvent, interval);

	return this.eventDtoToEventConverter.apply(new EventDto(uuid, timestamp, name, place, eventType, attendeesIDs,
		organizerIds, limitOfPeople, sponsorId, startTimeOfEvent, duration));

    }

    private PGInterval convertInterval(final Duration duration) {

	return new PGInterval(0, 0, 0, (int) duration.toHours(), (int) duration.toMinutes(), duration.getSeconds());

    }

    private UUID[] convertToArray(final List<UUID> ticketIds) {

	final UUID[] uuids = new UUID[ticketIds.size()];

	for (int i = 0; i < uuids.length; i++) {

	    uuids[i] = ticketIds.get(i);
	}
	return uuids;

    }

}
