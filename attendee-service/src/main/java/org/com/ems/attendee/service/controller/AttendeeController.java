package org.com.ems.attendee.service.controller;

import static java.util.Objects.requireNonNull;

import java.net.URISyntaxException;
import java.util.UUID;
import java.util.function.Function;

import org.com.ems.attendee.api.Attendee;
import org.com.ems.attendee.api.AttendeeDto;
import org.com.ems.attendee.api.IAttendeeController;
import org.com.ems.attendee.api.service.IAttendeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Controller for CRUD operation for the DAO object {@link Attendee}
 *
 * @author Evangelos Georgiou
 */
@RestController
@RequestMapping(AttendeeController.ATTENDEE_PATH)
public class AttendeeController implements IAttendeeController {

    static final String ATTENDEE_PATH = "/attendee";

    private final IAttendeeService attendeeService;
    private final Function<Attendee, AttendeeDto> attendeeToAttendeeDtoConverter;

    /**
     * C-or
     *
     * @param attendeeService                service responsible for CRUD operations
     * @param attendeeToAttendeeDtoConverter converts attendee to DTO
     * @param attendeeDtoToAttendeeConverter converts DTO to attendee
     */
    public AttendeeController(@Autowired final IAttendeeService attendeeService,
			      @Autowired @Qualifier("attendeeToAttendeeDtoConverter") final Function<Attendee,
				      AttendeeDto> attendeeToAttendeeDtoConverter) {

	this.attendeeService = requireNonNull(attendeeService);
	this.attendeeToAttendeeDtoConverter = requireNonNull(attendeeToAttendeeDtoConverter);

    }

    /**
     * {@inheritDoc}
     *
     * @throws URISyntaxException
     */
    @Override
    public Mono<AttendeeDto> postAttendee(final AttendeeDto attendeeDto) {

	return this.attendeeService.add(attendeeDto).map(this.attendeeToAttendeeDtoConverter::apply);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Mono<AttendeeDto> getAttendee(final UUID attendeeId) {

	final Mono<Attendee> attendee = this.attendeeService.get(attendeeId);

	return attendee.map(this.attendeeToAttendeeDtoConverter::apply);

    }

    /**
     * {@inheritDoc}
     *
     */
    @Override
    public Mono<AttendeeDto> putAttendee(final UUID attendeeId,
					 final AttendeeDto attendeeDto) {

	final Mono<Attendee> attendee = this.attendeeService.edit(attendeeId, attendeeDto);

	return attendee.map(this.attendeeToAttendeeDtoConverter::apply);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Mono<?> deleteAttendee(final UUID attendeeId) {

	return this.attendeeService.delete(attendeeId);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Flux<AttendeeDto> getAttendees() {

	return this.attendeeService.getAll().map(this.attendeeToAttendeeDtoConverter::apply);

    }

    @Override
    public Mono<Boolean> addTicket(final UUID attendeeId,
				   final UUID ticketId) {

	return this.attendeeService.addTicket(attendeeId, ticketId);

    }

}
