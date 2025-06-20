package io.github.evaggelos99.ems.attendee.service.controller;

import io.github.evaggelos99.ems.attendee.api.AttendeeDto;
import io.github.evaggelos99.ems.attendee.api.util.AttendeeObjectGenerator;
import io.github.evaggelos99.ems.attendee.service.AttendeeServiceApplication;
import io.github.evaggelos99.ems.attendee.service.EmailService;
import io.github.evaggelos99.ems.attendee.service.remote.TicketLookUpRemoteService;
import io.github.evaggelos99.ems.attendee.service.remote.UserLookUpRemoteService;
import io.github.evaggelos99.ems.attendee.service.util.SqlScriptExecutor;
import io.github.evaggelos99.ems.attendee.service.util.TestConfiguration;
import io.github.evaggelos99.ems.common.api.domainobjects.UserRole;
import io.github.evaggelos99.ems.testcontainerkafka.lib.ExtendedKafkaContainer;
import io.github.evaggelos99.ems.ticket.api.TicketDto;
import io.github.evaggelos99.ems.ticket.api.util.TicketObjectGenerator;
import io.github.evaggelos99.ems.user.api.UserDto;
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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.testcontainers.containers.wait.strategy.LogMessageWaitStrategy;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = {AttendeeServiceApplication.class, TestConfiguration.class},
        properties = "spring.main.allow-bean-definition-overriding=true",
        webEnvironment = WebEnvironment.RANDOM_PORT)
@TestInstance(Lifecycle.PER_CLASS)
@Testcontainers
@ActiveProfiles("test")
class AttendeeControllerIntegrationTest {

    private static final String HOSTNAME = "http://localhost:";
    private static final String RELATIVE_ENDPOINT = "/attendee";
    @Container
    private static final ExtendedKafkaContainer KAFKA = new ExtendedKafkaContainer();
    private final RestTemplate restTemplate = new RestTemplate();
    @Autowired
    private SqlScriptExecutor sqlScriptExecutor;
    @LocalServerPort
    private int port;
    @MockitoBean
    private TicketLookUpRemoteService ticketLookUpRemoteServiceMock;
    @MockitoBean
    private UserLookUpRemoteService userLookUpRemoteServiceMock;
    @MockitoBean
    private EmailService emailServiceMock;

    static {

        KAFKA.start();
    }

    @DynamicPropertySource
    static void configureKafkaProperties(DynamicPropertyRegistry registry) {

        registry.add("spring.kafka.producer.bootstrap-servers", () -> KAFKA.getHost() + ":" + KAFKA.getFirstMappedPort());
    }

    @Test
    void postAttendeeWithNoRole() {

        final AttendeeDto dto = AttendeeObjectGenerator.generateAttendeeDto(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID());
        try {
            restTemplate.postForEntity(createUrl(), dto, AttendeeDto.class);
        } catch (HttpClientErrorException.Unauthorized e) {
            return;
        }
        throw new AssertionError("The request status is not 401");
    }

    @BeforeAll
    void beforeAll() {

        sqlScriptExecutor.setup("migration/h2-schema.sql");
    }

