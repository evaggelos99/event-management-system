package io.github.evaggelos99.ems.event.service;

import io.github.evaggelos99.ems.common.api.db.CrudQueriesOperations;
import io.github.evaggelos99.ems.common.api.db.DurationToIntervalConverter;
import io.github.evaggelos99.ems.common.api.domainobjects.AbstractDomainObject;
import io.github.evaggelos99.ems.common.api.domainobjects.EventType;
import io.github.evaggelos99.ems.event.api.Event;
import io.github.evaggelos99.ems.event.api.EventDto;
import io.github.evaggelos99.ems.event.api.converters.EventDtoToEventConverter;
import io.github.evaggelos99.ems.event.api.repo.EventRowMapper;
import io.github.evaggelos99.ems.event.api.repo.IEventRepository;
import io.r2dbc.postgresql.codec.Interval;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;

import static java.util.Objects.requireNonNull;

@Component
public class EventRepository implements IEventRepository {

    private final DatabaseClient databaseClient;
    private final EventRowMapper eventRowMapper;
    private final Function<EventDto, Event> eventDtoToEventConverter;
    private final Map<CrudQueriesOperations, String> eventQueriesProperties;
    private final Function<Duration, Object> durationToIntervalConverter;

    /**
     * C-or
     *
     * @param databaseClient              the {@link DatabaseClient} used for
     *                                    connecting to the database for the Event
     *                                    objects
     * @param eventRowMapperFunction      the {@link EventRowMapper} used for
     *                                    returning Event objects from the database
     * @param eventDtoToEventConverter    the {@link EventDtoToEventConverter} used
     *                                    for converting {@link EventDto} to
     *                                    {@link Event}
     * @param eventQueriesProperties      the {@link Map} which are used for
     *                                    getting the right query CRUD database
     *                                    operations
     * @param durationToIntervalConverter {@link DurationToIntervalConverter} this
     *                                    returns {@link Interval}
     */
    public EventRepository(final DatabaseClient databaseClient,
                           @Qualifier("eventRowMapper") final EventRowMapper eventRowMapperFunction,
                           @Qualifier("eventDtoToEventConverter") final Function<EventDto, Event> eventDtoToEventConverter,
                           @Qualifier("queriesProperties") final Map<CrudQueriesOperations, String> eventQueriesProperties,
                           @Qualifier("durationToIntervalConverter") final Function<Duration, Object> durationToIntervalConverter) {

        this.databaseClient = requireNonNull(databaseClient);
        this.eventRowMapper = requireNonNull(eventRowMapperFunction);
        this.eventDtoToEventConverter = requireNonNull(eventDtoToEventConverter);
        this.eventQueriesProperties = requireNonNull(eventQueriesProperties);
        this.durationToIntervalConverter = requireNonNull(durationToIntervalConverter);
    }

    @Override
    public Mono<Event> save(final EventDto eventDto) {

        return saveEvent(eventDto);
    }

    @Override
    public Mono<Event> findById(final UUID uuid) {

        return databaseClient.sql(eventQueriesProperties.get(CrudQueriesOperations.GET_ID)).bind(0, uuid)
                .map(eventRowMapper).one();
    }

    @Override
    public Mono<Boolean> deleteById(final UUID uuid) {

        return databaseClient.sql(eventQueriesProperties.get(CrudQueriesOperations.DELETE_ID))
                .bind(0, uuid).fetch().rowsUpdated().map(this::rowsAffectedAreMoreThanOne);
    }

    @Override
    public Mono<Boolean> existsById(final UUID uuid) {

        return findById(uuid).map(Objects::nonNull);
    }

    @Override
    public Flux<Event> findAll() {

        return databaseClient.sql(eventQueriesProperties.get(CrudQueriesOperations.GET_ALL))
                .map(eventRowMapper).all();
    }

    @Override
    public Mono<Event> edit(final EventDto eventDto) {

        return editEvent(eventDto);
    }

