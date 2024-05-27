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

	final boolean deleted = rows == 1;

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

    private Event saveEvent(final EventDto event) {

	final UUID eventUuid = event.uuid();

	final Instant now = Instant.now();
	final Timestamp createdAt = Timestamp.from(now);
	final Timestamp updatedAt = Timestamp.from(now);

	final UUID uuid = eventUuid != null ? eventUuid : UUID.randomUUID();

	return this.saveOrEditCommand(uuid, createdAt, updatedAt, event, true);

    }

    private Event editEvent(final EventDto event) {

	final UUID uuid = event.uuid();

	final Timestamp createdAt = this.getCreatedAt(uuid);
	final Timestamp updatedAt = Timestamp.from(Instant.now());

	return this.saveOrEditCommand(uuid, createdAt, updatedAt, event, false);

    }

    private Event saveOrEditCommand(final UUID uuid,
				    final Timestamp createdAt,
				    final Timestamp updatedAt,
				    final EventDto event,
				    final boolean isSaveOperation) {

	final String name = event.denomination();
	final String place = event.place();
	final EventType eventType = event.eventType();
	final List<UUID> attendeesIds = event.attendeesIds() != null ? event.attendeesIds() : List.of();
	final UUID organizerId = event.organizerId();
	final Integer limitOfPeople = event.limitOfPeople();
	final List<UUID> sponsorIds = event.sponsorsIds() != null ? event.sponsorsIds() : List.of();
	final LocalDateTime startTimeOfEvent = event.startTimeOfEvent();
	final Duration duration = event.duration();
	final Object interval = this.durationToIntervalConverter.apply(duration);

	final UUID[] attendees = this.convertToArray(attendeesIds);
	final UUID[] sponsors = this.convertToArray(sponsorIds);

	if (isSaveOperation) {

	    this.jdbcTemplate.update(this.eventQueriesProperties.getProperty(CrudQueriesOperations.SAVE.name()), uuid,
		    createdAt, updatedAt, name, place, eventType.name(), attendees, organizerId, limitOfPeople,
		    sponsors, startTimeOfEvent, interval);

	} else {

	    this.jdbcTemplate.update(this.eventQueriesProperties.getProperty(CrudQueriesOperations.EDIT.name()), uuid,
		    createdAt, updatedAt, name, place, eventType.name(), attendees, organizerId, limitOfPeople,
		    sponsors, startTimeOfEvent, interval, uuid);
	}

	return this.eventDtoToEventConverter.apply(new EventDto(uuid, createdAt, updatedAt, name, place, eventType,
		attendeesIds, organizerId, limitOfPeople, sponsorIds, startTimeOfEvent, duration));

    }

    private UUID[] convertToArray(final List<UUID> ids) {

	if (null == ids) {

	    return new UUID[] {};
	}

	final UUID[] uuids = new UUID[ids.size()];

	for (int i = 0; i < uuids.length; i++) {

	    uuids[i] = ids.get(i);
	}
	return uuids;

    }

    private Timestamp getCreatedAt(final UUID uuid) {

	return Timestamp.from(this.findById(uuid).map(AbstractDomainObject::getCreatedAt).orElse(Instant.now()));

    }
}
