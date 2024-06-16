package org.com.ems.controller;

import static java.util.Objects.requireNonNull;

import java.util.UUID;
import java.util.function.Function;

import org.com.ems.api.domainobjects.Event;
import org.com.ems.api.dto.EventDto;
import org.com.ems.controller.api.IEventController;
import org.com.ems.services.api.IEventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Controller for CRUD operation for the DAO object {@link Event}
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
			   @Autowired @Qualifier("eventToEventDtoConverter") final Function<Event,
				   EventDto> eventToEventDtoConverter) {

	this.eventService = requireNonNull(eventService);
	this.eventToEventDtoConverter = requireNonNull(eventToEventDtoConverter);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Mono<EventDto> postEvent(final EventDto eventDto) {

	return this.eventService.add(eventDto).map(this.eventToEventDtoConverter::apply);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Mono<EventDto> getEvent(final UUID eventId) {

	return this.eventService.get(eventId).map(this.eventToEventDtoConverter::apply);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Mono<EventDto> putEvent(final UUID eventId,
				   final EventDto eventDto) {

	return this.eventService.edit(eventId, eventDto).map(this.eventToEventDtoConverter::apply);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Mono<?> deleteEvent(final UUID eventId) {

	return this.eventService.delete(eventId);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Flux<EventDto> getEvents() {

	return this.eventService.getAll().map(this.eventToEventDtoConverter::apply);

    }

}
