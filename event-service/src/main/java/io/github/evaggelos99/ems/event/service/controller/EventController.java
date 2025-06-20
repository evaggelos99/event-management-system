package io.github.evaggelos99.ems.event.service.controller;

import io.github.evaggelos99.ems.event.api.*;
import io.github.evaggelos99.ems.event.api.service.IEventService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;
import java.util.function.Function;

import static java.util.Objects.requireNonNull;

/**
 * Controller for CRUD operation for the object {@link Event}
 *
 * @author Evangelos Georgiou
 */
@RestController
@RequestMapping(EventController.EVENT_PATH)
public class EventController implements IEventController {

    static final String EVENT_PATH = "/event";
    private final IEventService eventService;
    private final Function<Event, EventDto> eventToEventDtoConverter;
    private final Function<EventStream, EventStreamDto> eventStreamToEventStreamDtoConverter;

    /**
     * C-or
     *
     * @param eventService             service responsible for CRUD operations
     * @param eventToEventDtoConverter converts event to DTO
     */
    public EventController(final IEventService eventService,
                           @Qualifier("eventToEventDtoConverter") final Function<Event, EventDto> eventToEventDtoConverter,
                           @Qualifier("eventStreamToEventStreamDtoConverter") Function<EventStream, EventStreamDto> eventStreamToEventStreamDtoConverter) {

        this.eventService = requireNonNull(eventService);
        this.eventToEventDtoConverter = requireNonNull(eventToEventDtoConverter);
        this.eventStreamToEventStreamDtoConverter = requireNonNull(eventStreamToEventStreamDtoConverter);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Mono<EventDto> postEvent(final EventDto eventDto) {

        return eventService.add(eventDto).map(eventToEventDtoConverter);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Mono<EventDto> getEvent(final UUID eventId) {

        return eventService.get(eventId).map(eventToEventDtoConverter);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Flux<EventDto> getEvents() {

        return eventService.getAll().map(eventToEventDtoConverter);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Mono<EventDto> putEvent(final UUID eventId, final EventDto eventDto) {

        return eventService.edit(eventId, eventDto).map(eventToEventDtoConverter);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Mono<ResponseEntity<Void>> deleteEvent(final UUID eventId) {

        return eventService.delete(eventId).filter(Boolean::booleanValue).map(this::mapResponseEntity);
    }

    @Override
    public Mono<ResponseEntity<Void>> removeSponsor(final UUID eventId, final UUID sponsorId) {

        return eventService.removeSponsor(eventId, sponsorId).filter(Boolean::booleanValue).map(this::mapResponseEntity);
    }

    @Override
    public Mono<ResponseEntity<Void>> addSponsor(final UUID eventId, final UUID sponsorId) {

        return eventService.addSponsor(eventId, sponsorId).filter(Boolean::booleanValue).map(this::mapResponseEntity);
    }

    @Override
    public Flux<EventStreamDto> getEventStreams(UUID eventId) {

        return eventService.getEventStreams(eventId).map(eventStreamToEventStreamDtoConverter);
    }

    @Override
    public Mono<ResponseEntity<Void>> ping() {

        return eventService.ping().filter(Boolean::booleanValue).map(this::mapResponseEntity);
    }

    private ResponseEntity<Void> mapResponseEntity(Boolean ignored) {
        return ResponseEntity.ok().build();
    }

}
