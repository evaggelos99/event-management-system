package io.github.evaggelos99.ems.attendee.service;

import io.github.evaggelos99.ems.attendee.api.Attendee;
import io.github.evaggelos99.ems.attendee.api.AttendeeDto;
import io.github.evaggelos99.ems.attendee.api.converters.AttendeeToAttendeeDtoConverter;
import io.github.evaggelos99.ems.attendee.api.repo.IAttendeeRepository;
import io.github.evaggelos99.ems.attendee.service.remote.EventServicePublisher;
import io.github.evaggelos99.ems.attendee.service.remote.TicketLookUpRemoteService;
import io.github.evaggelos99.ems.ticket.api.util.TicketObjectGenerator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

@ExtendWith({SpringExtension.class, MockitoExtension.class})
class AttendeeServiceTest {

    private final IAttendeeRepository attendeeRepository = attendeeRepositoryMock();

    private final Function<Attendee, AttendeeDto> attendeeToAttendeeDtoConverter = new AttendeeToAttendeeDtoConverter();

    @Mock
    private EventServicePublisher eventServiceMock;

    @Mock
    private TicketLookUpRemoteService lookUpTicketServiceMock;

    private AttendeeService service;

    @Test
    void add_addTicket_delete_whenInvokedWithAttendeeDtoAndValidTicket_thenExpectToBeSaved_thenExpectTicketToBeAdded_thenDeleteAttendee() {

        final String firstName = generateString();
        final String lastName = generateString();
        final UUID ticketId = UUID.randomUUID();
        final UUID eventId = UUID.randomUUID();
        final UUID attendeeId = UUID.randomUUID();
        final Instant createdAt = Instant.now();
        final AttendeeDto dto = new AttendeeDto(attendeeId, createdAt, createdAt, firstName, lastName, List.of());
        Mockito.when(lookUpTicketServiceMock.ping()).thenReturn(Mono.just(true));
        Mockito.when(eventServiceMock.ping()).thenReturn(Mono.just(true));
        Mockito.when(lookUpTicketServiceMock.lookUpTicket(ticketId))
            .thenReturn(Mono.just(TicketObjectGenerator.generateTicketDto(null, eventId)));
        Mockito.when(eventServiceMock.addAttendee(eventId, attendeeId)).thenReturn(Mono.just(true));
        Assertions.assertDoesNotThrow(() -> service.add(dto));
        StepVerifier.create(service.addTicket(dto.uuid(), ticketId)).assertNext(Assertions::assertTrue)
            .verifyComplete();
        Assertions.assertDoesNotThrow(() -> service.delete(dto.uuid()));
    }

    @Test
    void add_addTicket_delete_whenInvokedWithAttendeeDtoAndValidTicket_thenExpectToBeSaved_thenExpectTicketToBeNotBeAdded_thenDeleteAttendee() {

        final String firstName = generateString();
        final String lastName = generateString();
        final UUID ticketId = UUID.randomUUID();
        final UUID attendeeId = UUID.randomUUID();
        final Instant createdAt = Instant.now();
        final AttendeeDto dto = new AttendeeDto(attendeeId, createdAt, createdAt, firstName, lastName, List.of());
        Mockito.when(lookUpTicketServiceMock.ping()).thenReturn(Mono.just(true));
        Mockito.when(eventServiceMock.ping()).thenReturn(Mono.just(true));
        Mockito.when(lookUpTicketServiceMock.lookUpTicket(ticketId)).thenReturn(Mono.empty());
        Assertions.assertDoesNotThrow(() -> service.add(dto));
        final Mono<Boolean> ticket = service.addTicket(dto.uuid(), ticketId);
        StepVerifier.create(ticket).assertNext(Assertions::assertFalse).verifyComplete();
        Assertions.assertDoesNotThrow(() -> service.delete(dto.uuid()));
    }

    @Test
    void add_add_getAll_delete_delete_whenInvokedWithAttendeeDtos_thenExpectToBeSavedTwoTimes_thengetAllAttendees_thenDeleteTwo() {

        final String firstName = generateString();
        final String lastName = generateString();
        final UUID expectedUUID = UUID.randomUUID();
        final UUID attendeeId = UUID.randomUUID();
        final Instant createdAt = Instant.now();
        final AttendeeDto dto = new AttendeeDto(attendeeId, createdAt, createdAt, firstName, lastName,
            List.of(expectedUUID));
        final String firstName2 = generateString();
        final String lastName2 = generateString();
        final UUID expectedUUID2 = UUID.randomUUID();
        final UUID attendeeId2 = UUID.randomUUID();
        final AttendeeDto dto2 = new AttendeeDto(attendeeId2, createdAt, createdAt, firstName2, lastName2,
            List.of(expectedUUID2));
        StepVerifier.create(Assertions.assertDoesNotThrow(() -> service.add(dto))).assertNext(x -> {
            Assertions.assertEquals(dto.uuid(), x.getUuid());
        });
        StepVerifier.create(Assertions.assertDoesNotThrow(() -> service.add(dto2))).assertNext(x -> {
            Assertions.assertEquals(dto2.uuid(), x.getUuid());
        });
        final Flux<Attendee> allAttendees = Assertions.assertDoesNotThrow(() -> service.getAll());
        StepVerifier.create(allAttendees).assertNext(Assertions::assertNotNull).assertNext(Assertions::assertNotNull)
            .verifyComplete();
        final Mono<Boolean> firstAttendeeDeletion = Assertions.assertDoesNotThrow(() -> service.delete(dto.uuid()));
        final Mono<Boolean> secondAttendeeDeletion = Assertions.assertDoesNotThrow(() -> service.delete(dto2.uuid()));
        StepVerifier.create(firstAttendeeDeletion).expectNext(true);
        StepVerifier.create(secondAttendeeDeletion).expectNext(true);
    }

