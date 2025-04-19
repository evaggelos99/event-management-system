package io.github.evaggelos99.ems.event.service;

import io.github.evaggelos99.ems.common.api.transport.EventStreamPayload;
import io.github.evaggelos99.ems.event.api.Event;
import io.github.evaggelos99.ems.event.api.EventDto;
import io.github.evaggelos99.ems.event.api.EventStreamEntity;
import io.github.evaggelos99.ems.event.api.converters.EventToEventDtoConverter;
import io.github.evaggelos99.ems.event.api.repo.IEventRepository;
import io.github.evaggelos99.ems.event.api.util.EventObjectGenerator;
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
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith({SpringExtension.class})
class EventServiceTest {

    private final IEventRepository eventRepository = eventRepositoryMock();
    private final Function<Event, EventDto> eventToEventDtoConverter = new EventToEventDtoConverter();

    private EventService service;

    @BeforeEach
    void setUp() {

        service = new EventService(eventRepository, eventToEventDtoConverter);
    }

    @Test
    @WithMockUser(roles = {"CREATE_EVENT", "UPDATE_EVENT", "DELETE_EVENT", "READ_EVENT"})
    void add_delete_existsById_whenInvokedWithValidEventDto_thenExpectEventToBeCorrectThenDeletedThenNotFetched() {

        // given
        final UUID attendeeId = UUID.randomUUID();
        final UUID organizerId = UUID.randomUUID();
        final UUID sponsorId = UUID.randomUUID();
        final EventDto eventDto = EventObjectGenerator.generateEventDto(null, attendeeId, organizerId, sponsorId);
        // when
        StepVerifier.create(assertDoesNotThrow(() -> service.add(eventDto)))
                .assertNext(event -> {
                    assertEquals(eventDto.uuid(), event.getUuid());
                    assertNotNull(event.getCreatedAt());
                    assertNotNull(event.getLastUpdated());
                    assertEquals(eventDto.name(), event.getName());
                    assertEquals(eventDto.place(), event.getPlace());
                    assertEquals(eventDto.eventType(), event.getEventType());
                    assertTrue(event.getAttendeesIDs().contains(attendeeId));
                    assertEquals(organizerId, event.getOrganizerID());
                    assertTrue(event.getSponsorsIds().contains(sponsorId));
                    assertEquals(eventDto.startTimeOfEvent(), event.getStartTime());
                    assertEquals(eventDto.duration(), event.getDuration());
                }).verifyComplete();
        StepVerifier.create(assertDoesNotThrow(() -> service.delete(eventDto.uuid()))).expectNext(true)
                .verifyComplete();
        StepVerifier.create(service.existsById(eventDto.uuid())).expectNext(false).verifyComplete();
    }

    @Test
    @WithMockUser(roles = {"CREATE_EVENT", "UPDATE_EVENT", "DELETE_EVENT", "READ_EVENT"})
    void add_get_edit_delete_getAll_whenInvokedWithAttendeeDtoThenExpectToBeSaved_expectThatCanBeFetched() {

        final UUID attendeeId = UUID.randomUUID();
        final UUID organizerId = UUID.randomUUID();
        final UUID sponsorId = UUID.randomUUID();
        final EventDto eventDto = EventObjectGenerator.generateEventDto(null, attendeeId, organizerId, sponsorId);

        StepVerifier.create(assertDoesNotThrow(() -> service.add(eventDto))).assertNext(event -> {
            assertEquals(eventDto.uuid(), event.getUuid());
            assertNotNull(event.getCreatedAt());
            assertNotNull(event.getLastUpdated());
            assertEquals(eventDto.name(), event.getName());
            assertEquals(eventDto.place(), event.getPlace());
            assertEquals(eventDto.eventType(), event.getEventType());
            assertEquals(eventDto.eventType(), event.getEventType());
            assertTrue(event.getAttendeesIDs().contains(attendeeId));
            assertEquals(organizerId, event.getOrganizerID());
            assertTrue(event.getSponsorsIds().contains(sponsorId));
            assertEquals(eventDto.startTimeOfEvent(), event.getStartTime());
            assertEquals(eventDto.duration(), event.getDuration());
        }).verifyComplete();

        StepVerifier.create(service.get(eventDto.uuid())).assertNext(Assertions::assertNotNull).verifyComplete();

        final UUID attendeeId2 = UUID.randomUUID();
        final UUID organizerId2 = UUID.randomUUID();
        final UUID sponsorId2 = UUID.randomUUID();
        final EventDto updatedEventDto = EventObjectGenerator.generateEventDto(eventDto.uuid(), attendeeId2,
                organizerId2, sponsorId2);

        StepVerifier.create(assertDoesNotThrow(() -> service.edit(eventDto.uuid(), updatedEventDto)))
                .assertNext(event -> {

                    assertEquals(updatedEventDto.uuid(), event.getUuid());
                    assertNotNull(event.getCreatedAt());
                    assertNotNull(event.getLastUpdated());
                    assertEquals(updatedEventDto.name(), event.getName());
                    assertEquals(updatedEventDto.place(), event.getPlace());
                    assertEquals(updatedEventDto.eventType(), event.getEventType());
                    assertEquals(updatedEventDto.eventType(), event.getEventType());
                    assertTrue(event.getAttendeesIDs().contains(attendeeId2));
                    assertEquals(organizerId2, event.getOrganizerID());
                    assertTrue(event.getSponsorsIds().contains(sponsorId2));
                    assertEquals(updatedEventDto.startTimeOfEvent(), event.getStartTime());
                    assertEquals(updatedEventDto.duration(), event.getDuration());
                }).verifyComplete();

        StepVerifier.create(assertDoesNotThrow(() -> service.delete(eventDto.uuid()))).expectNext(true)
                .verifyComplete();

        StepVerifier.create(assertDoesNotThrow(() -> service.getAll())).expectNextCount(0).verifyComplete();
    }

