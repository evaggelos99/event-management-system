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

import org.com.ems.api.converters.EventDtoToEventConverter;
import org.com.ems.api.domainobjects.AbstractDomainObject;
import org.com.ems.api.domainobjects.Event;
import org.com.ems.api.domainobjects.EventType;
import org.com.ems.api.dto.EventDto;
import org.com.ems.db.IEventRepository;
import org.com.ems.db.queries.Queries.CrudQueriesOperations;
import org.com.ems.db.rowmappers.EventRowMapper;
import org.com.ems.db.rowmappers.util.DurationToIntervalConverter;
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
    private final Function<Duration, Object> durationToIntervalConverter;

    /**
     * C-or
     *
     * @param jdbcTemplate                the {@link JdbcTemplate} used for
     *                                    connecting to the database for the Event
     *                                    objects
     * @param eventRowMapperFunction      the {@link EventRowMapper} used for
     *                                    returning Event objects from the database
     * @param eventDtoToEventConverter    the {@link EventDtoToEventConverter} used
     *                                    for converting {@link EventDto} to
     *                                    {@link Event}
     * @param eventQueriesProperties      the {@link Properties} which are used for
     *                                    getting the right query CRUD database
     *                                    operations
     * @param durationToIntervalConverter {@link DurationToIntervalConverter}
     */
    public EventRepository(@Autowired final JdbcTemplate jdbcTemplate,
			   @Autowired @Qualifier("eventRowMapper") final RowMapper<Event> eventRowMapperFunction,
			   @Autowired @Qualifier("eventDtoToEventConverter") final Function<EventDto,
				   Event> eventDtoToEventConverter,
			   @Autowired @Qualifier("eventQueriesProperties") final Properties eventQueriesProperties,
			   @Autowired @Qualifier("durationToIntervalConverter") final Function<Duration,
				   Object> durationToIntervalConverter) {

	this.jdbcTemplate = requireNonNull(jdbcTemplate);
	this.eventRowMapper = requireNonNull(eventRowMapperFunction);
	this.eventDtoToEventConverter = requireNonNull(eventDtoToEventConverter);
	this.eventQueriesProperties = requireNonNull(eventQueriesProperties);
	this.durationToIntervalConverter = requireNonNull(durationToIntervalConverter);

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

	    LOGGER.trace("Deleted an Event with uuid: " + uuid);
	} else {

	    LOGGER.trace("Could not delete Event with uuid: " + uuid);
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

	final Event event = this.editEvent(eventDto);

	LOGGER.trace("Edited an Event: " + event);
	return event;

    }

    private Event editEvent(final EventDto dto) {

	final UUID uuid = dto.uuid();

	final Timestamp createdAt = Timestamp.from(this.getEvent(uuid).getCreatedAt());
	final Timestamp timestamp = Timestamp.from(Instant.now());
	final String name = dto.denomination();
	final String place = dto.place();
	final EventType eventType = dto.eventType();
	final List<UUID> attendeesIDs = dto.attendeesIds() != null ? dto.attendeesIds() : List.of();
	final UUID organizerId = dto.organizerId();
	final Integer limitOfPeople = dto.limitOfPeople();
	final UUID sponsorId = dto.sponsorId();
	final LocalDateTime startTimeOfEvent = dto.startTimeOfEvent();
	final Duration duration = dto.duration();

	final Object interval = this.durationToIntervalConverter.apply(duration);

	final UUID[] uuidsOfAttendees = this.convertToArray(attendeesIDs);

	this.jdbcTemplate.update(this.eventQueriesProperties.getProperty(CrudQueriesOperations.EDIT.name()), uuid,
		createdAt, timestamp, name, place, eventType.name(), uuidsOfAttendees, organizerId, limitOfPeople,
		sponsorId, startTimeOfEvent, interval, uuid);

	return this.eventDtoToEventConverter.apply(new EventDto(uuid, createdAt, timestamp, name, place, eventType,
		attendeesIDs, organizerId, limitOfPeople, sponsorId, startTimeOfEvent, duration));

    }

    private Event saveEvent(final EventDto dto) {

	final UUID eventUuid = dto.uuid();
	final Timestamp createdAt = Timestamp.from(Instant.now());
	final Timestamp timestamp = Timestamp.from(Instant.now());
	final String name = dto.denomination();
	final String place = dto.place();
	final EventType eventType = dto.eventType();
	final List<UUID> attendeesIDs = dto.attendeesIds() != null ? dto.attendeesIds() : List.of();
	final UUID organizerId = dto.organizerId();
	final Integer limitOfPeople = dto.limitOfPeople();
	final UUID sponsorId = dto.sponsorId();
	final LocalDateTime startTimeOfEvent = dto.startTimeOfEvent();
	final Duration duration = dto.duration();
	final Object interval = this.durationToIntervalConverter.apply(duration);

	final UUID[] uuidsOfAttendees = this.convertToArray(attendeesIDs);
	final UUID uuid = eventUuid != null ? eventUuid : UUID.randomUUID();

	this.jdbcTemplate.update(this.eventQueriesProperties.getProperty(CrudQueriesOperations.SAVE.name()), uuid,
		createdAt, timestamp, name, place, eventType.name(), uuidsOfAttendees, organizerId, limitOfPeople,
		sponsorId, startTimeOfEvent, interval);

	return this.eventDtoToEventConverter.apply(new EventDto(uuid, createdAt, timestamp, name, place, eventType,
		attendeesIDs, organizerId, limitOfPeople, sponsorId, startTimeOfEvent, duration));

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

    private AbstractDomainObject getEvent(final UUID uuid) {

	return this.findById(uuid).get();

    }

}
