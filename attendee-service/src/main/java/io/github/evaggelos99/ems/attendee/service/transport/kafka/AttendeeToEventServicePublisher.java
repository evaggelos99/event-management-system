package io.github.evaggelos99.ems.attendee.service.transport.kafka;

import io.github.evaggelos99.ems.common.api.transport.AttendeeToEventPayload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.io.Serializable;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Component
public class AttendeeToEventServicePublisher {

    private static final Logger LOGGER = LoggerFactory.getLogger(AttendeeToEventServicePublisher.class);

    private final KafkaTemplate<String, Serializable> template;
    private final String addAttendeeTopic;
    private final String removeAttendeeTopic;

    /**
     * C-or
     *
     * @param template  the {@link KafkaTemplate} of Key value {@link String} and Value {@link Serializable}
     * @param addAttendeeTopic the topic name to send the data to
     */
    public AttendeeToEventServicePublisher(final KafkaTemplate<String, Serializable> template,
                                           @Value("${io.github.evaggelos99.ems.event.topic.add-attendee}") final String addAttendeeTopic,
                                           @Value("${io.github.evaggelos99.ems.event.topic.remove-attendee}") final String removeAttendeeTopic) {

        this.template = template;
        this.addAttendeeTopic = addAttendeeTopic;
        this.removeAttendeeTopic = removeAttendeeTopic;
    }

    public Mono<Boolean> sendAddAttendeeMessage(final UUID eventId, final UUID attendeeId) {

        LOGGER.trace("Publishing data to topic: {}", addAttendeeTopic);

        final CompletableFuture<SendResult<String, Serializable>> fut = template.send(addAttendeeTopic, new AttendeeToEventPayload(eventId, attendeeId));

        return Mono.fromFuture(fut)
                .map(x -> true)
                .doOnError(error -> LOGGER.error("Could not reach send message to EventService", error))
                .onErrorReturn(false);
    }

    public Mono<Boolean> sendRemoveAttendeeMessage(final UUID eventId, final UUID attendeeId) {

        LOGGER.trace("Publishing data to topic: {}", removeAttendeeTopic);

        final CompletableFuture<SendResult<String, Serializable>> fut = template.send(removeAttendeeTopic, new AttendeeToEventPayload(eventId, attendeeId));

        return Mono.fromFuture(fut)
                .map(x -> true)
                .doOnError(error -> LOGGER.error("Could not reach send message to EventService", error))
                .onErrorReturn(false);
    }
}
