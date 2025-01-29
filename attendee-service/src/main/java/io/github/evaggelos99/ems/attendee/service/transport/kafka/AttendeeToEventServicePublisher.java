package io.github.evaggelos99.ems.attendee.service.transport.kafka;

import io.github.evaggelos99.ems.common.api.transport.AttendeeToEventPayload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

    private final String topicName;
    private final KafkaTemplate<String, Serializable> template;

    /**
     * C-or
     *
     * @param template  the {@link KafkaTemplate} of Key value {@link String} and Value {@link Serializable}
     * @param topicName the topic name to send the data to
     */
    public AttendeeToEventServicePublisher(@Autowired final KafkaTemplate<String, Serializable> template,
                                           @Value("${io.github.evaggelos99.ems.event.topic.add-attendee}") final String topicName) {

        this.template = template;
        this.topicName = topicName;
    }

    public Mono<Boolean> send(final UUID eventId, final UUID attendeeId) {

        LOGGER.trace("Publishing data to topic: {}", topicName);

        final CompletableFuture<SendResult<String, Serializable>> fut = template.send(topicName, new AttendeeToEventPayload(eventId, attendeeId));
        // return hook?
        return Mono.fromFuture(fut)
                .map(x -> true)
                .doOnError(error -> LOGGER.error("Could not reach send message to EventService", error))
                .onErrorReturn(false);
    }
}
