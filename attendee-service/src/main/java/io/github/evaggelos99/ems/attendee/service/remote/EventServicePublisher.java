package io.github.evaggelos99.ems.attendee.service.remote;

import io.github.evaggelos99.ems.attendee.api.service.remote.IEventServiceClient;
import io.github.evaggelos99.ems.attendee.service.transport.kafka.AttendeeToEventServicePublisher;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.UUID;

/**
 *
 * @author Evangelos Georgiou
 */
@Service
public class EventServicePublisher implements IEventServiceClient {

    private final AttendeeToEventServicePublisher publisher;

    public EventServicePublisher(final AttendeeToEventServicePublisher publisher) {

        this.publisher = publisher;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Mono<Boolean> addAttendee(final UUID eventId, final UUID attendeeId) {

        return publisher.send(eventId, attendeeId);
    }

    @Override
    public Mono<Boolean> ping() {

        return Mono.just(true);
    }

}
