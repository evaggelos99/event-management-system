package io.github.evaggelos99.ems.user.service;

import io.github.evaggelos99.ems.security.lib.IOnboardingIdentityManagerService;
import io.github.evaggelos99.ems.user.api.User;
import io.github.evaggelos99.ems.user.api.UserDto;
import io.github.evaggelos99.ems.user.api.repo.IUserRepository;
import io.github.evaggelos99.ems.user.api.util.UserObjectGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith({SpringExtension.class, MockitoExtension.class})
class UserServiceTest {

    private final IUserRepository userRepository = userRepositoryMock();

    @Mock
    private IOnboardingIdentityManagerService onboardingIdentityManagerServiceMock;

    private UserService service;



    @BeforeEach
    void setUp() {

        service = new UserService(userRepository, onboardingIdentityManagerServiceMock);
    }

    @Test
    @WithMockUser(roles = {"CREATE_USER", "UPDATE_USER", "DELETE_USER", "READ_USER"})
    void add_delete_existsById_whenInvokedWithValidUserDto_thenExpectEventToBeCorrectThenDeletedThenNotFetched() {

        assertTrue(false, "TODO");
    }

    @Test
    @WithMockUser(roles = {"CREATE_USER", "UPDATE_USER", "DELETE_USER", "READ_USER"})
    void add_get_edit_delete_getAll_whenInvokedWithAttendeeDtoThenExpectToBeSaved_expectThatCanBeFetched() {

        assertTrue(false, "TODO");
    }

    private IUserRepository userRepositoryMock() {

        return new IUserRepository() {

            private final Map<UUID, User> list = new HashMap<>();

            @Override
            public Mono<User> save(final UserDto userDto) {

                final User sponsor =new User(userDto.uuid(), userDto.createdAt(), userDto.lastUpdated(),
                        userDto.username(),
                        userDto.email(),
                        userDto.firstName(),
                        userDto.lastName(),
                        userDto.role(),
                        userDto.mobilePhone(),
                        userDto.birthDate());

                list.put(userDto.uuid(), sponsor);

                return Mono.just(sponsor);
            }

            @Override
            public Mono<User> findById(final UUID uuid) {

                if (!list.containsKey(uuid)) {
                    return Mono.empty();
                }

                return Mono.just(list.get(uuid));
            }

            @Override
            public Mono<Boolean> deleteById(final UUID uuid) {

                return Mono.just(list.remove(uuid) != null);
            }

            @Override
            public Mono<Boolean> existsById(final UUID uuid) {

                return Mono.just(list.containsKey(uuid));
            }

            @Override
            public Flux<User> findAll() {

                return Flux.fromIterable(list.values());
            }

            @Override
            public Mono<User> edit(final UserDto userDto) {

                final User sponsor = new User(userDto.uuid(), userDto.createdAt(), userDto.lastUpdated(),
                        userDto.username(),
                        userDto.email(),
                        userDto.firstName(),
                        userDto.lastName(),
                        userDto.role(),
                        userDto.mobilePhone(),
                        userDto.birthDate());

                list.put(userDto.uuid(), sponsor);

                return Mono.just(sponsor);
            }
        };
    }

}
