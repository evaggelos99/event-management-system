package io.github.evaggelos99.scheduler.service.component;

import io.github.evaggelos99.scheduler.service.model.FusionauthTokenDto;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@Path("/oauth2")
@RegisterRestClient(configKey = "fusionauth-token-api")
public interface IFusionAuthClient {

    @POST
    @Path("/token")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    FusionauthTokenDto getToken(@FormParam("grant_type") String grantType,
                                @FormParam("username") String username,
                                @FormParam("password") String password,
                                @FormParam("client_id") String clientId,
                                @FormParam("client_secret") String clientSecrets);

}
