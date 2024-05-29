package org.com.ems.db.impl;

import static java.util.Objects.requireNonNull;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Collection;
import java.util.Optional;
import java.util.Properties;
import java.util.UUID;
import java.util.function.Function;

import org.com.ems.api.converters.SponsorDtoToSponsorConverter;
import org.com.ems.api.domainobjects.AbstractDomainObject;
import org.com.ems.api.domainobjects.ContactInformation;
import org.com.ems.api.domainobjects.Sponsor;
import org.com.ems.api.dto.SponsorDto;
import org.com.ems.db.ISponsorRepository;
import org.com.ems.db.queries.Queries.CrudQueriesOperations;
import org.com.ems.db.rowmappers.SponsorRowMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

@Component
public class SponsorRepository implements ISponsorRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(SponsorRepository.class);

    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<Sponsor> sponsorRowMapper;
    private final Function<SponsorDto, Sponsor> sponsDtoToSponsorConverter;
    private final Properties sponsorQueriesProperties;

    /**
     * C-or
     *
     * @param jdbcTemplate                 the {@link JdbcTemplate} used for
     *                                     connecting to the database for the
     *                                     Sponsor objects
     * @param sponsorRowMapper             the {@link SponsorRowMapper} used for
     *                                     returning Event objects from the database
     * @param sponsorDtoToSponsorConverter the {@link SponsorDtoToSponsorConverter}
     *                                     used for converting {@link SponsorDto} to
     *                                     {@link Sponsor}
     * @param sponsorQueriesProperties     the {@link Properties} which are used for
     *                                     getting the right query CRUD database
     *                                     operations
     */
    public SponsorRepository(@Autowired final JdbcTemplate jdbcTemplate,
			     @Autowired @Qualifier("sponsorRowMapper") final RowMapper<Sponsor> sponsorRowMapper,
			     @Autowired @Qualifier("sponsorDtoToSponsorConverter") final Function<SponsorDto,
				     Sponsor> sponsorDtoToSponsorConverter,
			     @Autowired @Qualifier("sponsorQueriesProperties") final Properties sponsorQueriesProperties) {

	this.jdbcTemplate = requireNonNull(jdbcTemplate);
	this.sponsorRowMapper = requireNonNull(sponsorRowMapper);
	this.sponsDtoToSponsorConverter = requireNonNull(sponsorDtoToSponsorConverter);
	this.sponsorQueriesProperties = requireNonNull(sponsorQueriesProperties);

    }

    @Override
    public Sponsor save(final SponsorDto organizerDto) {

	final Sponsor sponsor = this.saveSponsor(organizerDto);

	LOGGER.trace("Saved an Sponsor: " + sponsor);

	return sponsor;

    }

    @Override
    public Sponsor edit(final SponsorDto organizerDto) {

	final Sponsor sponsor = this.editOrganizer(organizerDto);

	LOGGER.trace("Edited an Sponsor: " + sponsor);

	return sponsor;

    }

    @Override
    public Optional<Sponsor> findById(final UUID id) {

	try {

	    final Sponsor sponsor = this.jdbcTemplate.queryForObject(
		    this.sponsorQueriesProperties.getProperty(CrudQueriesOperations.GET_ID.name()),
		    this.sponsorRowMapper, id);
	    return Optional.of(sponsor);
	} catch (final EmptyResultDataAccessException e) {

	    LOGGER.warn("Sponsor with UUID: {} was not found", id);
	    return Optional.empty();
	}

    }

    @Override
    public boolean deleteById(final UUID id) {

	final int rows = this.jdbcTemplate
		.update(this.sponsorQueriesProperties.getProperty(CrudQueriesOperations.DELETE_ID.name()), id);

	final boolean deleted = rows == 1;

	if (deleted) {

	    LOGGER.trace("Deleted Sponsor with id: " + id);
	} else {

	    LOGGER.trace("Could not delete Sponsor with id: " + id);
	}

	return deleted;

    }

    @Override
    public boolean existsById(final UUID id) {

	return this.findById(id).isPresent();

    }

    @Override
    public Collection<Sponsor> findAll() {

	return this.jdbcTemplate.query(this.sponsorQueriesProperties.getProperty(CrudQueriesOperations.GET_ALL.name()),
		this.sponsorRowMapper);

    }

    private Sponsor saveSponsor(final SponsorDto sponsor) {

	final UUID sponsorUuid = sponsor.id();
	final Instant now = Instant.now();
	final Timestamp createdAt = Timestamp.from(now);
	final Timestamp updatedAt = Timestamp.from(now);

	final UUID id = sponsorUuid != null ? sponsorUuid : UUID.randomUUID();

	return this.saveOrEditCommand(id, createdAt, updatedAt, sponsor, true);

    }

    private Sponsor saveOrEditCommand(final UUID id,
				      final Timestamp createdAt,
				      final Timestamp updatedAt,
				      final SponsorDto sponsor,
				      final boolean isSaveOperation) {

	final String name = sponsor.name();
	final String website = sponsor.website();
	final Integer financialContribution = sponsor.financialContribution();
	final ContactInformation contactInformation = sponsor.contactInformation();

	if (isSaveOperation) {

	    this.jdbcTemplate.update(this.sponsorQueriesProperties.getProperty(CrudQueriesOperations.SAVE.name()), id,
		    createdAt, updatedAt, name, website, financialContribution, contactInformation.email(),
		    contactInformation.phoneNumber(), contactInformation.physicalAddress());

	} else {

	    this.jdbcTemplate.update(this.sponsorQueriesProperties.getProperty(CrudQueriesOperations.EDIT.name()), id,
		    createdAt, updatedAt, name, website, financialContribution, contactInformation.email(),
		    contactInformation.phoneNumber(), contactInformation.physicalAddress(), id);
	}

	return this.sponsDtoToSponsorConverter.apply(
		new SponsorDto(id, createdAt, createdAt, name, website, financialContribution, contactInformation));

    }

    private Sponsor editOrganizer(final SponsorDto sponsor) {

	final UUID id = sponsor.id();
	final Timestamp createdAt = this.getCreatedAt(id);
	final Timestamp updatedAt = Timestamp.from(Instant.now());

	return this.saveOrEditCommand(id, createdAt, updatedAt, sponsor, false);

    }

    private Timestamp getCreatedAt(final UUID id) {

	return Timestamp.from(this.findById(id).map(AbstractDomainObject::getCreatedAt).orElse(Instant.now()));

    }
}
