package org.com.ems.controller.api;

import java.util.UUID;

import org.com.ems.api.domainobjects.Ticket;
import org.com.ems.api.dto.TicketDto;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Interface for the {@link Ticket} Controller
 *
 * @author Evangelos Georgiou
 *
 */
@Tag(name = "Ticket", description = "API to create Ticket objects")
public interface ITicketController {

    /**
     * Method that creates an Ticket DAO object and saves it in the DB
     *
     * @param ticket
     */
    @Operation(summary = "POST operation that creates an ticket object", description = "creates an Attendee object and stores it in the data source")
    @ApiResponses(value = { @ApiResponse(responseCode = "201", description = "successful operation") })
    @ResponseStatus(value = HttpStatus.CREATED)
    @PostMapping
    Mono<TicketDto> postTicket(@Valid @RequestBody TicketDto ticketDto);

    /**
     * Method that gets an Ticket DAO object from the DB
     *
     * @param ticketId the UUID that will be used to search for the Attendee
     */
    @Operation(summary = "GET operation that returns an ticket object", description = "retrieves an Attendee object from the data source")
    @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "successful operation"),
	    @ApiResponse(responseCode = "404", description = "could not find the object") })
    @ResponseStatus(value = HttpStatus.OK)
    @GetMapping("/{ticketId}")
    Mono<TicketDto> getTicket(@PathVariable UUID ticketId);

    /**
     * Method that returns all Ticket objects from the DB
     *
     */
    @Operation(summary = "GET operation that returns all ticket object", description = "retrieves all Attendee objects from the data source")
    @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "successful operation") })
    @ResponseStatus(value = HttpStatus.OK)
    @GetMapping()
    Flux<TicketDto> getTickets();

    /**
     * Method that updates the Ticket with that AttendeeId If the id does not match
     * any Attendee stored in the DB it will return 404
     *
     * @param ticketId the UUID of the Attendee object
     * @param ticket   the edited Ticket object
     */
    @Operation(summary = "PUT operation that updates or creates an ticket object", description = "updates or creates an Attendee object and stores it in the data source")
    @ApiResponses(value = { @ApiResponse(responseCode = "201", description = "successful operation"),
	    @ApiResponse(responseCode = "404", description = "could not find the object") })
    @ResponseStatus(value = HttpStatus.CREATED)
    @PutMapping("/{ticketId}")
    Mono<TicketDto> putTicket(@PathVariable UUID ticketId,
			      @Valid @RequestBody TicketDto ticketDto);

    /**
     * Method that deletes the Ticket with the specific AttendeeId
     *
     * @param ticketId the UUID of the Ticket object
     */
    @Operation(summary = "DELETE operation that deletes an ticket object", description = "deletes an Attendee object")
    @ApiResponses(value = { @ApiResponse(responseCode = "204", description = "successful operation") })
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @DeleteMapping("/{ticketId}")
    Mono<?> deleteTicket(@PathVariable UUID ticketId);

}