    @Test
    @WithMockUser(roles = {"CREATE_ATTENDEE", "UPDATE_ATTENDEE", "DELETE_ATTENDEE", "READ_ATTENDEE"})
    void postAttendee_getAttendee_deleteAttendee_getAttendee_whenInvokedWithValidAttendeeDto_thenExpectForAttendeeToBeAddedFetchedAndDeleted() {

        final OffsetDateTime currentTime = OffsetDateTime.now();
        final AttendeeDto dto = AttendeeObjectGenerator.generateAttendeeDtoWithoutTimestamps(null);
        // postAttendee
        final ResponseEntity<AttendeeDto> actualEntity = restTemplate.postForEntity(createUrl(), dto, AttendeeDto.class);
        assertTrue(actualEntity.getStatusCode().is2xxSuccessful());
        final AttendeeDto actualDto = actualEntity.getBody();
        // assert
        assertNotNull(actualDto);
        assertEquals(dto.uuid(), actualDto.uuid());
        assertTrue(actualDto.createdAt().isAfter(currentTime));
        assertTrue(actualDto.lastUpdated().isAfter(currentTime));
        assertEquals(dto.firstName(), actualDto.firstName());
        assertEquals(dto.lastName(), actualDto.lastName());
        assertTrue(actualDto.ticketIDs().isEmpty());
        // getAttendee
        final ResponseEntity<AttendeeDto> actualGetEntity = restTemplate.getForEntity(createUrl() + "/{uuid}", AttendeeDto.class, actualDto.uuid());
        // assert
        assertTrue(actualGetEntity.getStatusCode().is2xxSuccessful());
        final AttendeeDto getDto = actualGetEntity.getBody();
        assertNotNull(getDto);
        assertEquals(actualDto.uuid(), getDto.uuid());
        assertEquals(actualDto.createdAt().truncatedTo(ChronoUnit.MILLIS), getDto.createdAt()
                .truncatedTo(ChronoUnit.MILLIS)); // for Github action tests
        assertEquals(actualDto.lastUpdated().truncatedTo(ChronoUnit.MILLIS), getDto.lastUpdated()
                .truncatedTo(ChronoUnit.MILLIS)); // for Github action tests
        assertEquals(actualDto.firstName(), getDto.firstName());
        assertEquals(actualDto.lastName(), getDto.lastName());
        assertEquals(actualDto.ticketIDs(), getDto.ticketIDs());

        // deleteAttendee
        restTemplate.delete(createUrl() + "/{uuid}", actualDto.uuid());
        // assertThat it cannot be found
        final ResponseEntity<AttendeeDto> deletedDto = restTemplate.getForEntity(createUrl() + "/{uuid}", AttendeeDto.class, actualDto.uuid());
        assertTrue(deletedDto.getStatusCode().is2xxSuccessful());
        assertNull(deletedDto.getBody());
    }

    @Test
    @WithMockUser(roles = {"CREATE_ATTENDEE", "UPDATE_ATTENDEE", "DELETE_ATTENDEE", "READ_ATTENDEE"})
    void postAttendee_putAttendee_getAttendee_deleteAttendee_getAll_whenInvokedWithValidAttendeeDto_thenExpectForAttendeeToBeAddedThenEditedThenDeleted() {

        final OffsetDateTime currentTime = OffsetDateTime.now();
        final UUID ticketId = UUID.randomUUID();
        final AttendeeDto dto = AttendeeObjectGenerator.generateAttendeeDtoWithoutTimestamps(null, ticketId);
        // postAttendee
        final ResponseEntity<AttendeeDto> actualEntity = restTemplate.postForEntity(createUrl(), dto, AttendeeDto.class);

        assertTrue(actualEntity.getStatusCode().is2xxSuccessful());
        final AttendeeDto actualDto = actualEntity.getBody();
        // assert
        assertNotNull(actualDto);
        assertEquals(dto.uuid(), actualDto.uuid());
        assertTrue(actualDto.createdAt().isAfter(currentTime));
        assertTrue(actualDto.lastUpdated().isAfter(currentTime));
        assertEquals(dto.firstName(), actualDto.firstName());
        assertEquals(dto.lastName(), actualDto.lastName());
        assertEquals(1, actualDto.ticketIDs().size());
        assertTrue(actualDto.ticketIDs().contains(ticketId));
        // putAttendee
        final UUID differentTicketId = UUID.randomUUID();
        final AttendeeDto updatedDto = AttendeeObjectGenerator.generateAttendeeDtoWithoutTimestamps(actualDto.uuid(), differentTicketId);
        final ResponseEntity<AttendeeDto> actualPutEntity = restTemplate.exchange(createUrl() + "/{uuid}", HttpMethod.PUT, createHttpEntity(updatedDto), AttendeeDto.class, actualDto.uuid());
        final AttendeeDto actualPutDto = actualPutEntity.getBody();
        // assert
        assertTrue(actualPutEntity.getStatusCode().is2xxSuccessful());
        assertNotNull(actualPutDto);
        assertEquals(actualPutDto.uuid(), actualDto.uuid());
        assertEquals(actualDto.createdAt().truncatedTo(ChronoUnit.MILLIS), actualPutDto.createdAt()
                .truncatedTo(ChronoUnit.MILLIS));
        assertTrue(actualPutDto.lastUpdated().isAfter(actualDto.lastUpdated()));
        assertEquals(updatedDto.firstName(), actualPutDto.firstName());
        assertEquals(updatedDto.lastName(), actualPutDto.lastName());
        assertTrue(updatedDto.ticketIDs().size() == 1);
        assertTrue(updatedDto.ticketIDs().contains(differentTicketId));
        // deleteAttendee
        restTemplate.delete(createUrl() + "/" + actualDto.uuid());
        // assertThat the list returned is empty
        @SuppressWarnings("rawtypes") final ResponseEntity<List> listOfAttendeeDtos = restTemplate.getForEntity(createUrl(), List.class);
        assertTrue(listOfAttendeeDtos.getStatusCode().is2xxSuccessful());
        final List<?> body = listOfAttendeeDtos.getBody();
        assertNotNull(body);
        assertTrue(body.isEmpty());
    }

