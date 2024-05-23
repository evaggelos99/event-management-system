package org.com.ems.services.api;

import java.util.UUID;

import org.com.ems.api.domainobjects.Event;
import org.com.ems.api.dto.EventDto;

public interface IEventService extends IService<Event, EventDto> {

    /**
     * This method does not yet cascade
     *
     * @param dto
     * @return
     */
    boolean addAttendee(UUID eventId,
			UUID attendeeId);

}