    @Test
    @WithMockUser(roles = {"CREATE_EVENT", "UPDATE_EVENT", "DELETE_EVENT", "READ_EVENT"})
    void add_addAttendee_get_whenInvokedWithValidAttendeeDto_ThenExpectAttendeeToBeSaved_ThenExpectToAddAttendeeToAnEventAndReturnTrue_thenExpectToRetrieveTheUpdatedEvent() {

        final UUID eventId = UUID.randomUUID();
        final UUID attendeeId = UUID.randomUUID();
        final UUID attendeeId2 = UUID.randomUUID();
        final UUID organizerId = UUID.randomUUID();
        final UUID sponsorId = UUID.randomUUID();
        final EventDto eventDto = EventObjectGenerator.generateEventDto(eventId, attendeeId, organizerId, sponsorId);

        StepVerifier.create(assertDoesNotThrow(() -> service.add(eventDto))).assertNext(event -> {
            assertEquals(eventDto.uuid(), event.getUuid());
            assertNotNull(event.getCreatedAt());
            assertNotNull(event.getLastUpdated());
            assertEquals(eventDto.name(), event.getName());
            assertEquals(eventDto.place(), event.getPlace());
            assertEquals(eventDto.eventType(), event.getEventType());
            assertEquals(eventDto.eventType(), event.getEventType());
            assertTrue(event.getAttendeesIDs().contains(attendeeId));
            assertEquals(organizerId, event.getOrganizerID());
            assertTrue(event.getSponsorsIds().contains(sponsorId));
            assertEquals(eventDto.startTimeOfEvent(), event.getStartTime());
            assertEquals(eventDto.duration(), event.getDuration());
        }).verifyComplete();

        StepVerifier.create(service.addAttendee(eventId, attendeeId2)).expectNext(true).verifyComplete();

        StepVerifier.create(service.get(eventId)).assertNext(event -> {
            assertTrue(event.getAttendeesIDs().size() == 2);
            assertTrue(event.getAttendeesIDs().contains(attendeeId));
            assertTrue(event.getAttendeesIDs().contains(attendeeId2));
        }).verifyComplete();

    }

    private IEventRepository eventRepositoryMock() {

        return new IEventRepository() {

            @Override
            public Mono<EventStreamEntity> saveOneEventStreamPayload(final EventStreamPayload payload) {
                return null;
            }

            @Override
            public Flux<EventStreamEntity> saveMultipleEventStreamPayload(final List<EventStreamPayload> payload) {
                return null;
            }

            private final Map<UUID, Event> list = new HashMap<>();

            @Override
            public Mono<Event> save(final EventDto dto) {

                final Event event = new Event(dto.uuid(), dto.createdAt(), dto.lastUpdated(), dto.name(), dto.place(),
                        dto.eventType(), dto.attendeesIds(), dto.organizerId(), dto.limitOfPeople(), dto.sponsorsIds(),
                        dto.streamable(), dto.startTimeOfEvent(), dto.duration());
                list.put(dto.uuid(), event);

                return Mono.just(event);
            }

            @Override
            public Mono<Event> findById(final UUID uuid) {

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
            public Flux<Event> findAll() {

                return Flux.fromIterable(list.values());
            }

            @Override
            public Mono<Event> edit(final EventDto dto) {

                final Event event = new Event(dto.uuid(), dto.createdAt(), dto.lastUpdated(), dto.name(), dto.place(),
                        dto.eventType(), dto.attendeesIds(), dto.organizerId(), dto.limitOfPeople(), dto.sponsorsIds(),
                        dto.streamable(), dto.startTimeOfEvent(), dto.duration());
                list.put(dto.uuid(), event);

                return Mono.just(event);
            }
        };
    }
}
