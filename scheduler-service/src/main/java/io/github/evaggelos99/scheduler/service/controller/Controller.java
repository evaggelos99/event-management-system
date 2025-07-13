package io.github.evaggelos99.scheduler.service.controller;

import io.github.evaggelos99.scheduler.service.component.ITicketRemoteLookUpClient;
import io.github.evaggelos99.scheduler.service.component.JwtService;
import io.github.evaggelos99.scheduler.service.component.RedisClient;
import io.github.evaggelos99.scheduler.service.model.FeedbackDto;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.apache.commons.lang3.tuple.Triple;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.resteasy.reactive.server.jaxrs.ResponseBuilderImpl;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.Instant;
import java.util.UUID;

@Path("/")
@ApplicationScoped
public class Controller {

    @Inject
    @RestClient
    private ITicketRemoteLookUpClient postRestClient;

    @Inject
    private JwtService jwtService;

    @Inject
    private RedisClient redisClient;

    @Inject
    private DataSource dataSource;

    @ConfigProperty(name = "io.github.evaggelos99.ems.scheduler-service.sql.save-feedback")
    private String sql;

    @POST
    @Path("/feedback/{jwtToken}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response feedback(@PathParam("jwtToken") String jwtToken, FeedbackDto dto) {

        // validate ticketId
        try {
            final Triple<UUID, UUID, Boolean> pair = jwtService.validateJwt(jwtToken);
            final Boolean isValid = pair.getRight();

            if (isValid) {

                final UUID ticketId = pair.getLeft();
                final UUID eventId = pair.getMiddle();

                if (!redisClient.existsForFeedbackJwt(ticketId)) {

                    try (Connection connection = dataSource.getConnection();
                         PreparedStatement statement = connection.prepareStatement(sql)) {

                        connection.setAutoCommit(false);

                        statement.setObject(1, eventId);
                        statement.setInt(2, dto.rating());
                        if (dto.likedMost()!=null) {
                            statement.setString(3, dto.likedMost());
                        }
                        if (dto.whatCanBeImproved()!=null) {
                            statement.setString(4, dto.whatCanBeImproved());
                        }
                        if (dto.otherComments()!=null) {
                            statement.setString(5, dto.otherComments());
                        }
                        statement.setTimestamp(6, java.sql.Timestamp.from(Instant.now()));
                        statement.setObject(7, ticketId);

                        statement.executeUpdate();
                        connection.commit();

                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }

                    redisClient.cacheFeedbackJwt(ticketId);

                    return Response.ok().build();
                } else {
                    return new ResponseBuilderImpl().status(403).entity("The JWT provided has been reused or is invalid").build();
                }

            }
        } catch (Exception e) {

            return getUnauthorizedResponse();
        }

        // log something here or audit blabla
        return getUnauthorizedResponse();
    }

    private Response getUnauthorizedResponse() {
        return new ResponseBuilderImpl().status(401).entity("This endpoint is restricted").build();
    }

}
