package org.com.ems.controller;

import static java.util.Objects.requireNonNull;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;

import org.com.ems.api.domainobjects.Event;
import org.com.ems.api.dto.EventDto;
import org.com.ems.controller.api.IEventController;
import org.com.ems.controller.exceptions.ObjectNotFoundException;
import org.com.ems.services.api.IEventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public ResponseEntity<EventDto> postEvent(final EventDto eventDto) {

	final Event event = this.eventService.add(eventDto);
	final EventDto newDto = this.eventToEventDtoConverter.apply(event);

	try {

	    return ResponseEntity.created(new URI(EVENT_PATH)).body(newDto);
	} catch (final URISyntaxException e) {

	    return new ResponseEntity<>(newDto, HttpStatus.CREATED);
	}

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResponseEntity<EventDto> getEvent(final UUID eventId) {

	final var optionalEvent = this.eventService.get(eventId);

	final EventDto eventDto = this.eventToEventDtoConverter
		.apply(optionalEvent.orElseThrow(() -> new ObjectNotFoundException(eventId, EventDto.class)));

	return ResponseEntity.ok(eventDto);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResponseEntity<EventDto> putEvent(final UUID eventId,
					     final EventDto eventDto) {

	final Event event = this.eventService.edit(eventId, eventDto);
	final EventDto newDto = this.eventToEventDtoConverter.apply(event);

	try {

	    return ResponseEntity.created(new URI(EVENT_PATH + eventId)).body(newDto);
	} catch (final URISyntaxException e) {

	    return new ResponseEntity<>(newDto, HttpStatus.CREATED);
	}

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResponseEntity<?> deleteEvent(final UUID eventId) {

	this.eventService.delete(eventId);

	return ResponseEntity.noContent().build();

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResponseEntity<Collection<EventDto>> getEvents() {

	final List<EventDto> listOfDtos = this.eventService.getAll().stream().map(this.eventToEventDtoConverter::apply)
		.toList();

	return ResponseEntity.ok(listOfDtos);

    }

}
