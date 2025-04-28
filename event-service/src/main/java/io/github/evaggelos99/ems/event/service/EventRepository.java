package io.github.evaggelos99.ems.event.service;

import io.github.evaggelos99.ems.common.api.db.CrudQueriesOperations;
import io.github.evaggelos99.ems.common.api.db.DurationToIntervalConverter;
import io.github.evaggelos99.ems.common.api.domainobjects.AbstractDomainObject;
import io.github.evaggelos99.ems.common.api.domainobjects.EventType;
import io.github.evaggelos99.ems.common.api.transport.EventStreamPayload;
import io.github.evaggelos99.ems.event.api.Event;
import io.github.evaggelos99.ems.event.api.EventDto;
import io.github.evaggelos99.ems.event.api.EventStream;
import io.github.evaggelos99.ems.event.api.converters.EventDtoToEventConverter;
import io.github.evaggelos99.ems.event.api.converters.EventStreamPayloadToEventStreamEntityConverter;
import io.github.evaggelos99.ems.event.api.repo.EventRowMapper;
import io.github.evaggelos99.ems.event.api.repo.EventStreamQueriesOperations;
import io.github.evaggelos99.ems.event.api.repo.EventStreamRowMapper;
import io.github.evaggelos99.ems.event.api.repo.IEventRepository;
import io.r2dbc.postgresql.codec.Interval;
import io.r2dbc.postgresql.codec.Json;
import io.r2dbc.spi.Connection;
import io.r2dbc.spi.Statement;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.*;
import java.util.function.Function;

import static io.github.evaggelos99.ems.event.api.repo.EventStreamQueriesOperations.GET_STREAM;
import static java.util.Objects.requireNonNull;

@Component
public class EventRepository implements IEventRepository {

