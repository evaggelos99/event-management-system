package io.github.evaggelos99.ems.ticket.service.controller;

import io.github.evaggelos99.ems.ticket.api.TicketDto;
import io.github.evaggelos99.ems.ticket.api.util.TicketObjectGenerator;
import io.github.evaggelos99.ems.ticket.service.TicketServiceApplication;
import io.github.evaggelos99.ems.ticket.service.util.SqlScriptExecutor;
import io.github.evaggelos99.ems.ticket.service.util.TestConfiguration;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = {TicketServiceApplication.class,
        TestConfiguration.class}, webEnvironment = WebEnvironment.RANDOM_PORT)
@TestInstance(Lifecycle.PER_CLASS)
class TicketControllerIntegrationTest {

    private static final String HOSTNAME = "http://localhost:";
    private static final String RELATIVE_ENDPOINT = "/ticket";
    private final RestTemplate restTemplate = new RestTemplate();
    @Autowired
    private SqlScriptExecutor sqlScriptExecutor;
    @LocalServerPort
    private int port;

    @BeforeAll
    public void beforeAll() {

        sqlScriptExecutor.setup();
    }

    @Test
    @WithMockUser(roles = {"CREATE_TICKET", "UPDATE_TICKET", "DELETE_TICKET", "READ_TICKET"})
    void postTicket_getTicket_deleteTicket_getTicket_whenInvokedWithValidTicketDto_thenExpectForTicketToBeAddedFetchedAndDeleted() {

        final OffsetDateTime currentTime = OffsetDateTime.now();
        final UUID eventId = UUID.randomUUID();
        final TicketDto dto = TicketObjectGenerator.generateTicketDtoWithoutTimestamps(null, eventId);

        final ResponseEntity<TicketDto> actualEntity = restTemplate.postForEntity(createUrl(), dto, TicketDto.class);
        assertTrue(actualEntity.getStatusCode().is2xxSuccessful());
        final TicketDto actualDto = actualEntity.getBody();

        assertNotNull(actualDto);
        assertEquals(dto.uuid(), actualDto.uuid());
        assertTrue(actualDto.createdAt().isAfter(currentTime));
        assertTrue(actualDto.lastUpdated().isAfter(currentTime));
        assertEquals(dto.eventID(), actualDto.eventID());
        assertEquals(dto.ticketType(), actualDto.ticketType());
        assertEquals(dto.price(), actualDto.price());
        assertEquals(dto.transferable(), actualDto.transferable());
        assertEquals(dto.seatInformation(), actualDto.seatInformation());

        final ResponseEntity<TicketDto> actualGetEntity = restTemplate
                .getForEntity(createUrl() + "/" + actualDto.uuid(), TicketDto.class);

        assertTrue(actualGetEntity.getStatusCode().is2xxSuccessful());
        final TicketDto getDto = actualGetEntity.getBody();
        assertNotNull(getDto);
        assertEquals(actualDto.uuid(), getDto.uuid());
        assertEquals(actualDto.createdAt().truncatedTo(ChronoUnit.MILLIS),
                getDto.createdAt().truncatedTo(ChronoUnit.MILLIS)); // for Github action tests
        assertEquals(actualDto.lastUpdated().truncatedTo(ChronoUnit.MILLIS),
                getDto.lastUpdated().truncatedTo(ChronoUnit.MILLIS)); // for Github action tests
        assertEquals(actualDto.eventID(), getDto.eventID());
        assertEquals(actualDto.ticketType(), getDto.ticketType());
        assertEquals(actualDto.price(), getDto.price());
        assertEquals(actualDto.transferable(), getDto.transferable());
        assertEquals(actualDto.seatInformation(), getDto.seatInformation());

        restTemplate.delete(createUrl() + "/{uuid}", actualDto.uuid());

        final ResponseEntity<TicketDto> deletedDto = restTemplate.getForEntity(createUrl() + "/" + actualDto.uuid(),
                TicketDto.class);
        assertTrue(deletedDto.getStatusCode().is2xxSuccessful());
        assertNull(deletedDto.getBody());
    }

    private String createUrl() {

        return HOSTNAME + port + RELATIVE_ENDPOINT;
    }

