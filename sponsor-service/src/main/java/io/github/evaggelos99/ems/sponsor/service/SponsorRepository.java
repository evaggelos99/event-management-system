package io.github.evaggelos99.ems.sponsor.service;

import io.github.evaggelos99.ems.common.api.db.CrudQueriesOperations;
import io.github.evaggelos99.ems.common.api.domainobjects.AbstractDomainObject;
import io.github.evaggelos99.ems.common.api.domainobjects.ContactInformation;
import io.github.evaggelos99.ems.sponsor.api.Sponsor;
import io.github.evaggelos99.ems.sponsor.api.SponsorDto;
import io.github.evaggelos99.ems.sponsor.api.converters.SponsorDtoToSponsorConverter;
import io.github.evaggelos99.ems.sponsor.api.repo.ISponsorRepository;
import io.github.evaggelos99.ems.sponsor.api.repo.SponsorRowMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Function;

import static java.util.Objects.requireNonNull;

@Component
public class SponsorRepository implements ISponsorRepository {

    private final DatabaseClient databaseClient;
    private final SponsorRowMapper sponsorRowMapper;
    private final Function<SponsorDto, Sponsor> sponsorDtoToSponsorConverter;
    private final Map<CrudQueriesOperations, String> sponsorQueriesProperties;

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
     * @param sponsorQueriesProperties     the {@link Map} which are used for
     *                                     getting the right query CRUD database
     *                                     operations
     */
    public SponsorRepository(final DatabaseClient databaseClient,
                             @Qualifier("sponsorRowMapper") final SponsorRowMapper sponsorRowMapper,
                             @Qualifier("sponsorDtoToSponsorConverter") final Function<SponsorDto, Sponsor> sponsorDtoToSponsorConverter,
                             @Qualifier("queriesProperties") final Map<CrudQueriesOperations, String> sponsorQueriesProperties) {

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
    public Mono<Sponsor> findById(final UUID uuid) {

        return databaseClient.sql(sponsorQueriesProperties.get(CrudQueriesOperations.GET_ID))
                .bind(0, uuid).map(sponsorRowMapper).one();
    }

    @Override
    public Mono<Boolean> deleteById(final UUID uuid) {

        return databaseClient.sql(sponsorQueriesProperties.get(CrudQueriesOperations.DELETE_ID))
                .bind(0, uuid).fetch().rowsUpdated().map(this::rowsAffectedAreMoreThanOne);
    }

    @Override
    public Mono<Boolean> existsById(final UUID uuid) {

        return findById(uuid).map(Objects::nonNull);
    }

    @Override
    public Flux<Sponsor> findAll() {
        return databaseClient.sql(sponsorQueriesProperties.get(CrudQueriesOperations.GET_ALL))
                .map(sponsorRowMapper).all();
    }

    @Override
    public Mono<Sponsor> edit(final SponsorDto organizerDto) {

        return editSponsor(organizerDto);
    }

    private Mono<Sponsor> editSponsor(final SponsorDto sponsor) {

        final UUID uuid = sponsor.uuid();
        final OffsetDateTime updatedAt = OffsetDateTime.now();
        final String name = sponsor.name();
        final String website = sponsor.website();
        final Integer financialContribution = sponsor.financialContribution();
        final ContactInformation contactInformation = sponsor.contactInformation();

        final Mono<Long> rowsAffected = databaseClient
                .sql(sponsorQueriesProperties.get(CrudQueriesOperations.EDIT)).bind(0, uuid)
                .bind(1, updatedAt).bind(2, name).bind(3, website).bind(4, financialContribution)
                .bind(5, contactInformation.email()).bind(6, contactInformation.phoneNumber())
                .bind(7, contactInformation.physicalAddress()).bind(8, uuid).fetch().rowsUpdated();

        return rowsAffected.filter(this::rowsAffectedAreMoreThanOne).flatMap(rowNum -> findById(uuid))
                .map(AbstractDomainObject::getCreatedAt)
                .map(monoCreatedAt -> sponsorDtoToSponsorConverter.apply(SponsorDto.builder()
                        .uuid(uuid)
                        .createdAt(monoCreatedAt)
                        .lastUpdated(updatedAt)
                        .name(name)
                        .website(website)
                        .financialContribution(financialContribution)
                        .contactInformation(contactInformation).build()));
    }

    private Mono<Sponsor> saveSponsor(final SponsorDto sponsor) {

        final UUID sponsorUuid = sponsor.uuid();
        final OffsetDateTime now = now.now();

        final UUID uuid = sponsorUuid != null ? sponsorUuid : UUID.randomUUID();
        final String name = sponsor.name();
        final String website = sponsor.website();
        final Integer financialContribution = sponsor.financialContribution();
        final ContactInformation contactInformation = sponsor.contactInformation();

        final Mono<Long> rowsAffected = databaseClient
                .sql(sponsorQueriesProperties.get(CrudQueriesOperations.SAVE)).bind(0, uuid)
                .bind(1, now).bind(2, now).bind(3, name).bind(4, website).bind(5, financialContribution)
                .bind(6, contactInformation.email()).bind(7, contactInformation.phoneNumber())
                .bind(8, contactInformation.physicalAddress()).fetch().rowsUpdated();

        return rowsAffected.filter(this::rowsAffectedAreMoreThanOne)
                .map(rowNum -> sponsorDtoToSponsorConverter.apply(SponsorDto.builder().uuid(uuid).createdAt(now)
                        .lastUpdated(now).name(name).website(website)
                        .financialContribution(financialContribution).contactInformation(contactInformation).build()));
    }

    private boolean rowsAffectedAreMoreThanOne(final Long x) {

        return x >= 1;
    }
}
