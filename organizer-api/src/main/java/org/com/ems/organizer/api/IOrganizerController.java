package org.com.ems.organizer.api;

import java.util.UUID;

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
 * Interface for the {@link Organizer} Controller
 *
 * @author Evangelos Georgiou
 *
 */
@Tag(name = "Organizer", description = "API to create Organizer objects")
public interface IOrganizerController {

    /**
     * Method that creates an Organizer DAO object and saves it in the DB
     *
     * @param organizer
     */
    @Operation(summary = "POST operation that creates an organizer object", description = "creates an Attendee object and stores it in the data source")
    @ApiResponses(value = { @ApiResponse(responseCode = "201", description = "successful operation") })
    @ResponseStatus(value = HttpStatus.CREATED)
    @PostMapping
    Mono<OrganizerDto> postOrganizer(@Valid @RequestBody OrganizerDto organizerDto);

    /**
     * Method that gets an Organizer DAO object from the DB
     *
     * @param organizerId the UUID that will be used to search for the Organizer
     */
    @Operation(summary = "GET operation that returns an organizer object", description = "retrieves an Attendee object from the data source")
    @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "successful operation"),
	    @ApiResponse(responseCode = "404", description = "could not find the object") })
    @ResponseStatus(value = HttpStatus.OK)
    @GetMapping("/{organizerId}")
    Mono<OrganizerDto> getOrganizer(@PathVariable UUID organizerId);

    /**
     * Method that gets all Organizer objects from the DB
     *
     */
    @Operation(summary = "GET operation that returns all organizer object", description = "retrieves all Attendee objects from the data source")
    @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "successful operation") })
    @ResponseStatus(value = HttpStatus.OK)
    @GetMapping()
    Flux<OrganizerDto> getOrganizers();

    /**
     * Method that updates the Organizer with that organizerId If the id does not
     * match any Attendee stored in the DB it will return 404
     *
     * @param organizerId the UUID of the Organizer object
     * @param organizer   the edited Organizer object
     */
    @Operation(summary = "PUT operation that updates or creates an organizer object", description = "updates or creates an Attendee object and stores it in the data source")
    @ApiResponses(value = { @ApiResponse(responseCode = "201", description = "successful operation"),
	    @ApiResponse(responseCode = "404", description = "could not find the object") })
    @ResponseStatus(value = HttpStatus.CREATED)
    @PutMapping("/{organizerId}")
    Mono<OrganizerDto> putOrganizer(@PathVariable UUID organizerId,
				    @Valid @RequestBody OrganizerDto organizerDto);

    /**
     * Method that deletes the Organizer with the specific organizerId
     *
     * @param organizerId the UUID of the Organizer object
     */
    @Operation(summary = "DELETE operation that deletes an organizer object", description = "deletes an Attendee object")
    @ApiResponses(value = { @ApiResponse(responseCode = "204", description = "successful operation") })
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @DeleteMapping(path = "/{organizerId}")
    Mono<?> deleteOrganizer(@PathVariable UUID organizerId);

}
