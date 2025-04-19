package io.github.evaggelos99.ems.event.api.service;

import io.github.evaggelos99.ems.common.api.transport.EventStreamPayload;

import java.util.List;

public interface IEventMessagingService {

    /**
     *
     * @param eventStreamPayload
     */
    void saveOneEventStreamPayload(EventStreamPayload eventStreamPayload);

    /**
     *
     * @param eventStreamPayload
     */
    void saveMultipleEventStreamPayload(List<EventStreamPayload> eventStreamPayload);

}
