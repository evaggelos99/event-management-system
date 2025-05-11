package io.github.evaggelos99.ems.user.service;

import io.github.evaggelos99.ems.common.api.controller.exceptions.ObjectNotFoundException;
import io.github.evaggelos99.ems.common.api.domainobjects.validators.constraints.PublisherValidator;
import io.github.evaggelos99.ems.security.lib.IOnboardingIdentityManagerService;
import io.github.evaggelos99.ems.security.lib.SecurityContextHelper;
import io.github.evaggelos99.ems.user.api.IUserService;
import io.github.evaggelos99.ems.user.api.User;
import io.github.evaggelos99.ems.user.api.UserDto;
import io.github.evaggelos99.ems.user.api.repo.IUserRepository;
import io.github.evaggelos99.ems.user.service.repository.UserRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

import static io.github.evaggelos99.ems.security.lib.Roles.ROLE_ADMIN;

@Service
public class UserService implements IUserService {

    private final IUserRepository userRepository;
    private final IOnboardingIdentityManagerService onboardingIdentityManagerService;

    /**
     * C-or
     *
     * @param userRepository                   {@link UserRepository} the repository that
     *                                         communicates with the database
     * @param onboardingIdentityManagerService
     */
    public UserService(final IUserRepository userRepository, final IOnboardingIdentityManagerService onboardingIdentityManagerService) {

        this.userRepository = userRepository;
        this.onboardingIdentityManagerService = onboardingIdentityManagerService;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Mono<User> add(final UserDto userDto) {

        return SecurityContextHelper.filterRoles(ROLE_ADMIN)
                .flatMap( x-> PublisherValidator.validateBooleanMono(x, () ->onboardingIdentityManagerService.enrollUser(userDto)))
                .map(x-> UserDto.from(userDto).uuid(x.id()).build())
                .flatMap(userRepository::save);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Mono<User> get(final UUID uuid) {

        return SecurityContextHelper.filterRoles(ROLE_ADMIN)
                .flatMap( x-> PublisherValidator.validateBooleanMono(x, () -> onboardingIdentityManagerService.getUser(uuid)))
                .flatMap(x -> userRepository.findById(uuid)); // TODO involve fusion auth DTO
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Mono<Boolean> delete(final UUID uuid) {

        return SecurityContextHelper.filterRoles(ROLE_ADMIN)
                .flatMap(x -> PublisherValidator.validateBooleanMono(x, () -> userRepository.deleteById(uuid)));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Mono<User> edit(final UUID uuid, final UserDto userDto) {

        return !uuid.equals(userDto.uuid()) ? Mono.error(() -> new ObjectNotFoundException(uuid, UserDto.class))
                : SecurityContextHelper.filterRoles(ROLE_ADMIN)
                .flatMap( x-> PublisherValidator.validateBooleanMono(x, () -> onboardingIdentityManagerService.editUser(userDto)))
                .flatMap(dto -> userRepository.edit(userDto));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Flux<User> getAll() {

        return SecurityContextHelper.filterRoles(ROLE_ADMIN)
                .flatMapMany(x -> PublisherValidator.validateBooleanFlux(x, userRepository::findAll));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Mono<Boolean> existsById(final UUID ticketId) {

        return SecurityContextHelper.filterRoles(ROLE_ADMIN)
                .flatMap(x -> PublisherValidator.validateBooleanMono(x, () -> userRepository.existsById(ticketId)));
    }

    @Override
    public Mono<Boolean> ping() {

        return Mono.just(true);
    }

}
