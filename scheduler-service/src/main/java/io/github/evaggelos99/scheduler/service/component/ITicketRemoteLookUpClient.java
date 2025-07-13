package io.github.evaggelos99.scheduler.service.component;

import io.github.evaggelos99.scheduler.service.model.TicketDto;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.rest.client.annotation.RegisterClientHeaders;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@Path("/ticket")
@RegisterRestClient(configKey = "ticket-api")
@RegisterClientHeaders(FusionAuthInterceptor.class)
public interface ITicketRemoteLookUpClient {

    /**
     * TODO add interceptor that adds bearer token
     * @param ticketId
     * @return
     */
    @GET
    @Path("/{uid}")
    @Consumes
    @Produces(MediaType.APPLICATION_JSON)
    TicketDto getById(@PathParam("uid") String ticketId);

}
