package io.github.evaggelos99.ems.organizer.service;

import io.github.evaggelos99.ems.common.api.db.CrudQueriesOperations;
import io.github.evaggelos99.ems.common.api.domainobjects.AbstractDomainObject;
import io.github.evaggelos99.ems.common.api.domainobjects.ContactInformation;
import io.github.evaggelos99.ems.common.api.domainobjects.EventType;
import io.github.evaggelos99.ems.organizer.api.Organizer;
import io.github.evaggelos99.ems.organizer.api.OrganizerDto;
import io.github.evaggelos99.ems.organizer.api.converters.OrganizerDtoToOrganizerConverter;
import io.github.evaggelos99.ems.organizer.api.repo.IOrganizerRepository;
import io.github.evaggelos99.ems.organizer.api.repo.OrganizerRowMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.*;
import java.util.function.Function;

import static java.util.Objects.requireNonNull;

@Component
public class OrganizerRepository implements IOrganizerRepository {

    private final DatabaseClient databaseClient;
    private final OrganizerRowMapper organizerRowMapper;
    private final Function<OrganizerDto, Organizer> organizerDtoToOrganizerConverter;
    private final Map<CrudQueriesOperations, String> organizerQueriesProperties;

    /**
     * C-or
     *
     * @param databaseClient                   the {@link DatabaseClient} used for
     *                                         connecting to the database for the
     *                                         Organizer objects
     * @param organizerRowMapper               the {@link OrganizerRowMapper} used
     *                                         for returning Organizer objects from
     *                                         the database
     * @param organizerDtoToOrganizerConverter the
     *                                         {@link OrganizerDtoToOrganizerConverter}
     *                                         used for converting
     *                                         {@link OrganizerDto} to
     *                                         {@link Organizer}
     * @param organizerQueriesProperties       the {@link Map} which are used
     *                                         for getting the right query CRUD
     *                                         database operations
     */
    public OrganizerRepository(final DatabaseClient databaseClient,
                               @Qualifier("organizerRowMapper") final OrganizerRowMapper organizerRowMapper,
                               @Qualifier("organizerDtoToOrganizerConverter") final Function<OrganizerDto, Organizer> organizerDtoToOrganizerConverter,
                               @Qualifier("queriesProperties") final Map<CrudQueriesOperations, String> organizerQueriesProperties) {

        this.databaseClient = requireNonNull(databaseClient);
        this.organizerRowMapper = requireNonNull(organizerRowMapper);
        this.organizerDtoToOrganizerConverter = requireNonNull(organizerDtoToOrganizerConverter);
        this.organizerQueriesProperties = requireNonNull(organizerQueriesProperties);
    }

    @Override
    public Mono<Organizer> save(final OrganizerDto organizerDto) {

        return saveOrganizer(organizerDto);
    }

    @Override
    public Mono<Organizer> findById(final UUID uuid) {

        return databaseClient.sql(organizerQueriesProperties.get(CrudQueriesOperations.GET_ID))
                .bind(0, uuid).map(organizerRowMapper).one();
    }

    @Override
    public Mono<Boolean> deleteById(final UUID uuid) {

        return databaseClient.sql(organizerQueriesProperties.get(CrudQueriesOperations.DELETE_ID))
                .bind(0, uuid).fetch().rowsUpdated().map(this::rowsAffectedAreMoreThanOne);
    }

    @Override
    public Mono<Boolean> existsById(final UUID uuid) {

        return findById(uuid).map(Objects::nonNull);
    }

    @Override
    public Flux<Organizer> findAll() {

        return databaseClient.sql(organizerQueriesProperties.get(CrudQueriesOperations.GET_ALL))
                .map(organizerRowMapper).all();
    }

    @Override
    public Mono<Organizer> edit(final OrganizerDto organizerDto) {

        return editOrganizer(organizerDto);
    }

    private Mono<Organizer> editOrganizer(final OrganizerDto organizer) {

        final UUID uuid = organizer.uuid();
        final Instant updatedAt = Instant.now();

        final String name = organizer.name();
        final String website = organizer.website();
        final String information = organizer.information();
        final List<EventType> listOfEventTypes = organizer.eventTypes();
        final ContactInformation contactInformation = organizer.contactInformation();
        final EventType[] eventTypesArray = convertToArray(listOfEventTypes);

        final Mono<Long> rowsAffected = databaseClient
                .sql(organizerQueriesProperties.get(CrudQueriesOperations.EDIT)).bind(0, uuid)
                .bind(1, updatedAt).bind(2, name).bind(3, website).bind(4, information).bind(5, eventTypesArray)
                .bind(6, contactInformation.email()).bind(7, contactInformation.phoneNumber())
                .bind(8, contactInformation.physicalAddress()).bind(9, uuid).fetch().rowsUpdated();

        return rowsAffected.filter(this::rowsAffectedAreMoreThanOne).flatMap(rowNum -> findById(uuid))
                .map(AbstractDomainObject::getCreatedAt)
                .map(createdAt -> organizerDtoToOrganizerConverter.apply(
                        OrganizerDto.builder()
                                .uuid(uuid)
                                .createdAt(createdAt)
                                .lastUpdated(updatedAt)
                                .eventTypes(listOfEventTypes)
                                .name(name).website(website).information(information)
                                .contactInformation(contactInformation).build()));
    }

    private Mono<Organizer> saveOrganizer(final OrganizerDto organizer) {

        final UUID organizerUuid = organizer.uuid();
        final UUID uuid = organizerUuid != null ? organizerUuid : UUID.randomUUID();

        final Instant instantNow = Instant.now();

        final String name = organizer.name();
        final String website = organizer.website();
        final String information = organizer.information();
        final List<EventType> listOfEventTypes = organizer.eventTypes();
        final ContactInformation contactInformation = organizer.contactInformation();
        final EventType[] eventTypesArray = convertToArray(listOfEventTypes);

        final Mono<Long> rowsAffected = databaseClient
                .sql(organizerQueriesProperties.get(CrudQueriesOperations.SAVE)).bind(0, uuid)
                .bind(1, instantNow).bind(2, instantNow).bind(3, name).bind(4, website).bind(5, information)
                .bind(6, eventTypesArray).bind(7, contactInformation.email()).bind(8, contactInformation.phoneNumber())
                .bind(9, contactInformation.physicalAddress()).fetch().rowsUpdated();

        return rowsAffected.filter(this::rowsAffectedAreMoreThanOne)
                .map(rowNum -> organizerDtoToOrganizerConverter.apply(OrganizerDto.builder()
                        .uuid(uuid)
                        .createdAt(instantNow)
                        .lastUpdated(instantNow)
                        .eventTypes(listOfEventTypes)
                        .name(name).website(website).information(information)
                        .contactInformation(contactInformation).build()));
    }

    private EventType[] convertToArray(final List<EventType> ticketIds) {

        if (null == ticketIds) {

            return new EventType[]{};
        }

        final EventType[] eventTypesArray = new EventType[ticketIds.size()];

        for (int i = 0; i < eventTypesArray.length; i++) {

            eventTypesArray[i] = ticketIds.get(i);
        }

        return eventTypesArray;
    }

    private boolean rowsAffectedAreMoreThanOne(final Long x) {

        return x >= 1;
    }

}
