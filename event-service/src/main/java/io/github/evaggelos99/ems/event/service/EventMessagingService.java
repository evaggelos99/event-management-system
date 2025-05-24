package io.github.evaggelos99.ems.event.service;

import io.github.evaggelos99.ems.common.api.transport.EventStreamPayload;
import io.github.evaggelos99.ems.event.api.repo.IEventRepository;
import io.github.evaggelos99.ems.event.api.service.IEventMessagingService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventMessagingService implements IEventMessagingService {

    private final IEventRepository eventRepository;

    public EventMessagingService(final IEventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    @Override
    public void saveOneEventStreamPayload(final EventStreamPayload eventStreamPayload) {
        eventRepository.saveOneEventStreamPayload(eventStreamPayload).block();
    }

    @Override
    public void saveMultipleEventStreamPayload(final List<EventStreamPayload> eventStreamPayload) {
        eventRepository.saveMultipleEventStreamPayload(eventStreamPayload).subscribe();
    }
}
