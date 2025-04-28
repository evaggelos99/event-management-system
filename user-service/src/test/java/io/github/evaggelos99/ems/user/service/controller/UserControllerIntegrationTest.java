package io.github.evaggelos99.ems.user.service.controller;

import io.github.evaggelos99.ems.user.api.UserDto;
import io.github.evaggelos99.ems.user.service.UserServiceApplication;
import io.github.evaggelos99.ems.user.service.util.SqlScriptExecutor;
import io.github.evaggelos99.ems.user.service.util.TestConfiguration;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.web.client.RestTemplate;

@SpringBootTest(classes = {UserServiceApplication.class,
        TestConfiguration.class}, webEnvironment = WebEnvironment.RANDOM_PORT)
@TestInstance(Lifecycle.PER_CLASS)
class UserControllerIntegrationTest {

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


    }

    private String createUrl() {

        return HOSTNAME + port + RELATIVE_ENDPOINT;
    }

    @Test
    @WithMockUser(roles = {"CREATE_TICKET", "UPDATE_TICKET", "DELETE_TICKET", "READ_TICKET"})
    void postTicket_putTicket_deleteTicket_getAll_whenInvokedWithValidTicketDto_thenExpectForTicketToBeAddedThenEditedThenDeleted() {


    }

    @SuppressWarnings({"all"})
    private HttpEntity createHttpEntity(final UserDto updatedDto) {

        return new HttpEntity(updatedDto);
    }

    @Test
    @WithMockUser(roles = {"READ_TICKET"})
    void postTicketWithWrongRole() {


    }

    @Test
    void postTicketWithNoRole() {

    }
}
