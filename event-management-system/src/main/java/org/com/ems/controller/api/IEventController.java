package org.com.ems.controller.api;

import java.util.Collection;
import java.util.UUID;

import org.com.ems.api.domainobjects.Event;
import org.com.ems.api.dto.EventDto;
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
 * Interface for the {@link Event} Controller
 *
 * @author Evangelos Georgiou
 *
 */
@Tag(name = "Event", description = "API to create Event objects")
public interface IEventController {

    /**
     * Method that creates an Event DAO object and saves it in the DB
     *
     * @param event
     */
    @Operation(summary = "POST operation that creates an event object", description = "creates an Event object and stores it in the data source")
    @ApiResponses(value = { @ApiResponse(responseCode = "201", description = "successful operation") })
    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    ResponseEntity<EventDto> postEvent(@Valid @RequestBody EventDto eventDto);

    /**
     * Method that gets an Event DAO object from the DB
     *
     * @param eventId the UUID that will be used to search for the Event
     */
    @Operation(summary = "GET operation that returns an event object", description = "retrieves an Event object from the data source")
    @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "successful operation"),
	    @ApiResponse(responseCode = "404", description = "could not find the object") })
    @ResponseStatus(value = HttpStatus.OK)
    @GetMapping("/{eventId}")
    ResponseEntity<EventDto> getEvent(@PathVariable UUID eventId);

    /**
     * Method that gets all Event objects from the DB
     *
     */
    @Operation(summary = "GET operation that returns all event object", description = "retrieves all Event objects from the data source")
    @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "successful operation") })
    @ResponseStatus(value = HttpStatus.OK)
    @GetMapping
    ResponseEntity<Collection<EventDto>> getEvents();

    /**
     * Method that updates the Event with that eventId If the id does not match any
     * Event stored in the DB it will return 404
     *
     * @param eventId the UUID of the Event object
     * @param event   the edited Event object
     */
    @Operation(summary = "PUT operation that updates or creates an event object", description = "updates or creates an Event object and stores it in the data source")
    @ApiResponses(value = { @ApiResponse(responseCode = "201", description = "successful operation"),
	    @ApiResponse(responseCode = "404", description = "could not find the object") })
    @PutMapping("/{eventId}")
    @ResponseStatus(value = HttpStatus.CREATED)
    ResponseEntity<EventDto> putEvent(@PathVariable UUID eventId,
				      @Valid @RequestBody EventDto eventDto);

    /**
     * Method that deletes the event with the specific eventId
     *
     * @param eventId the UUID of the Event object
     */
    @Operation(summary = "DELETE operation that deletes an event object", description = "deletes an Event object")
    @ApiResponses(value = { @ApiResponse(responseCode = "204", description = "successful operation") })
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @DeleteMapping(path = "/{eventId}")
    ResponseEntity<?> deleteEvent(@PathVariable UUID eventId);

    /**
     * Method that updates the Event with that eventId If the id does not match any
     * Event stored in the DB it will return 404
     *
     * @param eventId the UUID of the Event object
     * @param event   the edited Event object
     */
    @Operation(summary = "PUT Operation that adds an attendee to the event", description = "Updates the event and adds another attendee to it.")
    @ApiResponses(value = { @ApiResponse(responseCode = "201", description = "successful operation"),
	    @ApiResponse(responseCode = "404", description = "could not find the object") })
    @PutMapping("/{eventId}/addAttendee")
    @ResponseStatus(value = HttpStatus.CREATED)
    ResponseEntity<Boolean> addAttendee(@PathVariable UUID eventId,
					@RequestParam UUID attendeeId);

}
