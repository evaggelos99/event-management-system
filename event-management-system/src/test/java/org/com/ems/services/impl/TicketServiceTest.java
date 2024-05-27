package org.com.ems.services.impl;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

import org.com.ems.api.domainobjects.Ticket;
import org.com.ems.api.dto.TicketDto;
import org.com.ems.db.ITicketRepository;
import org.com.ems.util.RandomObjectGenerator;
import org.com.ems.util.SpringTestConfiguration;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = { SpringTestConfiguration.class })
@ActiveProfiles("service-tests")
class TicketServiceTest {

    @Autowired
    private ITicketRepository ticketRepository;

    private TicketService service;

    @BeforeEach
    void setUp() {

	this.service = new TicketService(this.ticketRepository);

    }

    @Test
    void add_get_getAll_existsById_delete_existsById_invokedWithValidDto_thenExpectToBeAdded_thenExpectItCanBeFetched_thenExpectToFetchAll_thenExpectItExists_thenExpectItCanBeDeleted() {

	final TicketDto dto = RandomObjectGenerator.generateTicketDto(UUID.randomUUID());

	final Ticket ticket = Assertions.assertDoesNotThrow(() -> this.service.add(dto));

	Assertions.assertEquals(dto.uuid(), ticket.getUuid());
	Assertions.assertEquals(dto.eventID(), ticket.getEventID());
	Assertions.assertEquals(dto.ticketType(), ticket.getTicketType());
	Assertions.assertEquals(dto.price(), ticket.getPrice());
	Assertions.assertEquals(dto.transferable(), ticket.getTransferable());
	Assertions.assertEquals(dto.seatInformation(), ticket.getSeatInformation());

	final Optional<Ticket> optionalTicket = this.service.get(dto.uuid());

	Assertions.assertEquals(ticket, optionalTicket.orElseThrow(() -> new AssertionError("Optional is null")));

	Assertions.assertEquals(1, this.service.getAll().size());

	Assertions.assertTrue(() -> this.service.existsById(dto.uuid()));

	Assertions.assertDoesNotThrow(() -> this.service.delete(dto.uuid()));

    }

    @Test
    void existsById_edit_invokedWithInvalidOrganizerId__thenExpectToNotExist_thenExpectToThrow() {

	final UUID ticketId = UUID.randomUUID();
	Assertions.assertFalse(() -> this.service.existsById(ticketId));
	Assertions.assertThrows(NoSuchElementException.class, () -> this.service.edit(ticketId, null));

    }

    @Test
    void add_edit_delete_invokedWithValidDto_thenExpectToBeAdded_thenExpectItCanBeFetched_thenExpectToFetchAll_thenExpectItExists_thenExpectItCanBeDeleted() {

	final TicketDto dto = RandomObjectGenerator.generateTicketDto(UUID.randomUUID());

	final Ticket ticket = Assertions.assertDoesNotThrow(() -> this.service.add(dto));

	final TicketDto newDto = RandomObjectGenerator.generateTicketDto(UUID.randomUUID());

	final Ticket newTicket = Assertions.assertDoesNotThrow(() -> this.service.edit(dto.uuid(), newDto));

	Assertions.assertNotEquals(ticket, newTicket);

	Assertions.assertDoesNotThrow(() -> this.service.delete(dto.uuid()));

    }

}
