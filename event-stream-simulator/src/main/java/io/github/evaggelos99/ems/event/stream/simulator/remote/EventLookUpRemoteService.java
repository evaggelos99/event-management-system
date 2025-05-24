package io.github.evaggelos99.ems.event.stream.simulator.remote;

import io.github.evaggelos99.ems.common.api.service.remote.ITokenRetriever;
import io.github.evaggelos99.ems.event.api.EventDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
public class EventLookUpRemoteService implements IRemoteServiceClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(EventLookUpRemoteService.class);

    private final WebClient webClient;
    private final ITokenRetriever tokenRetriever;

    public EventLookUpRemoteService(final WebClient.Builder webClientBuilder,
                                     @Value("${io.github.evaggelos99.ems.event.simulator.event-service-url}") final String ticketUrl, ITokenRetriever tokenRetriever) {

        this.webClient = webClientBuilder.baseUrl(ticketUrl).build();
        this.tokenRetriever=tokenRetriever;
    }

    public Mono<EventDto> getEventDto(final UUID id) {
        return webClient.get().uri(uriBuilder -> uriBuilder.path("/{id}").build(id)).retrieve()
                .bodyToMono(EventDto.class).doOnError(this::logOnError);
    }

    public Flux<EventDto> getAllEventDtos() {

        return tokenRetriever.getToken()
                .flatMapMany(token -> webClient
                .get()
                .header("Authorization","Bearer " + token)
                .retrieve()
                .bodyToFlux(EventDto.class).doOnError(this::logOnError));
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

    private void logOnError(Throwable err) {
        LOGGER.error("Could not find event", err);
    }

}
