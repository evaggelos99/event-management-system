package io.github.evaggelos99.ems.common.api.transport;

import java.io.Serializable;
import java.util.UUID;

public class AttendeeToEventPayload implements Serializable {

    private UUID eventId;
    private UUID attendeeId;

    /**
     * C-or used for serializing the object
     */
    public AttendeeToEventPayload() {

    }

    /**
     * C-or. used for cosntructing the object for transport
     *
     * @param eventId    the event {@link UUID}
     * @param attendeeId the attendee {@link UUID}
     */
    public AttendeeToEventPayload(UUID eventId, UUID attendeeId) {

        this.eventId = eventId;
        this.attendeeId = attendeeId;
    }

    public UUID eventId() {
        return eventId;
    }

    public UUID attendeeId() {
        return attendeeId;
    }

}
