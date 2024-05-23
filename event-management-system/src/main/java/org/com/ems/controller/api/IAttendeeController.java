package org.com.ems.controller.api;

import java.util.Collection;
import java.util.UUID;

import org.com.ems.api.domainobjects.Attendee;
import org.com.ems.api.dto.AttendeeDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

/**
 * Interface for the {@link Attendee} Controller
 *
 * @author Evangelos Georgiou
 *
 */
@Tag(name = "Attendee", description = "API to create Attendee objects")
public interface IAttendeeController {

    /**
     * Method that creates an Attendee DAO object and saves it in the DB
     *
     * @param attendee
     */
    @Operation(summary = "POST operation that creates an attendee object", description = "creates an Attendee object and stores it in the data source")
    @ApiResponses(value = { @ApiResponse(responseCode = "201", description = "successful operation") })
    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    ResponseEntity<AttendeeDto> postAttendee(@Valid @RequestBody AttendeeDto attendee);

    /**
     * Method that gets an Attendee DAO object from the DB
     *
     * @param attendeeId the UUID that will be used to search for the Attendee
     */
    @Operation(summary = "GET operation that returns an attendee object", description = "retrieves an Attendee object from the data source")
    @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "successful operation"),
	    @ApiResponse(responseCode = "404", description = "could not find the object") })
    @ResponseStatus(value = HttpStatus.OK)
    @GetMapping("/{attendeeId}")
    ResponseEntity<AttendeeDto> getAttendee(@PathVariable UUID attendeeId);

    /**
     * Method that gets all Attendee objects from the DB
     *
     */
    @Operation(summary = "GET operation that returns all attendee object", description = "retrieves all Attendee objects from the data source")
    @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "successful operation") })
    @ResponseStatus(value = HttpStatus.OK)
    @GetMapping
    ResponseEntity<Collection<AttendeeDto>> getAttendees();

    /**
     * Method that updates the Attendee with that AttendeeId If the id does not
     * match any Attendee stored in the DB it will return 404
     *
     * @param attendeeId the UUID of the Attendee object
     * @param attendee   the edited Attendee object
     */
    @Operation(summary = "PUT operation that updates or creates an attendee object", description = "updates or creates an Attendee object and stores it in the data source")
    @ApiResponses(value = { @ApiResponse(responseCode = "201", description = "successful operation"),
	    @ApiResponse(responseCode = "404", description = "could not find the object") })
    @PutMapping("/{attendeeId}")
    @ResponseStatus(value = HttpStatus.CREATED)
    ResponseEntity<AttendeeDto> putAttendee(@PathVariable UUID attendeeId,
					    @Valid @RequestBody AttendeeDto attendee);

    /**
     * Method that deletes the Attendee with the specific attendeeId
     *
     * @param attendeeId the UUID of the Attendee object
     */
    @Operation(summary = "DELETE operation that deletes an attendee object", description = "deletes an Attendee object")
    @ApiResponses(value = { @ApiResponse(responseCode = "204", description = "successful operation") })
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @DeleteMapping(path = "/{attendeeId}")
    ResponseEntity<?> deleteAttendee(@PathVariable UUID attendeeId);

    /**
     * Method that updates the Event with that eventId If the id does not match any
     * Event stored in the DB it will return 404
     *
     * @param eventId the UUID of the Event object
     * @param event   the edited Event object
     */
    @Operation(summary = "PUT Operation that adds a ticket to the attendee", description = "Updates the attendee and adds a ticket")
    @ApiResponses(value = { @ApiResponse(responseCode = "201", description = "successful operation"),
	    @ApiResponse(responseCode = "404", description = "could not find the object") })
    @PutMapping("/{attendeeId}/addTicket")
    @ResponseStatus(value = HttpStatus.CREATED)
    ResponseEntity<Boolean> addTicket(@PathVariable UUID attendeeId,
				      @RequestParam UUID ticketId);

}
