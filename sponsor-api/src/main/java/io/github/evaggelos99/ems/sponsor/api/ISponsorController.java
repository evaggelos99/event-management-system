package io.github.evaggelos99.ems.sponsor.api;

import io.github.evaggelos99.ems.common.api.controller.IGenericController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

/**
 * Interface for the {@link Sponsor} Controller
 *
 * @author Evangelos Georgiou
 */
@Tag(name = "Sponsor", description = "API to create Sponsor objects")
public interface ISponsorController extends IGenericController {

    /**
     * Method that creates an Sponsor object and saves it in the DB
     *
     * @param sponsorDto
     */
    @Operation(summary = "POST operation that creates an sponsor object", description = "creates an Sponsor object and stores it in the data source")
    @ApiResponses(value = {@ApiResponse(responseCode = "201", description = "successful operation")})
    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    Mono<SponsorDto> postSponsor(@Valid @RequestBody SponsorDto sponsorDto);

    /**
     * Method that gets an Sponsor object from the DB
     *
     * @param sponsorId the UUID that will be used to search for the Sponsor
     */
    @Operation(summary = "GET operation that returns an sponsor object", description = "retrieves an Sponsor object from the data source")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "successful operation"),
            @ApiResponse(responseCode = "404", description = "could not find the object")})
    @ResponseStatus(value = HttpStatus.OK)
    @GetMapping("/{sponsorId}")
    Mono<SponsorDto> getSponsor(@PathVariable UUID sponsorId);

    /**
     * Method that gets all Sponsor objects from the DB
     */
    @Operation(summary = "GET operation that returns all sponsor object", description = "retrieves all Sponsor objects from the data source")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "successful operation")})
    @ResponseStatus(value = HttpStatus.OK)
    @GetMapping
    Flux<SponsorDto> getSponsors();

    /**
     * Method that updates the Sponsor with that SponsorId If the id does not match
     * any Sponsor stored in the DB it will return 404
     *
     * @param sponsorId  the UUID of the Sponsor object
     * @param sponsorDto the edited Sponsor object
     */
    @Operation(summary = "PUT operation that updates or creates an sponsor object", description = "updates or creates an Sponsor object and stores it in the data source")
    @ApiResponses(value = {@ApiResponse(responseCode = "201", description = "successful operation"),
            @ApiResponse(responseCode = "404", description = "could not find the object")})
    @PutMapping("/{sponsorId}")
    @ResponseStatus(value = HttpStatus.CREATED)
    Mono<SponsorDto> putSponsor(@PathVariable UUID sponsorId, @Valid @RequestBody SponsorDto sponsorDto);

    /**
     * Method that deletes the Sponsor with the specific sponsorId
     *
     * @param sponsorId the UUID of the Sponsor object
     */
    @Operation(summary = "DELETE operation that deletes an sponsor object", description = "deletes an Sponsor object")
    @ApiResponses(value = {@ApiResponse(responseCode = "204", description = "successful operation")})
    @ResponseStatus(value = HttpStatus.OK)
    @DeleteMapping(path = "/{sponsorId}")
    Mono<ResponseEntity<Void>> deleteSponsor(@PathVariable UUID sponsorId);

}
