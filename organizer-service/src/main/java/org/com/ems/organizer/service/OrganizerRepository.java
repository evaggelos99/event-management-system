package org.com.ems.organizer.service;

import static java.util.Objects.requireNonNull;

import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.Properties;
import java.util.UUID;
import java.util.function.Function;

import org.com.ems.common.api.db.CrudQueriesOperations;
import org.com.ems.common.api.domainobjects.AbstractDomainObject;
import org.com.ems.common.api.domainobjects.ContactInformation;
import org.com.ems.common.api.domainobjects.EventType;
import org.com.ems.organizer.api.Organizer;
import org.com.ems.organizer.api.OrganizerDto;
import org.com.ems.organizer.api.converters.OrganizerDtoToOrganizerConverter;
import org.com.ems.organizer.api.repo.IOrganizerRepository;
import org.com.ems.organizer.api.repo.OrganizerRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Component;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class OrganizerRepository implements IOrganizerRepository {

	private final DatabaseClient databaseClient;
	private final OrganizerRowMapper organizerRowMapper;
	private final Function<OrganizerDto, Organizer> organizerDtoToOrganizerConverter;
	private final Properties organizerQueriesProperties;

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
	 * @param organizerQueriesProperties       the {@link Properties} which are used
	 *                                         for getting the right query CRUD
	 *                                         database operations
	 */
	public OrganizerRepository(@Autowired final DatabaseClient databaseClient,
			@Autowired @Qualifier("organizerRowMapper") final OrganizerRowMapper organizerRowMapper,
			@Autowired @Qualifier("organizerDtoToOrganizerConverter") final Function<OrganizerDto, Organizer> organizerDtoToOrganizerConverter,
			@Autowired @Qualifier("queriesProperties") final Properties organizerQueriesProperties) {

		this.databaseClient = requireNonNull(databaseClient);
		this.organizerRowMapper = requireNonNull(organizerRowMapper);
		this.organizerDtoToOrganizerConverter = requireNonNull(organizerDtoToOrganizerConverter);
		this.organizerQueriesProperties = requireNonNull(organizerQueriesProperties);

	}

	@Override
	public Mono<Organizer> save(final OrganizerDto organizerDto) {

		return this.saveOrganizer(organizerDto);

	}

	@Override
	public Mono<Organizer> edit(final OrganizerDto organizerDto) {

		return this.editOrganizer(organizerDto);

	}

	@Override
	public Mono<Organizer> findById(final UUID uuid) {

		return this.databaseClient.sql(this.organizerQueriesProperties.getProperty(CrudQueriesOperations.GET_ID.name()))
				.bind(0, uuid).map(this.organizerRowMapper::apply).one();

	}

	@Override
	public Mono<Boolean> deleteById(final UUID uuid) {

		return this.databaseClient
				.sql(this.organizerQueriesProperties.getProperty(CrudQueriesOperations.DELETE_ID.name())).bind(0, uuid)
				.fetch().rowsUpdated().map(this::rowsAffectedAreMoreThanOne);

	}

	@Override
	public Mono<Boolean> existsById(final UUID uuid) {

		return this.findById(uuid).map(Objects::nonNull);

	}

	@Override
	public Flux<Organizer> findAll() {

		return this.databaseClient
				.sql(this.organizerQueriesProperties.getProperty(CrudQueriesOperations.GET_ALL.name()))
				.map(this.organizerRowMapper::apply).all();

	}

	private Mono<Organizer> saveOrganizer(final OrganizerDto organizer) {

		final UUID organizerUuid = organizer.uuid();
		final UUID uuid = organizerUuid != null ? organizerUuid : UUID.randomUUID();

		final Instant instantNow = Instant.now();

		final String name = organizer.name();
		final String website = organizer.website();
		final String description = organizer.information();
		final List<EventType> listOfEventTypes = organizer.eventTypes();
		final ContactInformation contactInformation = organizer.contactInformation();
		final EventType[] eventTypesArray = this.convertToArray(listOfEventTypes);

		final Mono<Long> rowsAffected = this.databaseClient
				.sql(this.organizerQueriesProperties.getProperty(CrudQueriesOperations.SAVE.name())).bind(0, uuid)
				.bind(1, instantNow).bind(2, instantNow).bind(3, name).bind(4, website).bind(5, description)
				.bind(6, eventTypesArray).bind(7, contactInformation.email()).bind(8, contactInformation.phoneNumber())
				.bind(9, contactInformation.physicalAddress()).fetch().rowsUpdated();

		return rowsAffected.filter(this::rowsAffectedAreMoreThanOne)
				.map(n_ -> this.organizerDtoToOrganizerConverter.apply(new OrganizerDto(uuid, instantNow, instantNow,
						name, website, description, listOfEventTypes, contactInformation)));

	}

	private Mono<Organizer> editOrganizer(final OrganizerDto organizer) {

		final UUID uuid = organizer.uuid();
		final Instant updatedAt = Instant.now();

		final String name = organizer.name();
		final String website = organizer.website();
		final String description = organizer.information();
		final List<EventType> listOfEventTypes = organizer.eventTypes();
		final ContactInformation contactInformation = organizer.contactInformation();
		final EventType[] eventTypesArray = this.convertToArray(listOfEventTypes);

		final Mono<Long> rowsAffected = this.databaseClient
				.sql(this.organizerQueriesProperties.getProperty(CrudQueriesOperations.EDIT.name())).bind(0, uuid)
				.bind(1, updatedAt).bind(2, name).bind(3, website).bind(4, description).bind(5, eventTypesArray)
				.bind(6, contactInformation.email()).bind(7, contactInformation.phoneNumber())
				.bind(8, contactInformation.physicalAddress()).bind(9, uuid).fetch().rowsUpdated();

		return rowsAffected.filter(this::rowsAffectedAreMoreThanOne).flatMap(n_ -> this.findById(uuid))
				.map(AbstractDomainObject::getCreatedAt)
				.map(createdAt -> this.organizerDtoToOrganizerConverter.apply(new OrganizerDto(uuid, createdAt,
						updatedAt, name, website, description, listOfEventTypes, contactInformation)));

	}

	private EventType[] convertToArray(final List<EventType> ticketIds) {

		if (null == ticketIds) {

			return new EventType[] {};
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
