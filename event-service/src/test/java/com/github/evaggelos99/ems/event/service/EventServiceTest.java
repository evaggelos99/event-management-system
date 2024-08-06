package com.github.evaggelos99.ems.event.service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.github.evaggelos99.ems.event.api.Event;
import com.github.evaggelos99.ems.event.api.EventDto;
import com.github.evaggelos99.ems.event.api.converters.EventToEventDtoConverter;
import com.github.evaggelos99.ems.event.api.repo.IEventRepository;
import com.github.evaggelos99.ems.event.service.EventService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@ExtendWith({ SpringExtension.class })
class EventServiceTest {

	private final IEventRepository eventRepository = eventRepositoryMock();
	private final Function<Event, EventDto> eventToEventDtoConverter = new EventToEventDtoConverter();

	private EventService service;

	@BeforeEach
	void setUp() {

		service = new EventService(eventRepository, eventToEventDtoConverter);
	}

//	@Test
//	void add_delete_existsById_whenInvokedWithValidEventDto_thenExpectEventToBeCorrectThenDeletedThenNotFetched() {
//
//		// given
//		final UUID attendeeId = UUID.randomUUID();
//		final UUID organizerId = UUID.randomUUID();
//		final UUID sponsorId = UUID.randomUUID();
//		final EventDto eventDto = EventObjectGenerator.generateEventDto(attendeeId, organizerId, sponsorId);
//		// when
//		final Mono<Event> actualResult = Assertions.assertDoesNotThrow(() -> service.add(eventDto));
//		// assert
//		StepVerifier.create(actualResult).assertNext(event -> {
//			assertEquals(eventDto.uuid(), event.getUuid());
//			assertNotNull(event.getCreatedAt());
//			assertNotNull(event.getLastUpdated());
//			assertEquals(eventDto.name(), event.getName());
//			assertEquals(eventDto.place(), event.getPlace());
//			assertEquals(eventDto.eventType(), event.getEventType());
//			assertEquals(eventDto.eventType(), event.getEventType());
//			assertTrue(event.getAttendeesIDs().contains(attendeeId));
//			assertEquals(organizerId, event.getOrganizerID());
//			assertTrue(event.getSponsorsIds().contains(sponsorId));
//			assertEquals(eventDto.startTimeOfEvent(), event.getStartTime());
//			assertEquals(eventDto.duration(), event.getDuration());
//		}).verifyComplete();
//		StepVerifier.create(Assertions.assertDoesNotThrow(() -> service.delete(eventDto.uuid()))).expectNext(true)
//				.verifyComplete();
//		StepVerifier.create(service.existsById(eventDto.uuid())).expectNext(false).verifyComplete();
//	}

//	@Test
//	void add_get_edit_delete_get_whenInvokedWithAttendeeDtoThenExpectToBeSaved_expectThatCanBeFetched() {
//
//		final UUID attendeeId = UUID.randomUUID();
//		final UUID organizerId = UUID.randomUUID();
//		final UUID sponsorId = UUID.randomUUID();
//		final EventDto eventDto = EventObjectGenerator.generateEventDto(attendeeId, organizerId, sponsorId);
//
//		final Mono<Event> actualResult = Assertions.assertDoesNotThrow(() -> service.add(eventDto));
//		StepVerifier.create(actualResult).assertNext(event -> {
//			assertEquals(eventDto.uuid(), event.getUuid());
//			assertNotNull(event.getCreatedAt());
//			assertNotNull(event.getLastUpdated());
//			assertEquals(eventDto.name(), event.getName());
//			assertEquals(eventDto.place(), event.getPlace());
//			assertEquals(eventDto.eventType(), event.getEventType());
//			assertEquals(eventDto.eventType(), event.getEventType());
//			assertTrue(event.getAttendeesIDs().contains(attendeeId));
//			assertEquals(organizerId, event.getOrganizerID());
//			assertTrue(event.getSponsorsIds().contains(sponsorId));
//			assertEquals(eventDto.startTimeOfEvent(), event.getStartTime());
//			assertEquals(eventDto.duration(), event.getDuration());
//		}).verifyComplete();
//
//		final UUID attendeeId2 = UUID.randomUUID();
//		final UUID organizerId2 = UUID.randomUUID();
//		final UUID sponsorId2 = UUID.randomUUID();
//		final EventDto updatedEventDto = EventObjectGenerator.generateEventDto(attendeeId2, organizerId2, sponsorId2);
//
//		final Mono<Event> actualUpdatedResult = Assertions.assertDoesNotThrow(() -> service.edit(,updatedEventDto));
//
//		StepVerifier.create(Assertions.assertDoesNotThrow(() -> service.delete())).expectNext(true);
//	}

	private IEventRepository eventRepositoryMock() {

		return new IEventRepository() {

			Map<UUID, Event> list = new HashMap<>();

			@Override
			public Mono<Event> save(final EventDto dto) {

				final Event event = new Event(dto.uuid(), dto.createdAt(), dto.lastUpdated(), dto.name(), dto.place(),
						dto.eventType(), dto.attendeesIds(), dto.organizerId(), dto.limitOfPeople(), dto.sponsorsIds(),
						dto.startTimeOfEvent(), dto.duration());
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
						dto.startTimeOfEvent(), dto.duration());
				list.put(dto.uuid(), event);

				return Mono.just(event);
			}
		};
	}
}
