package io.github.evaggelos99.ems.event.service.transport.kafka;

import io.github.evaggelos99.ems.common.api.transport.AttendeeToEventPayload;
import io.github.evaggelos99.ems.common.api.transport.EventStreamPayload;
import io.github.evaggelos99.ems.event.api.service.IEventService;
import io.github.evaggelos99.ems.event.service.EventService;
import io.github.evaggelos99.ems.kafka.lib.deserializer.ObjectDeserializer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

@Component
public class Consumer {

    private static final Logger LOGGER = LoggerFactory.getLogger(Consumer.class);

    private final IEventService eventService;
    private final ObjectDeserializer objectDeserializer;

    /**
     * C-or
     *
     * @param eventService the {@link EventService}
     */
    public Consumer(final IEventService eventService, final ObjectDeserializer objectDeserializer) {

        this.eventService = eventService;
        this.objectDeserializer = objectDeserializer;
    }

    @KafkaListener(topics = "${io.github.evaggelos99.ems.event.topic.add-attendee}", groupId = "default-group",
            containerFactory = "kafkaManualAckListenerContainerFactory")
    void consumeAddAttendeeMessage(final ConsumerRecord<String, byte[]> payload, Acknowledgment ack, @Value("${io.github.evaggelos99.ems.event.topic.add-attendee}") String topicName) {

        final AttendeeToEventPayload attendeeToEventPayload = (AttendeeToEventPayload) objectDeserializer.convertBytesToObject(payload.value());
        LOGGER.trace("we just got an event for topic {} and message {}", topicName, attendeeToEventPayload);
        final boolean result = Boolean.TRUE.equals(eventService.addAttendee(attendeeToEventPayload.eventId(), attendeeToEventPayload.attendeeId())
                .doOnError(error -> LOGGER.error("Could not add attendee to event,", error))
                .block());

        if (result) {

            ack.acknowledge();
        }

        LOGGER.trace("Result of the event {} -> {}", topicName, result);
    }

    @KafkaListener(topics = "${io.github.evaggelos99.ems.event.topic.remove-attendee}", groupId = "default-group",
            containerFactory = "kafkaManualAckListenerContainerFactory")
    void consumeRemoveAttendeeMessage(final ConsumerRecord<String, byte[]> payload, Acknowledgment ack, @Value("${io.github.evaggelos99.ems.event.topic.remove-attendee}") String topicName) {

        final AttendeeToEventPayload attendeeToEventPayload = (AttendeeToEventPayload) objectDeserializer.convertBytesToObject(payload.value());
        LOGGER.trace("we just got an event for topic {} and message {}", topicName, attendeeToEventPayload);
        final boolean result = Boolean.TRUE.equals(eventService.removeAttendee(attendeeToEventPayload.eventId(), attendeeToEventPayload.attendeeId())
                .doOnError(error -> LOGGER.error("Could not add attendee to event,", error))
                .block());

        if (result) {

            ack.acknowledge();
        }

        LOGGER.trace("Result of the event {} -> {}", topicName, result);
    }

}