    private final DatabaseClient databaseClient;
    private final EventRowMapper eventRowMapper;
    private final Function<EventDto, Event> eventDtoToEventConverter;
    private final Map<CrudQueriesOperations, String> eventQueriesProperties;
    private final Function<Duration, Object> durationToIntervalConverter;
    private final Map<EventStreamQueriesOperations, String> eventStreamQueriesOperations;
    private final EventStreamPayloadToEventStreamEntityConverter eventPayloadToEventStreamConverter;
    private final EventStreamRowMapper eventStreamRowMapper;

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
                           @Qualifier("durationToIntervalConverter") final Function<Duration, Object> durationToIntervalConverter,
                           @Qualifier("saveEventStreamQueriesProperties") final Map<EventStreamQueriesOperations, String> eventStreamQueriesOperations,
                           final EventStreamPayloadToEventStreamEntityConverter eventPayloadToEventStreamConverter,
                           @Qualifier("eventStreamRowMapper") final EventStreamRowMapper eventStreamRowMapper) {

        this.databaseClient = requireNonNull(databaseClient);
        this.eventRowMapper = requireNonNull(eventRowMapperFunction);
        this.eventDtoToEventConverter = requireNonNull(eventDtoToEventConverter);
        this.eventQueriesProperties = requireNonNull(eventQueriesProperties);
        this.durationToIntervalConverter = requireNonNull(durationToIntervalConverter);
        this.eventStreamQueriesOperations = requireNonNull(eventStreamQueriesOperations);
        this.eventPayloadToEventStreamConverter = eventPayloadToEventStreamConverter;
        this.eventStreamRowMapper=eventStreamRowMapper;
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

    @Override
    public Mono<EventStream> saveOneEventStreamPayload(final EventStreamPayload payload) {
        return addEventStream(payload);
    }


    @Override
    public Flux<EventStream> saveMultipleEventStreamPayload(final List<EventStreamPayload> payload) {
        return addEventStreams(payload);
    }

    @Override
    public Flux<EventStream> findAllEventStreams(final UUID eventId) {
        return databaseClient.sql(eventStreamQueriesOperations.get(GET_STREAM)).bind(0,eventId).map(eventStreamRowMapper).all();
    }

    private Mono<Event> editEvent(final EventDto event) {

        final UUID uuid = event.uuid();
        final OffsetDateTime updatedAt = OffsetDateTime.now();
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
        final boolean streamable = event.streamable();

        final Mono<Long> rowsAffected = databaseClient
                .sql(eventQueriesProperties.get(CrudQueriesOperations.EDIT)).bind(0, uuid)
                .bind(1, updatedAt).bind(2, name).bind(3, place).bind(4, eventType).bind(5, attendees)
                .bind(6, organizerId).bind(7, limitOfPeople).bind(8, sponsors).bind(9, startTimeOfEvent)
                .bind(10, interval).bind(11, streamable).bind(12, uuid).fetch().rowsUpdated();

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
                        .streamable(streamable)
                        .sponsorsIds(sponsorIds)
                        .startTimeOfEvent(startTimeOfEvent)
                        .duration(duration)
                        .build()));
    }

    private Mono<Event> saveEvent(final EventDto event) {

        final UUID eventUuid = event.uuid();
        final OffsetDateTime now = OffsetDateTime.now();
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
        final boolean streamable = event.streamable();

        final Mono<Long> rowsAffected = databaseClient
                .sql(eventQueriesProperties.get(CrudQueriesOperations.SAVE)).bind(0, uuid).bind(1, now)
                .bind(2, now).bind(3, name).bind(4, place).bind(5, eventType).bind(6, attendees).bind(7, organizerId)
                .bind(8, limitOfPeople).bind(9, sponsors).bind(10, startTimeOfEvent).bind(11, interval).bind(12, streamable).fetch()
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
                        .streamable(streamable)
                        .startTimeOfEvent(startTimeOfEvent)
                        .duration(duration)
                        .build()));
    }

    private Mono<EventStream> addEventStream(final EventStreamPayload payload) {

        final OffsetDateTime createdAt = OffsetDateTime.now();

        final Mono<Long> rowsAffected = databaseClient.sql(eventStreamQueriesOperations.get(EventStreamQueriesOperations.ADD))
                .bind(0, payload.getUuid())
                .bind(1, createdAt)
                .bind(2, createdAt)
                .bind(3, payload.getStreamType())
                .bind(4, payload.getTime())
                .bind(5, payload.getMessageType())
                .bind(6, payload.getContent())
                .bind(7, payload.getLanguage())
                .bind(8, payload.getImportant())
                .bind(9, Json.of(payload.getMetadata()))
                .fetch()
                .rowsUpdated();

        return rowsAffected.filter(this::rowsAffectedAreMoreThanOne)
                .map(num -> eventPayloadToEventStreamConverter.convert(payload, createdAt));
    }

    private Flux<EventStream> addEventStreams(final List<EventStreamPayload> payloads) {
        return databaseClient.inConnectionMany(conn -> batchEventStreamInsertion(payloads, conn));
    }

    private Flux<EventStream> batchEventStreamInsertion(final List<EventStreamPayload> payloads, final Connection conn) {

        if (payloads.isEmpty()) {
            return Flux.empty();
        }
        final Statement statement = conn.createStatement(eventStreamQueriesOperations.get(EventStreamQueriesOperations.ADD));


        for (int i = 0; i< payloads.size()-1; i++) {

            final EventStreamPayload payload = payloads.get(i);

            final OffsetDateTime createdAt = OffsetDateTime.now();

            statement.bind(0, payload.getUuid())
                    .bind(1, createdAt)
                    .bind(2, createdAt)
                    .bind(3, payload.getStreamType())
                    .bind(4, payload.getTime())
                    .bind(5, payload.getMessageType())
                    .bind(6, payload.getContent())
                    .bind(7, payload.getLanguage())
                    .bind(8, payload.getImportant())
                    .bind(9, Json.of(payload.getMetadata())).add();
        }

        final EventStreamPayload payload = payloads.get(payloads.size()-1);
        final OffsetDateTime createdAt = OffsetDateTime.now();
        statement.bind(0, payload.getUuid())
                .bind(1, createdAt)
                .bind(2, createdAt)
                .bind(3, payload.getStreamType())
                .bind(4, payload.getTime())
                .bind(5, payload.getMessageType())
                .bind(6, payload.getContent())
                .bind(7, payload.getLanguage())
                .bind(8, payload.getImportant())
                .bind(9, Json.of(payload.getMetadata()));

        return Flux.from(statement.execute())
                .flatMap(result -> result.map(eventStreamRowMapper));
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
