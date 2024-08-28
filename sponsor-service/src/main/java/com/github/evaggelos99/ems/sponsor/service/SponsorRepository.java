package com.github.evaggelos99.ems.sponsor.service;

import static java.util.Objects.requireNonNull;

import java.time.Instant;
import java.util.Objects;
import java.util.Properties;
import java.util.UUID;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Component;

import com.github.evaggelos99.ems.common.api.db.CrudQueriesOperations;
import com.github.evaggelos99.ems.common.api.domainobjects.AbstractDomainObject;
import com.github.evaggelos99.ems.common.api.domainobjects.ContactInformation;
import com.github.evaggelos99.ems.sponsor.api.Sponsor;
import com.github.evaggelos99.ems.sponsor.api.SponsorDto;
import com.github.evaggelos99.ems.sponsor.api.converters.SponsorDtoToSponsorConverter;
import com.github.evaggelos99.ems.sponsor.api.repo.ISponsorRepository;
import com.github.evaggelos99.ems.sponsor.api.repo.SponsorRowMapper;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class SponsorRepository implements ISponsorRepository {

	private final DatabaseClient databaseClient;
	private final SponsorRowMapper sponsorRowMapper;
	private final Function<SponsorDto, Sponsor> sponsorDtoToSponsorConverter;
	private final Properties sponsorQueriesProperties;

	/**
	 * C-or
	 *
	 * @param databaseClient               the {@link DatabaseClient} used for
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
	public SponsorRepository(@Autowired final DatabaseClient databaseClient,
			@Autowired @Qualifier("sponsorRowMapper") final SponsorRowMapper sponsorRowMapper,
			@Autowired @Qualifier("sponsorDtoToSponsorConverter") final Function<SponsorDto, Sponsor> sponsorDtoToSponsorConverter,
			@Autowired @Qualifier("queriesProperties") final Properties sponsorQueriesProperties) {

		this.databaseClient = requireNonNull(databaseClient);
		this.sponsorRowMapper = requireNonNull(sponsorRowMapper);
		this.sponsorDtoToSponsorConverter = requireNonNull(sponsorDtoToSponsorConverter);
		this.sponsorQueriesProperties = requireNonNull(sponsorQueriesProperties);
	}

	@Override
	public Mono<Sponsor> save(final SponsorDto organizerDto) {

		return saveSponsor(organizerDto);

	}

	@Override
	public Mono<Sponsor> edit(final SponsorDto organizerDto) {

		return editSponsor(organizerDto);

	}

	@Override
	public Mono<Sponsor> findById(final UUID uuid) {

		return databaseClient.sql(sponsorQueriesProperties.getProperty(CrudQueriesOperations.GET_ID.name()))
				.bind(0, uuid).map(sponsorRowMapper::apply).one();

	}

	@Override
	public Mono<Boolean> deleteById(final UUID uuid) {

		return databaseClient.sql(sponsorQueriesProperties.getProperty(CrudQueriesOperations.DELETE_ID.name()))
				.bind(0, uuid).fetch().rowsUpdated().map(this::rowsAffectedAreMoreThanOne);

	}

	@Override
	public Mono<Boolean> existsById(final UUID uuid) {

		return findById(uuid).map(Objects::nonNull);

	}

	@Override
	public Flux<Sponsor> findAll() {

		return databaseClient.sql(sponsorQueriesProperties.getProperty(CrudQueriesOperations.GET_ALL.name()))
				.map(sponsorRowMapper::apply).all().log();

	}

	private Mono<Sponsor> saveSponsor(final SponsorDto sponsor) {

		final UUID sponsorUuid = sponsor.uuid();
		final Instant instantNow = Instant.now();

		final UUID uuid = sponsorUuid != null ? sponsorUuid : UUID.randomUUID();
		final String name = sponsor.name();
		final String website = sponsor.website();
		final Integer financialContribution = sponsor.financialContribution();
		final ContactInformation contactInformation = sponsor.contactInformation();

		final Mono<Long> rowsAffected = databaseClient
				.sql(sponsorQueriesProperties.getProperty(CrudQueriesOperations.SAVE.name())).bind(0, uuid)
				.bind(1, instantNow).bind(2, instantNow).bind(3, name).bind(4, website).bind(5, financialContribution)
				.bind(6, contactInformation.email()).bind(7, contactInformation.phoneNumber())
				.bind(8, contactInformation.physicalAddress()).fetch().rowsUpdated();

		return rowsAffected.filter(this::rowsAffectedAreMoreThanOne)
				.map(n_ -> sponsorDtoToSponsorConverter.apply(new SponsorDto(uuid, instantNow, instantNow, name,
						website, financialContribution, contactInformation)));

	}

	private Mono<Sponsor> editSponsor(final SponsorDto sponsor) {

		final UUID uuid = sponsor.uuid();
		final Instant updatedAt = Instant.now();
		final String name = sponsor.name();
		final String website = sponsor.website();
		final Integer financialContribution = sponsor.financialContribution();
		final ContactInformation contactInformation = sponsor.contactInformation();

		final Mono<Long> rowsAffected = databaseClient
				.sql(sponsorQueriesProperties.getProperty(CrudQueriesOperations.EDIT.name())).bind(0, uuid)
				.bind(1, updatedAt).bind(2, name).bind(3, website).bind(4, financialContribution)
				.bind(5, contactInformation.email()).bind(6, contactInformation.phoneNumber())
				.bind(7, contactInformation.physicalAddress()).bind(8, uuid).fetch().rowsUpdated();

		return rowsAffected.filter(this::rowsAffectedAreMoreThanOne).flatMap(n_ -> findById(uuid))
				.map(AbstractDomainObject::getCreatedAt)
				.map(monoCreatedAt -> sponsorDtoToSponsorConverter.apply(new SponsorDto(uuid, monoCreatedAt, updatedAt,
						name, website, financialContribution, contactInformation)));

	}

	private boolean rowsAffectedAreMoreThanOne(final Long x) {

		return x >= 1;

	}
}
