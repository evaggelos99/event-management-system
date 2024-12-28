package io.github.evaggelos99.ems.ticket.service;

import io.github.evaggelos99.ems.ticket.api.Ticket;
import io.github.evaggelos99.ems.ticket.api.TicketDto;
import io.github.evaggelos99.ems.ticket.api.repo.ITicketRepository;
import io.github.evaggelos99.ems.ticket.api.util.TicketObjectGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith({SpringExtension.class})
class TicketServiceTest {

    private final ITicketRepository ticketRepository = ticketRepositoryMock();

    private TicketService service;

    @BeforeEach
    void setUp() {

        service = new TicketService(ticketRepository);
    }

    @Test
    void add_delete_existsById_whenInvokedWithValidTicketDto_thenExpectEventToBeCorrectThenDeletedThenNotFetched() {

        final UUID eventId = UUID.randomUUID();
        final TicketDto ticketDto = TicketObjectGenerator.generateTicketDto(null, eventId);

        final Mono<Ticket> actualResult = assertDoesNotThrow(() -> service.add(ticketDto));

        StepVerifier.create(actualResult).assertNext(ticket -> {
            assertEquals(ticketDto.uuid(), ticket.getUuid());
            assertNotNull(ticket.getCreatedAt());
            assertNotNull(ticket.getLastUpdated());
            assertEquals(ticketDto.eventID(), ticket.getEventID());
            assertEquals(ticketDto.ticketType(), ticket.getTicketType());
            assertEquals(ticketDto.price(), ticket.getPrice());
            assertEquals(ticketDto.transferable(), ticket.getTransferable());
            assertEquals(ticketDto.seatInformation(), ticket.getSeatingInformation());
        }).verifyComplete();

        StepVerifier.create(assertDoesNotThrow(() -> service.delete(ticketDto.uuid()))).expectNext(true)
                .verifyComplete();

        StepVerifier.create(service.existsById(ticketDto.uuid())).expectNext(false).verifyComplete();

    }

    @Test
    void add_get_edit_delete_getAll_whenInvokedWithAttendeeDtoThenExpectToBeSaved_expectThatCanBeFetched() {

        final UUID eventId = UUID.randomUUID();
        final TicketDto ticketDto = TicketObjectGenerator.generateTicketDto(null, eventId);

        final Mono<Ticket> actualResult = assertDoesNotThrow(() -> service.add(ticketDto));

        StepVerifier.create(actualResult).assertNext(ticket -> {
            assertEquals(ticketDto.uuid(), ticket.getUuid());
            assertNotNull(ticket.getCreatedAt());
            assertNotNull(ticket.getLastUpdated());
            assertEquals(ticketDto.eventID(), ticket.getEventID());
            assertEquals(ticketDto.ticketType(), ticket.getTicketType());
            assertEquals(ticketDto.price(), ticket.getPrice());
            assertEquals(ticketDto.transferable(), ticket.getTransferable());
            assertEquals(ticketDto.seatInformation(), ticket.getSeatingInformation());
        }).verifyComplete();

        StepVerifier.create(service.get(ticketDto.uuid())).expectNext(actualResult.block()).verifyComplete();

        final TicketDto updatedTicketDto = TicketObjectGenerator.generateTicketDto(ticketDto.uuid(), eventId);

        StepVerifier.create(assertDoesNotThrow(() -> service.edit(ticketDto.uuid(), updatedTicketDto)))
                .assertNext(event -> {
                    assertEquals(updatedTicketDto.uuid(), event.getUuid());
                    assertNotNull(event.getCreatedAt());
                    assertNotNull(event.getLastUpdated());
                    assertEquals(updatedTicketDto.eventID(), event.getEventID());
                    assertEquals(updatedTicketDto.ticketType(), event.getTicketType());
                    assertEquals(updatedTicketDto.price(), event.getPrice());
                    assertEquals(updatedTicketDto.transferable(), event.getTransferable());
                    assertEquals(updatedTicketDto.seatInformation(), event.getSeatingInformation());
                }).verifyComplete();

        StepVerifier.create(assertDoesNotThrow(() -> service.delete(ticketDto.uuid()))).expectNext(true)
                .verifyComplete();

        StepVerifier.create(assertDoesNotThrow(() -> service.getAll())).expectNextCount(0).verifyComplete();

    }

    private ITicketRepository ticketRepositoryMock() {

        return new ITicketRepository() {

            Map<UUID, Ticket> list = new HashMap<>();

            @Override
            public Mono<Ticket> save(final TicketDto dto) {

                final Ticket sponsor = new Ticket(dto.uuid(), Instant.now(), Instant.now(), dto.eventID(),
                        dto.ticketType(), dto.price(), dto.transferable(), dto.seatInformation());

                list.put(dto.uuid(), sponsor);

                return Mono.just(sponsor);
            }

            @Override
            public Mono<Ticket> findById(final UUID uuid) {

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
            public Flux<Ticket> findAll() {

                return Flux.fromIterable(list.values());
            }

            @Override
            public Mono<Ticket> edit(final TicketDto dto) {

                final Ticket sponsor = new Ticket(dto.uuid(), dto.createdAt(), dto.lastUpdated(), dto.eventID(),
                        dto.ticketType(), dto.price(), dto.transferable(), dto.seatInformation());

                list.put(dto.uuid(), sponsor);

                return Mono.just(sponsor);
            }
        };
    }

}
