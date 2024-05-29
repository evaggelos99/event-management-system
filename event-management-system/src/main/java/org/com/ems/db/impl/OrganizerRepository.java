package org.com.ems.db.impl;

import static java.util.Objects.requireNonNull;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Properties;
import java.util.UUID;
import java.util.function.Function;

import org.com.ems.api.converters.OrganizerDtoToOrganizerConverter;
import org.com.ems.api.domainobjects.AbstractDomainObject;
import org.com.ems.api.domainobjects.ContactInformation;
import org.com.ems.api.domainobjects.EventType;
import org.com.ems.api.domainobjects.Organizer;
import org.com.ems.api.dto.OrganizerDto;
import org.com.ems.db.IOrganizerRepository;
import org.com.ems.db.queries.Queries.CrudQueriesOperations;
import org.com.ems.db.rowmappers.OrganizerRowMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

@Component
public class OrganizerRepository implements IOrganizerRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrganizerRepository.class);

    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<Organizer> organizerRowMapper;
    private final Function<OrganizerDto, Organizer> organizerDtoToOrganizerConverter;
    private final Properties organizerQueriesProperties;

    /**
     * C-or
     *
     * @param jdbcTemplate                     the {@link JdbcTemplate} used for
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
    public OrganizerRepository(@Autowired final JdbcTemplate jdbcTemplate,
			       @Autowired @Qualifier("organizerRowMapper") final RowMapper<
				       Organizer> organizerRowMapper,
			       @Autowired @Qualifier("organizerDtoToOrganizerConverter") final Function<OrganizerDto,
				       Organizer> organizerDtoToOrganizerConverter,
			       @Autowired @Qualifier("organizerQueriesProperties") final Properties organizerQueriesProperties) {

	this.jdbcTemplate = requireNonNull(jdbcTemplate);
	this.organizerRowMapper = requireNonNull(organizerRowMapper);
	this.organizerDtoToOrganizerConverter = requireNonNull(organizerDtoToOrganizerConverter);
	this.organizerQueriesProperties = requireNonNull(organizerQueriesProperties);

    }

    @Override
    public Organizer save(final OrganizerDto organizerDto) {

	final Organizer organizer = this.saveOrganizer(organizerDto);

	LOGGER.trace("Saved an Organizer: " + organizer);

	return organizer;

    }

    @Override
    public Organizer edit(final OrganizerDto organizerDto) {

	final Organizer organizer = this.editOrganizer(organizerDto);

	LOGGER.trace("Edited an Organizer: " + organizer);

	return organizer;

    }

    @Override
    public Optional<Organizer> findById(final UUID id) {

	try {

	    final Organizer organizer = this.jdbcTemplate.queryForObject(
		    this.organizerQueriesProperties.getProperty(CrudQueriesOperations.GET_ID.name()),
		    this.organizerRowMapper, id);
	    return Optional.of(organizer);
	} catch (final EmptyResultDataAccessException e) {

	    LOGGER.warn("Organizer with UUID: {} was not found", id);
	    return Optional.empty();
	}

    }

    @Override
    public boolean deleteById(final UUID id) {

	final int rows = this.jdbcTemplate
		.update(this.organizerQueriesProperties.getProperty(CrudQueriesOperations.DELETE_ID.name()), id);

	final boolean deleted = rows == 1;

	if (deleted) {

	    LOGGER.trace("Deleted Organizer with id: " + id);
	} else {

	    LOGGER.trace("Could not delete Organizer with id: " + id);
	}

	return deleted;

    }

    @Override
    public boolean existsById(final UUID id) {

	return this.findById(id).isPresent();

    }

    @Override
    public Collection<Organizer> findAll() {

	return this.jdbcTemplate.query(
		this.organizerQueriesProperties.getProperty(CrudQueriesOperations.GET_ALL.name()),
		this.organizerRowMapper);

    }

    private Organizer saveOrganizer(final OrganizerDto organizer) {

	final UUID organizerUuid = organizer.id();
	final Instant now = Instant.now();
	final Timestamp createdAt = Timestamp.from(now);
	final Timestamp updatedAt = Timestamp.from(now);

	final UUID id = organizerUuid != null ? organizerUuid : UUID.randomUUID();
	return this.saveOrEditCommand(id, createdAt, updatedAt, organizer, true);

    }

    private Organizer editOrganizer(final OrganizerDto organizer) {

	final UUID id = organizer.id();
	final Timestamp createdAt = this.getCreatedAt(id);
	final Timestamp updatedAt = Timestamp.from(Instant.now());

	return this.saveOrEditCommand(id, createdAt, updatedAt, organizer, false);

    }

    private Organizer saveOrEditCommand(final UUID id,
					final Timestamp createdAt,
					final Timestamp updatedAt,
					final OrganizerDto organizer,
					final boolean isSaveOperation) {

	final String name = organizer.name();
	final String website = organizer.website();
	final String description = organizer.information();
	final List<EventType> listOfEventTypes = organizer.eventTypes();
	final ContactInformation contactInformation = organizer.contactInformation();
	final String[] eventTypesArray = this.convertToArray(listOfEventTypes);

	if (isSaveOperation) {

	    this.jdbcTemplate.update(this.organizerQueriesProperties.getProperty(CrudQueriesOperations.SAVE.name()), id,
		    createdAt, updatedAt, name, website, description, eventTypesArray, contactInformation.email(),
		    contactInformation.phoneNumber(), contactInformation.physicalAddress());
	} else {

	    this.jdbcTemplate.update(this.organizerQueriesProperties.getProperty(CrudQueriesOperations.EDIT.name()), id,
		    createdAt, updatedAt, name, website, description, eventTypesArray, contactInformation.email(),
		    contactInformation.phoneNumber(), contactInformation.physicalAddress(), id);

	}

	return this.organizerDtoToOrganizerConverter.apply(new OrganizerDto(id, createdAt, updatedAt, name, website,
		description, listOfEventTypes, contactInformation));

    }

    private String[] convertToArray(final List<EventType> ticketIds) {

	if (null == ticketIds) {

	    return new String[] {};
	}

	final String[] eventTypesArray = new String[ticketIds.size()];

	for (int i = 0; i < eventTypesArray.length; i++) {

	    eventTypesArray[i] = ticketIds.get(i).name();
	}
	return eventTypesArray;

    }

    private Timestamp getCreatedAt(final UUID id) {

	return Timestamp.from(this.findById(id).map(AbstractDomainObject::getCreatedAt).orElse(Instant.now()));

    }

}