    private Mono<Event> editEvent(final EventDto event) {

        final UUID uuid = event.uuid();
        final Instant updatedAt = Instant.now();
        final String name = event.name();
        final String place = event.place();
        final EventType eventType = event.eventType();
        final List<UUID> attendeesIds = event.attendeesIds() != null ? event.attendeesIds() : List.of();
        final UUID organizerId = event.organizerId();
        final Integer limitOfPeople = event.limitOfPeople();
        final List<UUID> sponsorIds = event.sponsorsIds() != null ? event.sponsorsIds() : List.of();
        final LocalDateTime startTimeOfEvent = event.startTimeOfEvent();
        final Duration duration = event.duration();
        final Object interval = durationToIntervalConverter.apply(duration);
        final UUID[] attendees = convertToArray(attendeesIds);
        final UUID[] sponsors = convertToArray(sponsorIds);
        final Mono<Long> rowsAffected = databaseClient
                .sql(eventQueriesProperties.get(CrudQueriesOperations.EDIT)).bind(0, uuid)
                .bind(1, updatedAt).bind(2, name).bind(3, place).bind(4, eventType).bind(5, attendees)
                .bind(6, organizerId).bind(7, limitOfPeople).bind(8, sponsors).bind(9, startTimeOfEvent)
                .bind(10, interval).bind(11, uuid).fetch().rowsUpdated();

        return rowsAffected.filter(this::rowsAffectedAreMoreThanOne).flatMap(rowNum -> findById(uuid))
                .map(AbstractDomainObject::getCreatedAt)
                .map(createdAt -> eventDtoToEventConverter.apply(EventDto.builder()
                        .uuid(uuid)
                        .createdAt(createdAt)
                        .lastUpdated(updatedAt)
                        .name(name)
                        .place(place)
                        .eventType(eventType)
                        .attendeesIds(attendeesIds)
                        .organizerId(organizerId)
                        .limitOfPeople(limitOfPeople)
                        .sponsorsIds(sponsorIds)
                        .startTimeOfEvent(startTimeOfEvent)
                        .duration(duration)
                        .build()));
    }

    private Mono<Event> saveEvent(final EventDto event) {

        final UUID eventUuid = event.uuid();
        final Instant now = Instant.now();
        final UUID uuid = eventUuid != null ? eventUuid : UUID.randomUUID();
        final String name = event.name();
        final String place = event.place();
        final EventType eventType = event.eventType();
        final List<UUID> attendeesIds = event.attendeesIds() != null ? event.attendeesIds() : List.of();
        final UUID organizerId = event.organizerId();
        final Integer limitOfPeople = event.limitOfPeople();
        final List<UUID> sponsorIds = event.sponsorsIds() != null ? event.sponsorsIds() : List.of();
        final LocalDateTime startTimeOfEvent = event.startTimeOfEvent();
        final Duration duration = event.duration();
        final Object interval = durationToIntervalConverter.apply(duration);
        final UUID[] attendees = convertToArray(attendeesIds);
        final UUID[] sponsors = convertToArray(sponsorIds);

        final Mono<Long> rowsAffected = databaseClient
                .sql(eventQueriesProperties.get(CrudQueriesOperations.SAVE)).bind(0, uuid).bind(1, now)
                .bind(2, now).bind(3, name).bind(4, place).bind(5, eventType).bind(6, attendees).bind(7, organizerId)
                .bind(8, limitOfPeople).bind(9, sponsors).bind(10, startTimeOfEvent).bind(11, interval).fetch()
                .rowsUpdated();

        return rowsAffected.filter(this::rowsAffectedAreMoreThanOne)
                .map(rowNum -> eventDtoToEventConverter.apply(EventDto.builder()
                        .uuid(uuid)
                        .createdAt(now)
                        .lastUpdated(now)
                        .name(name)
                        .place(place)
                        .eventType(eventType)
                        .attendeesIds(attendeesIds)
                        .organizerId(organizerId)
                        .limitOfPeople(limitOfPeople)
                        .sponsorsIds(sponsorIds)
                        .startTimeOfEvent(startTimeOfEvent)
                        .duration(duration)
                        .build()));
    }

    private UUID[] convertToArray(final List<UUID> ids) {

        if (null == ids) {
            return new UUID[]{};
        }
        final UUID[] uuids = new UUID[ids.size()];

        for (int i = 0; i < uuids.length; i++) {
            uuids[i] = ids.get(i);
        }
        return uuids;
    }

    private boolean rowsAffectedAreMoreThanOne(final Long x) {

        return x >= 1;
    }
}
