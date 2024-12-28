package io.github.evaggelos99.ems.event.service.controller;

import io.github.evaggelos99.ems.event.api.Event;
import io.github.evaggelos99.ems.event.api.EventDto;
import io.github.evaggelos99.ems.event.api.IEventController;
import io.github.evaggelos99.ems.event.api.service.IEventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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

    /**
     * C-or
     *
     * @param eventService             service responsible for CRUD operations
     * @param eventToEventDtoConverter converts event to DTO
     * @param eventDtoToEventConverter converts DTO to event
     */
    public EventController(@Autowired final IEventService eventService,
                           @Autowired @Qualifier("eventToEventDtoConverter") final Function<Event, EventDto> eventToEventDtoConverter) {

        this.eventService = requireNonNull(eventService);
        this.eventToEventDtoConverter = requireNonNull(eventToEventDtoConverter);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Mono<EventDto> postEvent(final EventDto eventDto) {

        return eventService.add(eventDto).map(eventToEventDtoConverter::apply);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Mono<EventDto> getEvent(final UUID eventId) {

        return eventService.get(eventId).map(eventToEventDtoConverter::apply);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Mono<EventDto> putEvent(final UUID eventId, final EventDto eventDto) {

        return eventService.edit(eventId, eventDto).map(eventToEventDtoConverter::apply);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Mono<?> deleteEvent(final UUID eventId) {

        return eventService.delete(eventId);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Flux<EventDto> getEvents() {

        return eventService.getAll().map(eventToEventDtoConverter::apply);

    }

    @Override
    public Mono<Boolean> addAttendee(final UUID eventId, final UUID attendeeId) {

        return eventService.addAttendee(eventId, attendeeId);

    }

    @Override
    public Mono<Boolean> ping() {

        return pingService();
    }

    private Mono<Boolean> pingService() {
        try {

            return eventService.ping().log().onErrorReturn(false);
        } catch (final Exception e) {

            return Mono.just(false);
        }
    }

}
