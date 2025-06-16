package io.github.evaggelos99.ems.attendee.service.remote;

import io.github.evaggelos99.ems.attendee.api.service.remote.IEventServiceClient;
import io.github.evaggelos99.ems.attendee.service.transport.kafka.AttendeeToEventServicePublisher;
import io.github.evaggelos99.ems.ticket.api.TicketDto;
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
    public Mono<TicketDto> addAttendee(final TicketDto ticketDto, final UUID attendeeId) {

        return publisher.sendAddAttendeeMessage(ticketDto.eventID(), attendeeId)
                .filter(Boolean::booleanValue)
                .map(x-> ticketDto);
    }

    @Override
    public Mono<Boolean> removeAttendee(final UUID uuid, final UUID attendeeId) {
        return publisher.sendRemoveAttendeeMessage(uuid, attendeeId);
    }

    @Override
    public Mono<Boolean> ping() {

        return Mono.just(true);
    }

}
