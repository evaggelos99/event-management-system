package io.github.evaggelos99.ems.event.api;

import io.github.evaggelos99.ems.common.api.controller.IGenericController;
import io.swagger.v3.oas.annotations.Hidden;
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
 * Interface for the {@link Event} Controller
 *
 * @author Evangelos Georgiou
 */
@Tag(name = "Event", description = "API to create Event objects")
public interface IEventController extends IGenericController {

    /**
     * Method that creates an Event object and saves it in the DB
     *
     * @param eventDto the DTO of event to be saved
     */
    @Operation(summary = "POST operation that creates an event object", description = "creates an Event object and stores it in the data source")
    @ApiResponses(value = {@ApiResponse(responseCode = "201", description = "successful operation")})
    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    Mono<EventDto> postEvent(@Valid @RequestBody EventDto eventDto);

    /**
     * Method that gets an Event object from the DB
     *
     * @param eventId the UUID that will be used to search for the Event
     */
    @Operation(summary = "GET operation that returns an event object", description = "retrieves an Event object from the data source")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "successful operation"),
            @ApiResponse(responseCode = "404", description = "could not find the object")})
    @ResponseStatus(value = HttpStatus.OK)
    @GetMapping("/{eventId}")
    Mono<EventDto> getEvent(@PathVariable UUID eventId);

    /**
     * Method that gets all Event objects from the DB
     */
    @Operation(summary = "GET operation that returns all event object", description = "retrieves all Event objects from the data source")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "successful operation")})
    @ResponseStatus(value = HttpStatus.OK)
    @GetMapping
    Flux<EventDto> getEvents();

    /**
     * Method that updates the Event with that eventId If the id does not match any
     * Event stored in the DB it will return 404
     *
     * @param eventId the UUID of the Event object
     * @param eventDto   the edited Event object
     */
    @Operation(summary = "PUT operation that updates or creates an event object", description = "updates or creates an Event object and stores it in the data source")
    @ApiResponses(value = {@ApiResponse(responseCode = "201", description = "successful operation"),
            @ApiResponse(responseCode = "404", description = "could not find the object")})
    @PutMapping("/{eventId}")
    @ResponseStatus(value = HttpStatus.CREATED)
    Mono<EventDto> putEvent(@PathVariable UUID eventId, @Valid @RequestBody EventDto eventDto);

    /**
     * Method that deletes the event with the specific eventId
     *
     * @param eventId the UUID of the Event object
     */
    @Operation(summary = "DELETE operation that deletes an event object", description = "deletes an Event object")
    @ApiResponses(value = {@ApiResponse(responseCode = "204", description = "successful operation")})
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @DeleteMapping(path = "/{eventId}")
    Mono<Boolean> deleteEvent(@PathVariable UUID eventId);

    /**
     * Adds a sponsor to the event. <br/>
     *
     * @param eventId the event ID that the sponsor will be added
     * @param sponsorId the sponsor ID that will be added to the event
     * @return {@link Boolean#TRUE} if the operation is successful otherwise {@link Boolean#FALSE}
     */
    @Hidden
    @PutMapping("/{eventId}/removeSponsor")
    Mono<Boolean> removeSponsor(@PathVariable UUID eventId, @RequestParam UUID sponsorId);

    /**
     * Removes a sponsor to the event. <br/>
     *
     * @param eventId the event ID that the sponsor will be added
     * @param sponsorId the sponsor ID that will be added to the event
     * @return {@link Boolean#TRUE} if the operation is successful otherwise {@link Boolean#FALSE}
     */
    @Hidden
    @PutMapping("/{eventId}/addSponsor")
    Mono<Boolean> addSponsor(@PathVariable UUID eventId, @RequestParam UUID sponsorId);

    /**
     * Method that gets all Event Stream objects from the DB
     */
    @Operation(summary = "GET operation that returns all event stream object", description = "retrieves all Event Stream objects from the data source")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "successful operation")})
    @ResponseStatus(value = HttpStatus.OK)
    @GetMapping("/event-stream/{eventId}")
    Flux<EventStreamDto> getEventStreams(@PathVariable UUID eventId);

}