    @Test
    @WithMockUser(roles = {"CREATE_ATTENDEE", "UPDATE_ATTENDEE", "DELETE_ATTENDEE", "READ_ATTENDEE"})
    void postAttendee_addTicket_deleteAttendee_whenInvokedWithValidAttendeeDto_thenExpectForAttendeeToBeAddedThenEditedWithAddTicketThenDeleted() {

        final OffsetDateTime currentTime = OffsetDateTime.now();
        final UUID ticketId = UUID.randomUUID();
        final AttendeeDto dto = AttendeeObjectGenerator.generateAttendeeDtoWithoutTimestamps(null);
        // postAttendee
        final ResponseEntity<AttendeeDto> actualEntity = restTemplate.postForEntity(createUrl(), dto, AttendeeDto.class);
        assertTrue(actualEntity.getStatusCode().is2xxSuccessful());
        final AttendeeDto actualDto = actualEntity.getBody();
        // assert
        assertNotNull(actualDto);
        assertEquals(dto.uuid(), actualDto.uuid());
        assertTrue(actualDto.createdAt().isAfter(currentTime));
        assertTrue(actualDto.lastUpdated().isAfter(currentTime));
        assertEquals(dto.firstName(), actualDto.firstName());
        assertEquals(dto.lastName(), actualDto.lastName());
        assertTrue(actualDto.ticketIDs().isEmpty());

        final TicketDto ticket = TicketObjectGenerator.generateTicketDto(UUID.randomUUID(), UUID.randomUUID());
        when(ticketLookUpRemoteServiceMock.lookUpTicket(ticketId)).thenReturn(Mono.just(ticket));
        when(ticketLookUpRemoteServiceMock.ping()).thenReturn(Mono.just(true));
        when(userLookUpRemoteServiceMock.lookUpEntity(dto.uuid())).thenReturn(Mono.just(UserDto.builder()
                .uuid(UUID.randomUUID())
                .createdAt(OffsetDateTime.now())
                .lastUpdated(OffsetDateTime.now())
                .username("testUser")
                .email("testUser@example.com")
                .firstName("Test")
                .lastName("User")
                .role(UserRole.ATTENDEE)
                .mobilePhone("1234567890")
                .birthDate(LocalDate.of(1990, 1, 1))
                .build()));
        when(emailServiceMock.sendPurchaseTicketEmail(any(UUID.class),any(TicketDto.class))).thenReturn(Mono.empty());

        final ResponseEntity<Void> actualPutEntity = restTemplate.exchange(createUrl() + "/" + "{attendeeId}/addTicket?ticketId={ticketId}", HttpMethod.PUT, null, Void.class, actualDto.uuid(), ticketId);

        assertTrue(actualPutEntity.getStatusCode().is2xxSuccessful());

        restTemplate.delete(createUrl() + "/" + actualDto.uuid());
    }

    @Test
    @WithMockUser(roles = {"READ_ATTENDEE"})
    void postAttendeeWithWrongRole() {

        final AttendeeDto dto = AttendeeObjectGenerator.generateAttendeeDtoWithoutTimestamps(UUID.randomUUID());
        try {
            restTemplate.postForEntity(createUrl(), dto, AttendeeDto.class);
        } catch (HttpClientErrorException.Forbidden e) {
            return;
        }
        throw new AssertionError("The request status is not 403");
    }

    private String createUrl() {

        return HOSTNAME + port + RELATIVE_ENDPOINT;
    }

    @SuppressWarnings({"all"})
    private HttpEntity createHttpEntity(final AttendeeDto updatedDto) {

        return new HttpEntity(updatedDto);
    }
}