    @Test
    @WithMockUser(roles = {"CREATE_TICKET", "UPDATE_TICKET", "DELETE_TICKET", "READ_TICKET"})
    void postTicket_putTicket_deleteTicket_getAll_whenInvokedWithValidTicketDto_thenExpectForTicketToBeAddedThenEditedThenDeleted() {

        final OffsetDateTime currentTime = OffsetDateTime.now();
        final UUID eventId = UUID.randomUUID();
        final UUID ticketId = UUID.randomUUID();
        final TicketDto dto = TicketObjectGenerator.generateTicketDtoWithoutTimestamps(ticketId, eventId);

        final ResponseEntity<TicketDto> actualEntity = restTemplate.postForEntity(createUrl(), dto, TicketDto.class);
        assertTrue(actualEntity.getStatusCode().is2xxSuccessful());
        final TicketDto actualDto = actualEntity.getBody();

        assertNotNull(actualDto);
        assertEquals(dto.uuid(), actualDto.uuid());
        assertTrue(actualDto.createdAt().isAfter(currentTime));
        assertTrue(actualDto.lastUpdated().isAfter(currentTime));
        assertEquals(dto.eventID(), actualDto.eventID());
        assertEquals(dto.ticketType(), actualDto.ticketType());
        assertEquals(dto.price(), actualDto.price());
        assertEquals(dto.transferable(), actualDto.transferable());
        assertEquals(dto.seatInformation(), actualDto.seatInformation());

        final TicketDto updatedDto = TicketObjectGenerator.generateTicketDtoWithoutTimestamps(ticketId, eventId);

        final ResponseEntity<TicketDto> actualPutEntity = restTemplate.exchange(createUrl() + "/" + actualDto.uuid(),
                HttpMethod.PUT, createHttpEntity(updatedDto), TicketDto.class);
        final TicketDto actualPutDto = actualPutEntity.getBody();
        // assert
        assertTrue(actualPutEntity.getStatusCode().is2xxSuccessful());
        assertNotNull(actualPutDto);
        assertEquals(updatedDto.uuid(), actualPutDto.uuid());
        assertEquals(actualDto.createdAt().truncatedTo(ChronoUnit.MILLIS),
                actualPutDto.createdAt().truncatedTo(ChronoUnit.MILLIS));
        assertTrue(actualPutDto.lastUpdated().isAfter(actualDto.lastUpdated()));
        assertEquals(updatedDto.eventID(), actualPutDto.eventID());
        assertEquals(updatedDto.ticketType(), actualPutDto.ticketType());
        assertEquals(updatedDto.price(), actualPutDto.price());
        assertEquals(updatedDto.transferable(), actualPutDto.transferable());
        assertEquals(updatedDto.seatInformation(), actualPutDto.seatInformation());

        // deleteTicket
        restTemplate.delete(createUrl() + "/{uuid}", actualDto.uuid());
        // assertThat the list returned is empty
        @SuppressWarnings("rawtypes") final ResponseEntity<List> listOfTickets = restTemplate.getForEntity(createUrl(), List.class);
        assertTrue(listOfTickets.getStatusCode().is2xxSuccessful());
        final List<?> body = listOfTickets.getBody();
        assertNotNull(body);
        assertTrue(body.isEmpty());
    }

    @SuppressWarnings({"all"})
    private HttpEntity createHttpEntity(final TicketDto updatedDto) {

        return new HttpEntity(updatedDto);
    }

    @Test
    @WithMockUser(roles = {"READ_TICKET"})
    void postTicketWithWrongRole() {

        final TicketDto dto = TicketObjectGenerator.generateTicketDtoWithoutTimestamps(UUID.randomUUID(), UUID.randomUUID());
        try {
            restTemplate.postForEntity(createUrl(), dto, TicketDto.class);

        } catch (HttpClientErrorException.Forbidden e) {
            return;
        }

        throw new AssertionError("The request status is not 403");
    }

    @Test
    void postTicketWithNoRole() {

        final TicketDto dto = TicketObjectGenerator.generateTicketDto(UUID.randomUUID(), UUID.randomUUID());
        try {
            restTemplate.postForEntity(createUrl(), dto, TicketDto.class);
        } catch (HttpClientErrorException.Unauthorized e) {
            return;
        }
        throw new AssertionError("The request status is not 401");
    }
}
