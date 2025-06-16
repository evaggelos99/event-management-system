package io.github.evaggelos99.ems.user.service;

import com.inversoft.error.Errors;
import com.inversoft.rest.ClientResponse;
import com.nimbusds.jose.util.Pair;
import io.fusionauth.client.FusionAuthClient;
import io.fusionauth.domain.Application;
import io.fusionauth.domain.User;
import io.fusionauth.domain.UserRegistration;
import io.fusionauth.domain.api.ApplicationSearchRequest;
import io.fusionauth.domain.api.UserRequest;
import io.fusionauth.domain.api.UserResponse;
import io.fusionauth.domain.api.user.RegistrationRequest;
import io.fusionauth.domain.search.ApplicationSearchCriteria;
import io.github.evaggelos99.ems.common.api.dto.IdpUserProperties;
import io.github.evaggelos99.ems.user.api.service.OnboardingIdentityManagerService;
import io.github.evaggelos99.ems.user.api.Roles;
import io.github.evaggelos99.ems.user.api.UserDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

import java.time.ZoneId;
import java.util.Objects;
import java.util.TreeSet;
import java.util.UUID;
import java.util.stream.Stream;

public class FusionAuthOnboardingIdentityManagerService implements OnboardingIdentityManagerService {

    private static final Logger LOGGER = LoggerFactory.getLogger(FusionAuthOnboardingIdentityManagerService.class);
    private final FusionAuthClient fusionAuthClient;
    private final String applicationName;

    /**
     * C-or
     *
     * @param apiKey
     * @param baseUrl
     * @param applicationName
     */
    public FusionAuthOnboardingIdentityManagerService(String apiKey, String baseUrl, String applicationName) {

        fusionAuthClient = new FusionAuthClient(apiKey, baseUrl);
        this.applicationName = applicationName;
    }

    @Override
    public Mono<IdpUserProperties> enrollUser(final UserDto userDto) {

        final UUID userId = Objects.isNull(userDto.uuid()) ? UUID.randomUUID() : userDto.uuid();
        final User fusionAuthUser = populateUser(userDto);

        return Mono.zip(Mono.just(getApplication()), Mono.just(new UserRegistration()
                        .with(userReg -> userReg.id = fusionAuthUser.id)
                        .with(userReg -> userReg.roles = new TreeSet<>(Stream.of(Roles.values())
                                .map(Enum::name)
                                .filter(x -> x.contains(userDto.role().name()))
                                .peek(x -> LOGGER.info("role: {}", x))
                                .toList()))
                        .with(userReg -> userReg.verified = true)
                        .with(userReg -> userReg.username = userDto.username())))
                .map(x -> x.getT2().with(userReg -> userReg.applicationId = x.getT1().id))
                .map(userRegistration -> mapToRegistrationRequest(userRegistration, fusionAuthUser))
                .map(x -> fusionAuthClient.register(userId, x))
                .map(ClientResponse::getSuccessResponse)
                .filter(Objects::nonNull)
                .map(res -> Pair.of(res.user, res.registration))
                .map(pair ->
                        new IdpUserProperties(pair.getLeft().id, pair.getLeft().firstName, pair.getLeft().lastName, pair.getLeft().email, pair.getLeft().birthDate, pair.getLeft().mobilePhone, pair.getRight().roles.stream()
                                .toList()));
    }

    @Override
    public Mono<IdpUserProperties> editUser(final UserDto userDto) {

        return Mono.zip(Mono.just(getApplication()),
                        Mono.just(fusionAuthClient.updateUser(userDto.uuid(), new UserRequest(populateEditUser(userDto)))))
                .filter(tuple -> tuple.getT2().getSuccessResponse() != null)
                .map(this::mapToIdentityUserDto);
    }

    @Override
    public Mono<IdpUserProperties> getUser(final UUID uuid) {

        return Mono.zip(Mono.just(getApplication()), Mono.just(fusionAuthClient.retrieveUser(uuid)))
                .filter(x -> x.getT2().getSuccessResponse() != null)
                .map(this::mapToUserAndUserReg)
                .map(x -> new IdpUserProperties(x.getT1().id, x.getT1().firstName, x.getT1().lastName, x.getT1().email, x.getT1().birthDate, x.getT1().mobilePhone, x.getT2().roles.stream()
                        .toList()));
    }

    private IdpUserProperties mapToIdentityUserDto(final Tuple2<Application, ClientResponse<UserResponse, Errors>> tuple) {

        final User user = tuple.getT2().getSuccessResponse().user;
        final UserRegistration userRegistrationFromApplication = user.getRegistrationForApplication(tuple.getT1().id);
        return new IdpUserProperties(user.id, user.firstName, user.lastName, user.email, user.birthDate, user.mobilePhone, userRegistrationFromApplication.roles.stream()
                .toList());
    }

    private Tuple2<User, UserRegistration> mapToUserAndUserReg(final Tuple2<Application, ClientResponse<UserResponse, Errors>> x) {
        final User user = x.getT2().getSuccessResponse().user;
        final UserRegistration registration = user.getRegistrationForApplication(x.getT1().id);
        return Tuples.of(user, registration);
    }

    private User populateEditUser(final UserDto userDto) {

        return new User()
                .with(user -> user.username = userDto.username())
                .with(user -> user.active = true)
                .with(user -> user.birthDate = userDto.birthDate())
                .with(user -> user.email = userDto.email())
                .with(user -> user.firstName = userDto.firstName())
                .with(user -> user.lastName = userDto.lastName())
                .with(user -> user.fullName = userDto.firstName() + " " + userDto.lastName())
                .with(user -> user.mobilePhone = userDto.mobilePhone())
                .with(user -> user.timezone = ZoneId.systemDefault());
    }

    private RegistrationRequest mapToRegistrationRequest(final UserRegistration userRegistration, final User fusionAuthUser) {

        final RegistrationRequest registrationRequest = new RegistrationRequest();
        registrationRequest.user = fusionAuthUser;
        registrationRequest.registration = userRegistration;
        registrationRequest.skipVerification = false;
        registrationRequest.sendSetPasswordEmail = true;

        return registrationRequest;
    }

    private Application getApplication() {

        final ApplicationSearchRequest search = new ApplicationSearchRequest()
                .with(x -> x.search = new ApplicationSearchCriteria()
                        .with(y -> y.name = applicationName));

        return fusionAuthClient.searchApplications(search)
                .getSuccessResponse().applications.get(0);
    }


    private User populateUser(final UserDto userDto) {

        return new User()
                .with(user -> user.username = userDto.username())
                .with(user -> user.active = true)
                .with(user -> user.birthDate = userDto.birthDate())
                .with(user -> user.email = userDto.email())
                .with(user -> user.firstName = userDto.firstName())
                .with(user -> user.lastName = userDto.lastName())
                .with(user -> user.fullName = userDto.firstName() + " " + userDto.lastName())
                .with(user -> user.mobilePhone = userDto.mobilePhone())
                .with(user -> user.timezone = ZoneId.systemDefault());
    }
}
