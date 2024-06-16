package org.com.ems.db.impl;

import static java.util.Objects.requireNonNull;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Objects;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Component;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class SponsorRepository implements ISponsorRepository {

    private final DatabaseClient databaseClient;
    private final SponsorRowMapper sponsorRowMapper;
    private final Function<SponsorDto, Sponsor> sponsDtoToSponsorConverter;
    private final Properties sponsorQueriesProperties;

    /**
     * C-or
     *
     * @param jdbcTemplate                 the {@link DatabaseClient} used for
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
    public SponsorRepository(@Autowired final DatabaseClient jdbcTemplate,
			     @Autowired @Qualifier("sponsorRowMapper") final SponsorRowMapper sponsorRowMapper,
			     @Autowired @Qualifier("sponsorDtoToSponsorConverter") final Function<SponsorDto,
				     Sponsor> sponsorDtoToSponsorConverter,
			     @Autowired @Qualifier("sponsorQueriesProperties") final Properties sponsorQueriesProperties) {

	this.databaseClient = requireNonNull(jdbcTemplate);
	this.sponsorRowMapper = requireNonNull(sponsorRowMapper);
	this.sponsDtoToSponsorConverter = requireNonNull(sponsorDtoToSponsorConverter);
	this.sponsorQueriesProperties = requireNonNull(sponsorQueriesProperties);

    }

    @Override
    public Mono<Sponsor> save(final SponsorDto organizerDto) {

	return this.saveSponsor(organizerDto);

    }

    @Override
    public Mono<Sponsor> edit(final SponsorDto organizerDto) {

	return this.editOrganizer(organizerDto);

    }

    @Override
    public Mono<Sponsor> findById(final UUID uuid) {

	return this.databaseClient.sql(this.sponsorQueriesProperties.getProperty(CrudQueriesOperations.GET_ID.name()))
		.bind(0, uuid).map(this.sponsorRowMapper::apply).one();

    }

    @Override
    public Mono<Boolean> deleteById(final UUID uuid) {

	return this.databaseClient
		.sql(this.sponsorQueriesProperties.getProperty(CrudQueriesOperations.DELETE_ID.name())).bind(0, uuid)
		.fetch().rowsUpdated().map(this::rowsAffectedAreMoreThanOne);

    }

    @Override
    public Mono<Boolean> existsById(final UUID uuid) {

	return this.findById(uuid).map(Objects::nonNull);

    }

    @Override
    public Flux<Sponsor> findAll() {

	return this.databaseClient.sql(this.sponsorQueriesProperties.getProperty(CrudQueriesOperations.GET_ALL.name()))
		.map(this.sponsorRowMapper::apply).all().log();

    }

    private Mono<Sponsor> saveSponsor(final SponsorDto sponsor) {

	final UUID sponsorUuid = sponsor.uuid();
	final Instant now = Instant.now();
	final Timestamp createdAt = Timestamp.from(now);
	final Timestamp updatedAt = Timestamp.from(now);

	final UUID uuid = sponsorUuid != null ? sponsorUuid : UUID.randomUUID();
	final String name = sponsor.denomination();
	final String website = sponsor.website();
	final Integer financialContribution = sponsor.financialContribution();
	final ContactInformation contactInformation = sponsor.contactInformation();

	final Mono<Long> rowsAffected = this.databaseClient
		.sql(this.sponsorQueriesProperties.getProperty(CrudQueriesOperations.SAVE.name())).bind(0, uuid)
		.bind(1, createdAt).bind(2, updatedAt).bind(3, name).bind(4, website).bind(5, financialContribution)
		.bind(6, contactInformation.email()).bind(7, contactInformation.phoneNumber())
		.bind(8, contactInformation.physicalAddress()).fetch().rowsUpdated();

	return rowsAffected.filter(this::rowsAffectedAreMoreThanOne).map(n_ -> this.sponsDtoToSponsorConverter.apply(
		new SponsorDto(uuid, createdAt, updatedAt, name, website, financialContribution, contactInformation)));

    }

    private Mono<Sponsor> editOrganizer(final SponsorDto sponsor) {

	final UUID uuid = sponsor.uuid();
	final Timestamp updatedAt = Timestamp.from(Instant.now());
	final String name = sponsor.denomination();
	final String website = sponsor.website();
	final Integer financialContribution = sponsor.financialContribution();
	final ContactInformation contactInformation = sponsor.contactInformation();

	final Mono<Long> rowsAffected = this.databaseClient
		.sql(this.sponsorQueriesProperties.getProperty(CrudQueriesOperations.EDIT.name())).bind(0, uuid)
		.bind(1, updatedAt).bind(2, name).bind(3, website).bind(4, financialContribution)
		.bind(5, contactInformation.email()).bind(6, contactInformation.phoneNumber())
		.bind(7, contactInformation.physicalAddress()).bind(8, uuid).fetch().rowsUpdated();

	return rowsAffected.filter(this::rowsAffectedAreMoreThanOne).flatMap(n_ -> this.findById(uuid))
		.map(AbstractDomainObject::getCreatedAt)
		.map(monoCreatedAt -> this.sponsDtoToSponsorConverter
			.apply(new SponsorDto(uuid, Timestamp.from(monoCreatedAt), updatedAt, name, website,
				financialContribution, contactInformation)));

    }

    private boolean rowsAffectedAreMoreThanOne(final Long x) {

	return x >= 1;

    }
}
