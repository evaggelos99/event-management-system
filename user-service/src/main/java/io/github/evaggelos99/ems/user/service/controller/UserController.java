package io.github.evaggelos99.ems.user.service.controller;

import io.github.evaggelos99.ems.user.api.IUserService;
import io.github.evaggelos99.ems.user.api.User;
import io.github.evaggelos99.ems.user.api.UserDto;
import io.github.evaggelos99.ems.user.api.IUserController;
import org.springframework.beans.factory.annotation.Qualifier;
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
    public Mono<UserDto> postUser(final UserDto userDto) {

        return userService.add(userDto).map(userToUserDtoConverter);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Mono<UserDto> getUser(final UUID userId) {

        return userService.get(userId).map(userToUserDtoConverter);
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
    public Mono<Boolean> deleteUser(final UUID userId) {

        return userService.delete(userId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Flux<UserDto> getUsers() {

        return userService.getAll().map(userToUserDtoConverter);
    }

    @Override
    public Mono<Boolean> ping() {

        return userService.ping().onErrorReturn(false);
    }

}
