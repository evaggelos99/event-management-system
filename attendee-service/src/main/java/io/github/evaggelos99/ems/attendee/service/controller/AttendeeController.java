package io.github.evaggelos99.ems.attendee.service.controller;

import io.github.evaggelos99.ems.attendee.api.Attendee;
import io.github.evaggelos99.ems.attendee.api.AttendeeDto;
import io.github.evaggelos99.ems.attendee.api.IAttendeeController;
import io.github.evaggelos99.ems.attendee.api.service.IAttendeeService;
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
 * Controller for CRUD operation for the object {@link Attendee}
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
     */
    public AttendeeController(@Autowired final IAttendeeService attendeeService,
                              @Autowired @Qualifier("attendeeToAttendeeDtoConverter") final Function<Attendee, AttendeeDto> attendeeToAttendeeDtoConverter) {

        this.attendeeService = requireNonNull(attendeeService);
        this.attendeeToAttendeeDtoConverter = requireNonNull(attendeeToAttendeeDtoConverter);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Mono<AttendeeDto> postAttendee(final AttendeeDto attendeeDto) {

        return attendeeService.add(attendeeDto).map(attendeeToAttendeeDtoConverter);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Mono<AttendeeDto> getAttendee(final UUID attendeeId) {

        final Mono<Attendee> attendee = attendeeService.get(attendeeId);
        return attendee.map(attendeeToAttendeeDtoConverter).switchIfEmpty(Mono.empty());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Flux<AttendeeDto> getAttendees() {

        return attendeeService.getAll().map(attendeeToAttendeeDtoConverter);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Mono<AttendeeDto> putAttendee(final UUID attendeeId, final AttendeeDto attendeeDto) {

        final Mono<Attendee> attendee = attendeeService.edit(attendeeId, attendeeDto);
        return attendee.map(attendeeToAttendeeDtoConverter);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Mono<Boolean> deleteAttendee(final UUID attendeeId) {

        return attendeeService.delete(attendeeId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Mono<Boolean> addTicket(final UUID attendeeId, final UUID ticketId) {

        return attendeeService.addTicket(attendeeId, ticketId);
    }

    @Override
    public Mono<Boolean> pingOther() {

        return attendeeService.pingOther();
    }

    @Override
    public Mono<Boolean> ping() {

        return Mono.just(true).onErrorReturn(false);
    }
}
