package io.github.evaggelos99.ems.organizer.service;

import io.github.evaggelos99.ems.common.api.domainobjects.EventType;
import io.github.evaggelos99.ems.organizer.api.repo.IOrganizerRepository;
import io.github.evaggelos99.ems.organizer.api.Organizer;
import io.github.evaggelos99.ems.organizer.api.OrganizerDto;
import io.github.evaggelos99.ems.organizer.api.util.OrganizerObjectGenerator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
class OrganizerServiceTest {

    private final IOrganizerRepository organizerRepository = organizerRepositoryMock();

    private OrganizerService service;

    @BeforeEach
    void setUp() {

        service = new OrganizerService(organizerRepository);
    }

    @Test
    @WithMockUser(roles = {"CREATE_ORGANIZER", "UPDATE_ORGANIZER", "DELETE_ORGANIZER", "READ_ORGANIZER"})
    void add_edit_delete_existsById_whenInvokedWithValidOrganizerDto_thenExpectOrganizerToBeCorrectThenDeletedThenNotFetched() {

        final OrganizerDto organizerDto = OrganizerObjectGenerator.generateOrganizerDto(null, EventType.OTHER);

        StepVerifier.create(service.add(organizerDto)).assertNext(organizer -> {
            assertEquals(organizerDto.uuid(), organizer.getUuid());
            assertNotNull(organizer.getCreatedAt());
            assertNotNull(organizer.getLastUpdated());
            assertEquals(organizerDto.name(), organizer.getName());
            assertEquals(organizerDto.website(), organizer.getWebsite());
            assertTrue(organizerDto.eventTypes().contains(EventType.OTHER));
            assertEquals(organizerDto.information(), organizer.getInformation());
            assertEquals(organizerDto.contactInformation(), organizer.getContactInformation());
        }).verifyComplete();

        final OrganizerDto updatedDto = OrganizerObjectGenerator.generateOrganizerDto(organizerDto.uuid(), EventType.CONFERENCE);

        StepVerifier.create(service.edit(updatedDto.uuid(), updatedDto)).assertNext(organizer -> {
            assertEquals(updatedDto.uuid(), organizer.getUuid());
            assertNotNull(organizer.getCreatedAt());
            assertNotNull(organizer.getLastUpdated());
            assertEquals(updatedDto.name(), organizer.getName());
            assertEquals(updatedDto.website(), organizer.getWebsite());
            assertTrue(updatedDto.eventTypes().contains(EventType.CONFERENCE));
            assertEquals(updatedDto.information(), organizer.getInformation());
            assertEquals(updatedDto.contactInformation(), organizer.getContactInformation());
        }).verifyComplete();

        StepVerifier.create(service.delete(organizerDto.uuid())).expectNext(true).verifyComplete();

        StepVerifier.create(service.existsById(organizerDto.uuid())).expectNext(false).verifyComplete();
    }

    @Test
    @WithMockUser(roles = {"CREATE_ORGANIZER", "UPDATE_ORGANIZER", "DELETE_ORGANIZER", "READ_ORGANIZER"})
    void add_get_getAll_delete_whenInvokedWithValidOrganizerDto_thenExpectOrganizerToBeFetchedThenFetchAllAvailableOrganizersThenDeleted() {

        final UUID organizerId = UUID.randomUUID();
        final OrganizerDto organizerDto = OrganizerObjectGenerator.generateOrganizerDto(organizerId, EventType.CONFERENCE);

        StepVerifier.create(service.add(organizerDto)).assertNext(organizer -> {
            assertEquals(organizerDto.uuid(), organizer.getUuid());
            assertNotNull(organizer.getCreatedAt());
            assertNotNull(organizer.getLastUpdated());
            assertEquals(organizerDto.name(), organizer.getName());
            assertEquals(organizerDto.website(), organizer.getWebsite());
            assertTrue(organizerDto.eventTypes().contains(EventType.CONFERENCE));
            assertEquals(organizerDto.information(), organizer.getInformation());
            assertEquals(organizerDto.contactInformation(), organizer.getContactInformation());
        }).verifyComplete();

        StepVerifier.create(service.get(organizerId)).assertNext(organizer -> {

            assertEquals(organizerDto.uuid(), organizer.getUuid());
            assertNotNull(organizer.getCreatedAt());
            assertNotNull(organizer.getLastUpdated());
            assertEquals(organizerDto.name(), organizer.getName());
            assertEquals(organizerDto.website(), organizer.getWebsite());
            assertTrue(organizerDto.eventTypes().contains(EventType.CONFERENCE));
            assertEquals(organizerDto.information(), organizer.getInformation());
            assertEquals(organizerDto.contactInformation(), organizer.getContactInformation());
        }).verifyComplete();

        StepVerifier.create(service.getAll()).assertNext(Assertions::assertNotNull).expectNextCount(0).verifyComplete();
        StepVerifier.create(service.delete(organizerDto.uuid())).expectNext(true).verifyComplete();
    }

    private IOrganizerRepository organizerRepositoryMock() {

        return new IOrganizerRepository() {

            private final Map<UUID, Organizer> list = new HashMap<>();

            @Override
            public Mono<Organizer> save(final OrganizerDto dto) {

                final Organizer organizer = new Organizer(dto.uuid(), dto.createdAt(), dto.lastUpdated(), dto.name(),
                        dto.website(), dto.information(),dto.eventTypes(), dto.contactInformation());
                list.put(dto.uuid(), organizer);
                return Mono.just(organizer);
            }

            @Override
            public Mono<Organizer> findById(final UUID uuid) {

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
            public Flux<Organizer> findAll() {

                return Flux.fromIterable(list.values());
            }

            @Override
            public Mono<Organizer> edit(final OrganizerDto dto) {

                final Organizer organizer = new Organizer(dto.uuid(), dto.createdAt(), dto.lastUpdated(), dto.name(),
                        dto.website(), dto.information(),dto.eventTypes(), dto.contactInformation());
                list.put(dto.uuid(), organizer);
                return Mono.just(organizer);
            }
        };
    }

}
