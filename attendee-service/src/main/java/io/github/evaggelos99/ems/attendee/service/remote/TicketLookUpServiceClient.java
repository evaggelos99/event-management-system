package io.github.evaggelos99.ems.attendee.service.remote;

import io.github.evaggelos99.ems.ticket.api.TicketDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
public class TicketLookUpServiceClient implements ITicketLookUpServiceClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(TicketLookUpServiceClient.class);

    private final WebClient webClient;

    public TicketLookUpServiceClient(@Autowired final WebClient.Builder webClientBuilder) {

        this.webClient = webClientBuilder.baseUrl("http://ticket-service/ticket").build();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Mono<TicketDto> lookUpTicket(final UUID id) {

        return webClient.get().uri(uriBuilder -> uriBuilder.path("/{id}").build(id)).retrieve()
                .bodyToMono(TicketDto.class).doOnError(this::log);
    }

    private void log(final Throwable exc) {

        final String simpleName = getClass().getSimpleName();
        LOGGER.warn(String.format("Could not add Attendee in: %s", simpleName), exc);
    }

    @Override
    public Mono<Boolean> ping() {

        return webClient.get().uri("/ping").retrieve().bodyToMono(Boolean.class).log().onErrorReturn(false);
    }

}