    @Test
    void add_delete_existsById_whenInvokedWithAttendeeDtoThenExpectToBeSaved_thenExpectToBeDeleted() {

        final String firstName = generateString();
        final String lastName = generateString();
        final UUID expectedUUID = UUID.randomUUID();
        final AttendeeDto dto = new AttendeeDto(UUID.randomUUID(), Instant.now(), Instant.now(), firstName, lastName,
            List.of(expectedUUID));
        final Mono<Attendee> monoAttendee = Assertions.assertDoesNotThrow(() -> service.add(dto));
        StepVerifier.create(monoAttendee).assertNext(attendee -> {
            Assertions.assertEquals(dto.uuid(), attendee.getUuid());
            Assertions.assertEquals(dto.firstName(), attendee.getFirstName());
            Assertions.assertEquals(dto.lastName(), attendee.getLastName());
            Assertions.assertEquals(dto.ticketIDs(), attendee.getTicketIDs());
        }).verifyComplete();
        StepVerifier.create(Assertions.assertDoesNotThrow(() -> service.delete(dto.uuid()))).expectNext(true)
            .verifyComplete();
        StepVerifier.create(service.existsById(expectedUUID)).expectNext(false).verifyComplete();
    }

    @Test
    void add_edit_delete_whenInvokedWithAttendeeDto_thenExpectToBeSaved_thenEdited_thenDeleted() {

        final String firstName = generateString();
        final String lastName = generateString();
        final UUID expectedUUID = UUID.randomUUID();
        final UUID attendeeId = UUID.randomUUID();
        final Instant createdAt = Instant.now();
        final AttendeeDto dto = new AttendeeDto(attendeeId, createdAt, createdAt, firstName, lastName,
            List.of(expectedUUID));
        final Mono<Attendee> monoAttendee = Assertions.assertDoesNotThrow(() -> service.add(dto));
        final UUID updatedTicketId = UUID.randomUUID();
        final String updatedFirstName = UUID.randomUUID().toString();
        final String updatedLastName = UUID.randomUUID().toString();
        final Instant updatedTimestamp = Instant.now();
        final AttendeeDto newDto = new AttendeeDto(attendeeId, createdAt, updatedTimestamp, updatedFirstName,
            updatedLastName, List.of(updatedTicketId));
        final Mono<Attendee> monoUpdatedAttendee = monoAttendee
            .flatMap(attendee -> service.edit(attendee.getUuid(), newDto));
        StepVerifier.create(monoUpdatedAttendee).assertNext(updatedAttendee -> {
            Assertions.assertEquals(List.of(updatedTicketId), updatedAttendee.getTicketIDs());
            Assertions.assertEquals(updatedFirstName, updatedAttendee.getFirstName());
            Assertions.assertEquals(updatedLastName, updatedAttendee.getLastName());
            Assertions.assertDoesNotThrow(() -> service.delete(dto.uuid()));
        }).verifyComplete();
    }

    @Test
    void add_get_delete_whenInvokedWithAttendeeDtoThenExpectToBeSaved_expectThatCanBeFetched() {

        final String firstName = generateString();
        final String lastName = generateString();
        final UUID expectedUUID = UUID.randomUUID();
        final AttendeeDto dto = new AttendeeDto(UUID.randomUUID(), Instant.now(), Instant.now(), firstName, lastName,
            List.of(expectedUUID));
        final Mono<Attendee> monoAttendee = Assertions.assertDoesNotThrow(() -> service.add(dto));
        StepVerifier.create(monoAttendee.flatMap(attendee -> service.get(attendee.getUuid())))
            .assertNext(Assertions::assertNotNull);
        StepVerifier.create(Assertions.assertDoesNotThrow(() -> service.delete(dto.uuid()))).expectNext(true);
    }

    IAttendeeRepository attendeeRepositoryMock() {

        return new IAttendeeRepository() {

            Map<UUID, Attendee> list = new HashMap<>();

            @Override
            public Mono<Boolean> deleteById(final UUID uuid) {

                return Mono.just(list.remove(uuid) != null);
            }

            @Override
            public Mono<Attendee> edit(final AttendeeDto dto) {

                final Attendee attendee = new Attendee(dto.uuid(), dto.createdAt(), dto.lastUpdated(), dto.firstName(),
                    dto.lastName(), dto.ticketIDs());
                list.put(dto.uuid(), attendee);
                return Mono.just(attendee);
            }

            @Override
            public Mono<Boolean> existsById(final UUID uuid) {

                return Mono.just(list.containsKey(uuid));
            }

            @Override
            public Flux<Attendee> findAll() {

                return Flux.fromIterable(list.values());
            }

            @Override
            public Mono<Attendee> findById(final UUID uuid) {

                return Mono.just(list.get(uuid));
            }

            @Override
            public Mono<Attendee> save(final AttendeeDto dto) {

                final Attendee attendee = new Attendee(dto.uuid(), dto.createdAt(), dto.lastUpdated(), dto.firstName(),
                    dto.lastName(), dto.ticketIDs());
                list.put(dto.uuid(), attendee);
                return Mono.just(attendee);
            }
        };
    }

    @BeforeEach
    void setUp() {

        service = new AttendeeService(attendeeRepository, attendeeToAttendeeDtoConverter, eventServiceMock,
            lookUpTicketServiceMock);
    }

    private String generateString() {

        return UUID.randomUUID().toString();
    }
}
