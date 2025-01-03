package io.github.evaggelos99.ems.attendee.service.remote;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.UUID;

/**
 * FIXME this must be it's own artifact
 *
 * @author Evangelos Georgiou
 */
@Service
public class EventServiceClient implements IEventServiceClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(EventServiceClient.class);

    private final WebClient webClient;

    public EventServiceClient(@Autowired final WebClient.Builder webClientBuilder) {

        this.webClient = webClientBuilder.baseUrl("http://event-service/event").build();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Mono<Boolean> addAttendee(final UUID eventId, final UUID attendeeId) {

        return webClient.put().uri(
                        uriBuilder -> uriBuilder.path("/{id}/addAttendee").queryParam("attendeeId", attendeeId).build(eventId))
                .retrieve().bodyToMono(Boolean.class).doOnError(this::log).onErrorReturn(false);
    }

    private void log(final Throwable exc) {

        final String simpleName = getClass().getSimpleName();
        LOGGER.warn(String.format("Could not add Attendee in: %s", simpleName), exc);
    }

    @Override
    public Mono<Boolean> ping() {

        return webClient.get().uri(uriBuilder -> uriBuilder.path("/ping").build()).retrieve().bodyToMono(Boolean.class)
                .log().onErrorReturn(false);
    }

}
