package io.github.evaggelos99.ems.sponsor.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import io.github.evaggelos99.ems.sponsor.api.Sponsor;
import io.github.evaggelos99.ems.sponsor.api.SponsorDto;
import io.github.evaggelos99.ems.sponsor.api.repo.ISponsorRepository;
import io.github.evaggelos99.ems.sponsor.api.util.SponsorObjectGenerator;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith({ SpringExtension.class })
class SponsorServiceTest {

	private final ISponsorRepository sponsorRepository = sponsorRepositoryMock();

	private SponsorService service;

	@BeforeEach
	void setUp() {

		service = new SponsorService(sponsorRepository);
	}

	@Test
	void add_edit_delete_existsById_whenInvokedWithValidSponsorDto_thenExpectSponsorToBeCorrectThenDeletedThenNotFetched() {

		final UUID sponsorId = UUID.randomUUID();
		final SponsorDto sponsorDto = SponsorObjectGenerator.generateSponsorDto(sponsorId);

		StepVerifier.create(service.add(sponsorDto)).assertNext(sponsor -> {

			assertEquals(sponsorDto.uuid(), sponsor.getUuid());
			assertNotNull(sponsor.getCreatedAt());
			assertNotNull(sponsor.getLastUpdated());
			assertEquals(sponsorDto.name(), sponsor.getName());
			assertEquals(sponsorDto.website(), sponsor.getWebsite());
			assertEquals(sponsorDto.financialContribution(), sponsor.getFinancialContribution());
			assertEquals(sponsorDto.contactInformation(), sponsor.getContactInformation());
		}).verifyComplete();

		final SponsorDto updatedDto = SponsorObjectGenerator.generateSponsorDto(sponsorId);

		StepVerifier.create(service.edit(sponsorId, updatedDto)).assertNext(sponsor -> {

			assertEquals(updatedDto.uuid(), sponsor.getUuid());
			assertNotNull(sponsor.getCreatedAt());
			assertNotNull(sponsor.getLastUpdated());
			assertEquals(updatedDto.name(), sponsor.getName());
			assertEquals(updatedDto.website(), sponsor.getWebsite());
			assertEquals(updatedDto.financialContribution(), sponsor.getFinancialContribution());
			assertEquals(updatedDto.contactInformation(), sponsor.getContactInformation());
		}).verifyComplete();

		StepVerifier.create(service.delete(sponsorDto.uuid())).expectNext(true).verifyComplete();

		StepVerifier.create(service.existsById(sponsorDto.uuid())).expectNext(false).verifyComplete();

	}

	@Test
	void add_get_getAll_delete_whenInvokedWithValidSponsorDto_thenExpectSponsorToBeFetchedThenFetchAllAvailableSponsorsThenDeleted() {

		final UUID sponsorId = UUID.randomUUID();
		final SponsorDto sponsorDto = SponsorObjectGenerator.generateSponsorDto(sponsorId);

		StepVerifier.create(service.add(sponsorDto)).assertNext(sponsor -> {

			assertEquals(sponsorDto.uuid(), sponsor.getUuid());
			assertNotNull(sponsor.getCreatedAt());
			assertNotNull(sponsor.getLastUpdated());
			assertEquals(sponsorDto.name(), sponsor.getName());
			assertEquals(sponsorDto.website(), sponsor.getWebsite());
			assertEquals(sponsorDto.financialContribution(), sponsor.getFinancialContribution());
			assertEquals(sponsorDto.contactInformation(), sponsor.getContactInformation());
		}).verifyComplete();

		StepVerifier.create(service.get(sponsorId)).assertNext(sponsor -> {

			assertEquals(sponsorDto.uuid(), sponsor.getUuid());
			assertNotNull(sponsor.getCreatedAt());
			assertNotNull(sponsor.getLastUpdated());
			assertEquals(sponsorDto.name(), sponsor.getName());
			assertEquals(sponsorDto.website(), sponsor.getWebsite());
			assertEquals(sponsorDto.financialContribution(), sponsor.getFinancialContribution());
			assertEquals(sponsorDto.contactInformation(), sponsor.getContactInformation());
		}).verifyComplete();

		StepVerifier.create(service.getAll()).assertNext(sponsor -> {
			assertNotNull(sponsor);
		}).expectNextCount(0).verifyComplete();

		StepVerifier.create(service.delete(sponsorDto.uuid())).expectNext(true).verifyComplete();

	}

	private ISponsorRepository sponsorRepositoryMock() {

		return new ISponsorRepository() {

			Map<UUID, Sponsor> list = new HashMap<>();

			@Override
			public Mono<Sponsor> save(final SponsorDto dto) {

				final Sponsor sponsor = new Sponsor(dto.uuid(), dto.createdAt(), dto.lastUpdated(), dto.name(),
						dto.website(), dto.financialContribution(), dto.contactInformation());

				list.put(dto.uuid(), sponsor);

				return Mono.just(sponsor);
			}

			@Override
			public Mono<Sponsor> findById(final UUID uuid) {

				if (!list.containsKey(uuid)) {
					return Mono.empty();
				}

				return Mono.just(list.get(uuid));
			}

			@Override
			public Mono<Boolean> deleteById(final UUID uuid) {

				return Mono.just(list.remove(uuid) != null);
			}

			@Override
			public Mono<Boolean> existsById(final UUID uuid) {

				return Mono.just(list.containsKey(uuid));
			}

			@Override
			public Flux<Sponsor> findAll() {

				return Flux.fromIterable(list.values());
			}

			@Override
			public Mono<Sponsor> edit(final SponsorDto dto) {

				final Sponsor sponsor = new Sponsor(dto.uuid(), dto.createdAt(), dto.lastUpdated(), dto.name(),
						dto.website(), dto.financialContribution(), dto.contactInformation());

				list.put(dto.uuid(), sponsor);

				return Mono.just(sponsor);
			}
		};
	}

}
