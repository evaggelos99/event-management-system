package io.github.evaggelos99.ems.user.service.controller;

import io.github.evaggelos99.ems.common.api.domainobjects.UserRole;
import io.github.evaggelos99.ems.user.api.service.IUserService;
import io.github.evaggelos99.ems.user.api.User;
import io.github.evaggelos99.ems.user.api.UserDto;
import io.github.evaggelos99.ems.user.api.service.IUserController;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;
import java.util.function.Function;


/**
 * Controller for CRUD operation for the object {@link User}
 *
 * @author Evangelos Georgiou
 */
@RestController
@RequestMapping(UserController.USER_PATH)
public class UserController implements IUserController {

    static final String USER_PATH = "/user";

    private final IUserService userService;
    private final Function<User, UserDto> userToUserDtoConverter;

    /**
     * C-or
     *
     * @param userService              service responsible for CRUD operations
     * @param userToUserDtoConverter   user to DTO
     */
    public UserController(final IUserService userService,
            @Qualifier("userToUserDtoConverter") final Function<User, UserDto> userToUserDtoConverter) {

        this.userService = userService;
        this.userToUserDtoConverter = userToUserDtoConverter;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Mono<UserDto> postUser(UserDto userDto, final UUID entityUuid) {

        Mono<User> userMono = userService.add(userDto);
        if (userDto.role() == UserRole.ADMIN) {

            return userMono.map(userToUserDtoConverter);
        }

        return userMono
                .flatMap(user -> userService.addEntity(user, entityUuid).map(x-> user))
                .map(userToUserDtoConverter);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Mono<UserDto> getUser(final UUID userId) {

        return userService.get(userId).map(userToUserDtoConverter);
    }

    @Override
    public Mono<UserDto> getEntityUser(final UUID entityId) {

        return userService.getEntity(entityId).map(userToUserDtoConverter);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Mono<UserDto> putUser(final UUID userId, final UserDto userDto) {

        return userService.edit(userId, userDto).map(userToUserDtoConverter);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Mono<ResponseEntity<Void>> deleteUser(final UUID userId) {

        return userService.delete(userId).filter(Boolean::booleanValue).map(x -> ResponseEntity.ok().build());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Flux<UserDto> getUsers() {

        return userService.getAll().map(userToUserDtoConverter);
    }

    @Override
    public Mono<ResponseEntity<Void>> ping() {

        return userService.ping().filter(Boolean::booleanValue).map(x -> ResponseEntity.ok().build());
    }

    private ResponseEntity<Void> mapResponseEntity(Boolean ignored) {
        return ResponseEntity.ok().build();
    }

}
