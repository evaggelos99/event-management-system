package org.com.ems.db.impl;

import static java.util.Objects.requireNonNull;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Collection;
import java.util.Optional;
import java.util.Properties;
import java.util.UUID;
import java.util.function.Function;

import org.com.ems.api.domainobjects.ContactInformation;
import org.com.ems.api.domainobjects.Sponsor;
import org.com.ems.api.dto.SponsorDto;
import org.com.ems.db.ISponsorRepository;
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
public class SponsorRepository implements ISponsorRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(SponsorRepository.class);

    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<Sponsor> sponsorRowMapper;
    private final Function<SponsorDto, Sponsor> sponsDtoToSponsorConverter;
    private final Properties sponsorQueriesProperties;

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
    public Optional<Sponsor> findById(final UUID uuid) {

	try {

	    final Sponsor sponsor = this.jdbcTemplate.queryForObject(
		    this.sponsorQueriesProperties.getProperty(CrudQueriesOperations.GET_ID.name()),
		    this.sponsorRowMapper, uuid);
	    return Optional.of(sponsor);
	} catch (final EmptyResultDataAccessException e) {

	    LOGGER.warn("Sponsor with UUID: {} was not found", uuid);
	    return Optional.empty();
	}

    }

    @Override
    public boolean deleteById(final UUID uuid) {

	final int rows = this.jdbcTemplate
		.update(this.sponsorQueriesProperties.getProperty(CrudQueriesOperations.DELETE_ID.name()), uuid);

	final boolean deleted = rows == 1 ? true : false;

	if (deleted) {

	    LOGGER.trace("Deleted Sponsor with uuid: " + uuid);
	} else {

	    LOGGER.trace("Could not delete Sponsor with uuid: " + uuid);
	}

	return deleted;

    }

    @Override
    public boolean existsById(final UUID uuid) {

	return this.findById(uuid).isPresent();

    }

    @Override
    public Collection<Sponsor> findAll() {

	return this.jdbcTemplate.query(this.sponsorQueriesProperties.getProperty(CrudQueriesOperations.GET_ALL.name()),
		this.sponsorRowMapper);

    }

    private Sponsor saveSponsor(final SponsorDto sponsor) {

	final UUID sponsorUuid = sponsor.uuid();
	final Timestamp timestamp = Timestamp.from(Instant.now());
	final String name = sponsor.denomination();
	final String website = sponsor.website();
	final Integer financialContribution = sponsor.financialContribution();
	final ContactInformation contactInformation = sponsor.contactInformation();

	final UUID uuid = sponsorUuid != null ? sponsorUuid : UUID.randomUUID();

	this.jdbcTemplate.update(this.sponsorQueriesProperties.getProperty(CrudQueriesOperations.SAVE.name()), uuid,
		timestamp, name, website, financialContribution, contactInformation.getEmail(),
		contactInformation.getPhoneNumber(), contactInformation.getPhysicalAddress());

	return this.sponsDtoToSponsorConverter
		.apply(new SponsorDto(uuid, timestamp, name, website, financialContribution, contactInformation));

    }

    private Sponsor editOrganizer(final SponsorDto sponsor) {

	final UUID uuid = sponsor.uuid();
	final Timestamp timestamp = Timestamp.from(Instant.now());
	final String name = sponsor.denomination();
	final String website = sponsor.website();
	final Integer financialContribution = sponsor.financialContribution();
	final ContactInformation contactInformation = sponsor.contactInformation();

	this.jdbcTemplate.update(this.sponsorQueriesProperties.getProperty(CrudQueriesOperations.EDIT.name()), uuid,
		timestamp, name, website, financialContribution, contactInformation.getEmail(),
		contactInformation.getPhoneNumber(), contactInformation.getPhysicalAddress(), uuid);

	return this.sponsDtoToSponsorConverter
		.apply(new SponsorDto(uuid, timestamp, name, website, financialContribution, contactInformation));

    }

}
