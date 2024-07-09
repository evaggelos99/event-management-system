package org.com.ems.attendee.service.remote;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Mono;

/**
 * FIXME this must be it's own artifact
 *
 * @author Evangelos Georgiou
 *
 */
@Service
public class EventWebService {

    private static final Logger LOGGER = LoggerFactory.getLogger(EventWebService.class);

    private final WebClient webClient;

    public EventWebService(@Autowired final WebClient.Builder webClientBuilder) {

	this.webClient = webClientBuilder.baseUrl("http://event-service/event").build();

    }

    public Mono<Boolean> addAttendee(final UUID eventId,
				     final UUID attendeeId) {

	return this.webClient.put().uri(
		uriBuilder -> uriBuilder.path("/{id}/addAttendee").queryParam("attendeeId", attendeeId).build(eventId))
		.retrieve().bodyToMono(Boolean.class).doOnError(this::log).onErrorReturn(false);

    }

    private void log(final Throwable exc) {

	final String simpleName = this.getClass().getSimpleName();
	LOGGER.warn(String.format("Could not add Attendee in: %s", simpleName), exc);

    }

}
