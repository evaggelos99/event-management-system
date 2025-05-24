package io.github.evaggelos99.ems.user.api;

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
 * Interface for the {@link User} Controller
 *
 * @author Evangelos Georgiou
 */
@Tag(name = "User", description = "API to create User objects")
public interface IUserController extends IGenericController {

    /**
     * Method that creates a User object and saves it in the DB
     *
     * @param userDto
     */
    @Operation(summary = "POST operation that creates a user object", description = "creates a User object and stores it in the data source")
    @ApiResponses(value = {@ApiResponse(responseCode = "201", description = "successful operation")})
    @ResponseStatus(value = HttpStatus.CREATED)
    @PostMapping
    Mono<UserDto> postUser(@Valid @RequestBody UserDto userDto);

    /**
     * Method that gets a User object from the DB
     *
     * @param userId the UUID that will be used to search for the User
     */
    @Operation(summary = "GET operation that returns a user object", description = "retrieves a User object from the data source")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "successful operation"),
            @ApiResponse(responseCode = "404", description = "could not find the object")})
    @ResponseStatus(value = HttpStatus.OK)
    @GetMapping("/{userId}")
    Mono<UserDto> getUser(@PathVariable UUID userId);

    /**
     * Method that returns all User objects from the DB
     */
    @Operation(summary = "GET operation that returns all user object", description = "retrieves all User objects from the data source")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "successful operation")})
    @ResponseStatus(value = HttpStatus.OK)
    @GetMapping()
    Flux<UserDto> getUsers();

    /**
     * Method that updates the User with that UserId If the id does not match
     * any User stored in the DB it will return 404
     *
     * @param userId the UUID of the User object
     */
    @Operation(summary = "PUT operation that updates or creates a user object", description = "updates or creates a User object and stores it in the data source")
    @ApiResponses(value = {@ApiResponse(responseCode = "201", description = "successful operation"),
            @ApiResponse(responseCode = "404", description = "could not find the object")})
    @ResponseStatus(value = HttpStatus.CREATED)
    @PutMapping("/{userId}")
    Mono<UserDto> putUser(@PathVariable UUID userId, @Valid @RequestBody UserDto userDto);

    /**
     * Method that deletes the User with the specific UserId
     *
     * @param userId the UUID of the User object
     */
    @Operation(summary = "DELETE operation that deletes a user object", description = "deletes a User object")
    @ApiResponses(value = {@ApiResponse(responseCode = "204", description = "successful operation")})
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @DeleteMapping("/{userId}")
    Mono<Boolean> deleteUser(@PathVariable UUID userId);

}
