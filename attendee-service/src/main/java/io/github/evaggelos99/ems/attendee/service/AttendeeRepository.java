package io.github.evaggelos99.ems.attendee.service;

import io.github.evaggelos99.ems.attendee.api.Attendee;
import io.github.evaggelos99.ems.attendee.api.AttendeeDto;
import io.github.evaggelos99.ems.attendee.api.converters.AttendeeDtoToAttendeeConverter;
import io.github.evaggelos99.ems.attendee.api.repo.AttendeeRowMapper;
import io.github.evaggelos99.ems.attendee.api.repo.IAttendeeRepository;
import io.github.evaggelos99.ems.common.api.db.CrudQueriesOperations;
import io.github.evaggelos99.ems.common.api.domainobjects.AbstractDomainObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.*;
import java.util.function.Function;

import static java.util.Objects.requireNonNull;

@Component
public class AttendeeRepository implements IAttendeeRepository {

    private final DatabaseClient databaseClient;
    private final AttendeeRowMapper attendeeRowMapper;
    private final Function<AttendeeDto, Attendee> attendeeDtoToAttendeeConverter;
    private final Map<CrudQueriesOperations, String> attendeeQueriesProperties;

    /**
     * C-or
     *
     * @param databaseClient                 the {@link DatabaseClient} used for
     *                                       connecting to the database for the
     *                                       Attendee objects
     * @param attendeeRowMapper              the {@link AttendeeRowMapper} used for
     *                                       returning Attendee objects from the
     *                                       database
     * @param attendeeDtoToAttendeeConverter the
     *                                       {@link AttendeeDtoToAttendeeConverter}
     *                                       used for converting {@link AttendeeDto}
     *                                       to {@link Attendee}
     * @param attendeeQueriesProperties      the {@link Map} which are used
     *                                       for getting the right query CRUD
     *                                       database operations
     */
    public AttendeeRepository(final DatabaseClient databaseClient,
                              @Qualifier("attendeeRowMapper") final AttendeeRowMapper attendeeRowMapper,
                              @Qualifier("attendeeDtoToAttendeeConverter") final Function<AttendeeDto, Attendee> attendeeDtoToAttendeeConverter,
                              @Qualifier("queriesProperties") final Map<CrudQueriesOperations, String> attendeeQueriesProperties) {

        this.databaseClient = requireNonNull(databaseClient);
        this.attendeeRowMapper = requireNonNull(attendeeRowMapper);
        this.attendeeDtoToAttendeeConverter = requireNonNull(attendeeDtoToAttendeeConverter);
        this.attendeeQueriesProperties = requireNonNull(attendeeQueriesProperties);
    }

    @Override
    public Mono<Attendee> save(final AttendeeDto attendeeDto) {

        return saveAttendee(attendeeDto);
    }

    @Override
    public Mono<Attendee> findById(final UUID uuid) {

        return databaseClient.sql(attendeeQueriesProperties.get(CrudQueriesOperations.GET_ID))
                .bind(0, uuid).map(attendeeRowMapper).one();
    }

    @Override
    public Mono<Boolean> deleteById(final UUID uuid) {

        return databaseClient.sql(attendeeQueriesProperties.get(CrudQueriesOperations.DELETE_ID))
                .bind(0, uuid).fetch().rowsUpdated().map(this::rowsAffectedAreMoreThanOne);
    }

    @Override
    public Mono<Boolean> existsById(final UUID uuid) {

        return findById(uuid).map(Objects::nonNull);
    }

    @Override
    public Flux<Attendee> findAll() {

        return databaseClient.sql(attendeeQueriesProperties.get(CrudQueriesOperations.GET_ALL))
                .map(attendeeRowMapper).all();
    }

    @Override
    public Mono<Attendee> edit(final AttendeeDto attendeeDto) {

        return editAttendee(attendeeDto);
    }

    private Mono<Attendee> editAttendee(final AttendeeDto attendee) {

        final UUID uuid = attendee.uuid();
        final OffsetDateTime updatedAt = OffsetDateTime.now();
        final List<UUID> ticketIds = attendee.ticketIDs() != null ? attendee.ticketIDs() : List.of();
        final UUID[] uuids = convertToArray(ticketIds);
        final Mono<Long> rowsAffected = databaseClient
                .sql(attendeeQueriesProperties.get(CrudQueriesOperations.EDIT)).bind(0, uuid)
                .bind(1, updatedAt).bind(2, attendee.firstName()).bind(3, attendee.lastName()).bind(4, uuids)
                .bind(5, uuid).fetch().rowsUpdated();
        return rowsAffected.filter(this::rowsAffectedAreMoreThanOne).flatMap(rowNum -> findById(uuid))
                .map(AbstractDomainObject::getCreatedAt)
                .map(createdAt -> attendeeDtoToAttendeeConverter.apply(
                        AttendeeDto.builder()
                                .uuid(uuid)
                                .createdAt(createdAt)
                                .lastUpdated(updatedAt)
                                .firstName(attendee.firstName())
                                .lastName(attendee.lastName())
                                .ticketIDs(ticketIds)
                                .build()));
    }

    private Mono<Attendee> saveAttendee(final AttendeeDto attendee) {

        final UUID attendeeId = attendee.uuid();
        final OffsetDateTime instantNow = OffsetDateTime.now();
        final UUID uuid = attendeeId != null ? attendeeId : UUID.randomUUID();
        final List<UUID> ticketIds = attendee.ticketIDs() != null ? attendee.ticketIDs() : List.of();
        final UUID[] uuids = convertToArray(ticketIds);
        final Mono<Long> rowsAffected = databaseClient
                .sql(attendeeQueriesProperties.get(CrudQueriesOperations.SAVE)).bind(0, uuid)
                .bind(1, instantNow).bind(2, instantNow).bind(3, attendee.firstName()).bind(4, attendee.lastName())
                .bind(5, uuids).fetch().rowsUpdated();
        return rowsAffected.filter(this::rowsAffectedAreMoreThanOne).map(rowNum -> attendeeDtoToAttendeeConverter.apply(
                AttendeeDto.builder().uuid(uuid)
                        .createdAt(instantNow)
                        .lastUpdated(instantNow)
                        .firstName(attendee.firstName())
                        .lastName(attendee.lastName())
                        .ticketIDs(ticketIds)
                        .build()));
    }

    private UUID[] convertToArray(final List<UUID> ticketIds) {

        if (null == ticketIds) {
            return new UUID[]{};
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
