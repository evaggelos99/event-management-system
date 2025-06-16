package io.github.evaggelos99.ems.attendee.service.remote;

import io.github.evaggelos99.ems.attendee.api.service.remote.ITicketLookUpServiceClient;
import io.github.evaggelos99.ems.attendee.api.service.remote.IUserLookUpServiceClient;
import io.github.evaggelos99.ems.common.api.dto.IdpUserProperties;
import io.github.evaggelos99.ems.ticket.api.TicketDto;
import io.github.evaggelos99.ems.user.api.UserDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
public class UserLookUpRemoteService implements IUserLookUpServiceClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserLookUpRemoteService.class);

    private final WebClient webClient;

    public UserLookUpRemoteService(final WebClient.Builder webClientBuilder,
                                   @Value("${io.github.evaggelos99.ems.attendee.user-service-url}") final String userUrl) {

        this.webClient = webClientBuilder.baseUrl(userUrl).build();
    }

    @Override
    public Mono<Boolean> ping() {

        return webClient.get()
                .uri("/ping")
                .retrieve()
                .bodyToMono(Boolean.class)
                .doOnError(this::logOnError)
                .onErrorReturn(false);
    }

    @Override
    public Mono<UserDto> lookUpUser(final UUID id) {

        return webClient.get().uri(uriBuilder -> uriBuilder.path("/{id}").build(id)).retrieve()
                .bodyToMono(UserDto.class).doOnError(this::logOnError);
    }

    @Override
    public Mono<UserDto> lookUpEntity(final UUID id) {

        return webClient.get().uri(uriBuilder -> uriBuilder.path("/entity/{id}").build(id)).retrieve()
                .bodyToMono(UserDto.class).doOnError(this::logOnError);
    }

    private void logOnError(Throwable err) {
        LOGGER.error("Could not find user", err);
    }

}
