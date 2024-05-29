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
    public Optional<Event> findById(final UUID id) {

	try {

	    final Event event = this.jdbcTemplate.queryForObject(
		    this.eventQueriesProperties.getProperty(CrudQueriesOperations.GET_ID.name()), this.eventRowMapper,
		    id);
	    return Optional.of(event);
	} catch (final EmptyResultDataAccessException e) {

	    LOGGER.warn("Event with UUID: {} was not found", id);
	    return Optional.empty();
	}

    }

    @Override
    public boolean deleteById(final UUID id) {

	final int rows = this.jdbcTemplate
		.update(this.eventQueriesProperties.getProperty(CrudQueriesOperations.DELETE_ID.name()), id);

	final boolean deleted = rows == 1;

	if (deleted) {

	    LOGGER.trace("Deleted an Event with id: " + id);
	} else {

	    LOGGER.trace("Could not delete Event with id: " + id);
	}

	return deleted;

    }

    @Override
    public boolean existsById(final UUID id) {

	return this.findById(id).isPresent();

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

	final UUID eventUuid = event.id();

	final Instant now = Instant.now();
	final Timestamp createdAt = Timestamp.from(now);
	final Timestamp updatedAt = Timestamp.from(now);

	final UUID id = eventUuid != null ? eventUuid : UUID.randomUUID();

	return this.saveOrEditCommand(id, createdAt, updatedAt, event, true);

    }

    private Event editEvent(final EventDto event) {

	final UUID id = event.id();

	final Timestamp createdAt = this.getCreatedAt(id);
	final Timestamp updatedAt = Timestamp.from(Instant.now());

	return this.saveOrEditCommand(id, createdAt, updatedAt, event, false);

    }

    private Event saveOrEditCommand(final UUID id,
				    final Timestamp createdAt,
				    final Timestamp updatedAt,
				    final EventDto event,
				    final boolean isSaveOperation) {

	final String name = event.name();
	final String place = event.place();
	final EventType eventType = event.eventType();
	final List<UUID> attendeesIds = event.attendeesIds() != null ? event.attendeesIds() : List.of();
	final UUID organizerId = event.organizerId();
	final Integer limitOfPeople = event.limitOfPeople();
	final List<UUID> sponsorIds = event.sponsorsIds() != null ? event.sponsorsIds() : List.of();
	final LocalDateTime startTimeOfEvent = event.startTimeOfEvent();
	final Duration duration = event.duration();
	final Object interval = this.durationToIntervalConverter.apply(duration);

	final UUID[] arrAttendeesIds = this.convertToArray(attendeesIds);
	final UUID[] arrSponsorIds = this.convertToArray(sponsorIds);

	if (isSaveOperation) {

	    this.jdbcTemplate.update(this.eventQueriesProperties.getProperty(CrudQueriesOperations.SAVE.name()), id,
		    createdAt, updatedAt, name, place, eventType.name(), arrAttendeesIds, organizerId, limitOfPeople,
		    arrSponsorIds, startTimeOfEvent, interval);

	} else {

	    this.jdbcTemplate.update(this.eventQueriesProperties.getProperty(CrudQueriesOperations.EDIT.name()), id,
		    createdAt, updatedAt, name, place, eventType.name(), arrAttendeesIds, organizerId, limitOfPeople,
		    arrSponsorIds, startTimeOfEvent, interval, id);
	}

	return this.eventDtoToEventConverter.apply(new EventDto(id, createdAt, updatedAt, name, place, eventType,
		attendeesIds, organizerId, limitOfPeople, sponsorIds, startTimeOfEvent, duration));

    }

    private UUID[] convertToArray(final List<UUID> ids) {

	if (null == ids) {

	    return new UUID[] {};
	}

	final UUID[] arrIds = new UUID[ids.size()];

	for (int i = 0; i < arrIds.length; i++) {

	    arrIds[i] = ids.get(i);
	}
	return arrIds;

    }

    private Timestamp getCreatedAt(final UUID id) {

	return Timestamp.from(this.findById(id).map(AbstractDomainObject::getCreatedAt).orElse(Instant.now()));

    }
}
