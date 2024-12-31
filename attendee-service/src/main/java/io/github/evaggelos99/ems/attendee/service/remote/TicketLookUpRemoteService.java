package io.github.evaggelos99.ems.attendee.service.remote;

import io.github.evaggelos99.ems.ticket.api.TicketDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
public class TicketLookUpRemoteService implements ITicketLookUpServiceClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(TicketLookUpRemoteService.class);

    private final WebClient webClient;

    public TicketLookUpRemoteService(@Autowired final WebClient.Builder webClientBuilder,
                                     @Value("${io.github.evaggelos99.ems.attendee.ticket-service-url}") final String ticketUrl) {

        this.webClient = webClientBuilder.baseUrl(ticketUrl).build();
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
