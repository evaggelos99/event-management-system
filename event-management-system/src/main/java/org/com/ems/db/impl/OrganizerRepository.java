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

import org.com.ems.api.domainobjects.ContactInformation;
import org.com.ems.api.domainobjects.EventType;
import org.com.ems.api.domainobjects.Organizer;
import org.com.ems.api.dto.OrganizerDto;
import org.com.ems.db.IOrganizerRepository;
import org.com.ems.db.queries.Queries.CrudQueriesOperations;
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
    public Optional<Organizer> findById(final UUID uuid) {

	try {

	    final Organizer attendee = this.jdbcTemplate.queryForObject(
		    this.organizerQueriesProperties.getProperty(CrudQueriesOperations.GET_ID.name()),
		    this.organizerRowMapper, uuid);
	    return Optional.of(attendee);
	} catch (final EmptyResultDataAccessException e) {

	    LOGGER.warn("Organizer with UUID: {} was not found", uuid);
	    return Optional.empty();
	}

    }

    @Override
    public boolean deleteById(final UUID uuid) {

	final int rows = this.jdbcTemplate
		.update(this.organizerQueriesProperties.getProperty(CrudQueriesOperations.DELETE_ID.name()), uuid);

	final boolean deleted = rows == 1 ? true : false;

	if (deleted) {

	    LOGGER.trace("Deleted Organizer with uuid: " + uuid);
	} else {

	    LOGGER.trace("Could not delete Organizer with uuid: " + uuid);
	}

	return deleted;

    }

    @Override
    public boolean existsById(final UUID uuid) {

	return this.findById(uuid).isPresent();

    }

    @Override
    public Collection<Organizer> findAll() {

	return this.jdbcTemplate.query(
		this.organizerQueriesProperties.getProperty(CrudQueriesOperations.GET_ALL.name()),
		this.organizerRowMapper);

    }

    private Organizer saveOrganizer(final OrganizerDto organizer) {

	final UUID organizerUuid = organizer.uuid();
	final Timestamp timestamp = Timestamp.from(Instant.now());
	final String name = organizer.denomination();
	final String website = organizer.website();
	final String description = organizer.information();
	final List<EventType> listOfEventTypes = organizer.eventTypes();
	final ContactInformation contactInformation = organizer.contactInformation();
	final String[] eventTypesArray = this.convertToArray(listOfEventTypes);
	final UUID uuid = organizerUuid != null ? organizerUuid : UUID.randomUUID();

	System.out.println(eventTypesArray);

	this.jdbcTemplate.update(this.organizerQueriesProperties.getProperty(CrudQueriesOperations.SAVE.name()), uuid,
		timestamp, name, website, description, eventTypesArray, contactInformation.getEmail(),
		contactInformation.getPhoneNumber(), contactInformation.getPhysicalAddress());

	return this.organizerDtoToOrganizerConverter.apply(
		new OrganizerDto(uuid, timestamp, name, website, description, listOfEventTypes, contactInformation));

    }

    private Organizer editOrganizer(final OrganizerDto organizer) {

	final UUID uuid = organizer.uuid();
	final Timestamp timestamp = Timestamp.from(Instant.now());
	final String name = organizer.denomination();
	final String website = organizer.website();
	final String description = organizer.information();
	final List<EventType> listOfEventTypes = organizer.eventTypes();
	final ContactInformation contactInformation = organizer.contactInformation();
	final String[] eventTypesArray = this.convertToArray(listOfEventTypes);

	this.jdbcTemplate.update(this.organizerQueriesProperties.getProperty(CrudQueriesOperations.EDIT.name()), uuid,
		timestamp, name, website, description, eventTypesArray, contactInformation.getEmail(),
		contactInformation.getPhoneNumber(), contactInformation.getPhysicalAddress());

	return this.organizerDtoToOrganizerConverter.apply(
		new OrganizerDto(uuid, timestamp, name, website, description, listOfEventTypes, contactInformation));

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

}
