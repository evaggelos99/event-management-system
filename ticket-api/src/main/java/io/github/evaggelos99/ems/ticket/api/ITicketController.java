package io.github.evaggelos99.ems.ticket.api;

import io.github.evaggelos99.ems.common.api.controller.IGenericController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

/**
 * Interface for the {@link Ticket} Controller
 *
 * @author Evangelos Georgiou
 */
@Tag(name = "Ticket", description = "API to create Ticket objects")
public interface ITicketController extends IGenericController {

    /**
     * Method that creates an Ticket object and saves it in the DB
     *
     * @param ticketDto
     */
    @Operation(summary = "POST operation that creates an ticket object", description = "creates an Attendee object and stores it in the data source")
    @ApiResponses(value = {@ApiResponse(responseCode = "201", description = "successful operation")})
    @ResponseStatus(value = HttpStatus.CREATED)
    @PostMapping
    Mono<TicketDto> postTicket(@Valid @RequestBody TicketDto ticketDto);

    /**
     * Method that gets an Ticket object from the DB
     *
     * @param ticketId the UUID that will be used to search for the Attendee
     */
    @Operation(summary = "GET operation that returns an ticket object", description = "retrieves an Attendee object from the data source")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "successful operation"),
            @ApiResponse(responseCode = "404", description = "could not find the object")})
    @ResponseStatus(value = HttpStatus.OK)
    @GetMapping("/{ticketId}")
    Mono<TicketDto> getTicket(@PathVariable UUID ticketId);

    /**
     * Method that returns all Ticket objects from the DB
     */
    @Operation(summary = "GET operation that returns all ticket object", description = "retrieves all Attendee objects from the data source")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "successful operation")})
    @ResponseStatus(value = HttpStatus.OK)
    @GetMapping()
    Flux<TicketDto> getTickets();

    /**
     * Method that updates the Ticket with that AttendeeId If the id does not match
     * any Attendee stored in the DB it will return 404
     *
     * @param ticketId the UUID of the Attendee object
     */
    @Operation(summary = "PUT operation that updates or creates an ticket object", description = "updates or creates an Attendee object and stores it in the data source")
    @ApiResponses(value = {@ApiResponse(responseCode = "201", description = "successful operation"),
            @ApiResponse(responseCode = "404", description = "could not find the object")})
    @ResponseStatus(value = HttpStatus.CREATED)
    @PutMapping("/{ticketId}")
    Mono<TicketDto> putTicket(@PathVariable UUID ticketId, @Valid @RequestBody TicketDto ticketDto);

    /**
     * Method that deletes the Ticket with the specific AttendeeId
     *
     * @param ticketId the UUID of the Ticket object
     */
    @Operation(summary = "DELETE operation that deletes an ticket object", description = "deletes an Attendee object")
    @ApiResponses(value = {@ApiResponse(responseCode = "204", description = "successful operation")})
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @DeleteMapping("/{ticketId}")
    Mono<Boolean> deleteTicket(@PathVariable UUID ticketId);

}
