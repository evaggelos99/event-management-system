package io.github.evaggelos99.ems.user.service.util;

import com.inversoft.error.Errors;
import com.inversoft.rest.ClientResponse;
import io.fusionauth.client.FusionAuthClient;
import io.fusionauth.domain.ApplicationRole;
import io.fusionauth.domain.api.ApplicationRequest;
import io.fusionauth.domain.api.ApplicationResponse;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.UUID;

public class FusionAuthApiTest {

    @Test
    @Disabled
    public void createMultipleRoles() {

        final String apiKey = "jUH_OWviFOIiRsNeN0GQ0uBwWdh8-QdMu2zLEkcKu3ghEfDICG9zO9Bi";
        final String baseUrl = "http://localhost:9011";
        final FusionAuthClient client = new FusionAuthClient(apiKey, baseUrl);

        UUID applicationId = UUID.fromString("99c404e4-2877-4296-9c85-a89191d19e21");



        String[] roles = {
                "ROLE_CREATE_TICKET", "ROLE_READ_TICKET", "ROLE_UPDATE_TICKET", "ROLE_DELETE_TICKET",
                "ROLE_CREATE_ATTENDEE", "ROLE_READ_ATTENDEE", "ROLE_UPDATE_ATTENDEE", "ROLE_DELETE_ATTENDEE",
                "ROLE_CREATE_EVENT", "ROLE_READ_EVENT", "ROLE_UPDATE_EVENT", "ROLE_DELETE_EVENT",
                "ROLE_CREATE_SPONSOR", "ROLE_READ_SPONSOR", "ROLE_UPDATE_SPONSOR", "ROLE_DELETE_SPONSOR",
                "ROLE_CREATE_ORGANIZER", "ROLE_READ_ORGANIZER", "ROLE_UPDATE_ORGANIZER", "ROLE_DELETE_ORGANIZER"
        };

        for (String role : roles) {
            final ApplicationRequest applicationReq = new ApplicationRequest();
            applicationReq.role = new ApplicationRole().with(x-> x.name=role);
            ClientResponse<ApplicationResponse, Errors> responseErrorsClientResponse = client.createApplicationRole(applicationId, UUID.randomUUID(), applicationReq);
            System.out.println(responseErrorsClientResponse.getSuccessResponse());
            System.out.println(responseErrorsClientResponse.getErrorResponse());
        }
    }

    @Test
    @Disabled
    public void createOneRole() {

        final String apiKey = "jUH_OWviFOIiRsNeN0GQ0uBwWdh8-QdMu2zLEkcKu3ghEfDICG9zO9Bi";
        final String baseUrl = "http://localhost:9011";
        final FusionAuthClient client = new FusionAuthClient(apiKey, baseUrl);

        UUID applicationId = UUID.fromString("99c404e4-2877-4296-9c85-a89191d19e21");

        final ApplicationRequest applicationReq = new ApplicationRequest();
        applicationReq.role = new ApplicationRole().with(x-> x.name="ROLE_ADMIN");
        ClientResponse<ApplicationResponse, Errors> responseErrorsClientResponse = client.createApplicationRole(applicationId, UUID.randomUUID(), applicationReq);
        System.out.println(responseErrorsClientResponse.getSuccessResponse());
        System.out.println(responseErrorsClientResponse.getErrorResponse());

    }



}
