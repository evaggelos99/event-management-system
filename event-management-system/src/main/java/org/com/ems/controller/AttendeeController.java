package org.com.ems.controller;

import static java.util.Objects.requireNonNull;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;

import org.com.ems.api.domainobjects.Attendee;
import org.com.ems.api.dto.AttendeeDto;
import org.com.ems.controller.api.IAttendeeController;
import org.com.ems.controller.exceptions.ObjectNotFoundException;
import org.com.ems.db.IAttendeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for CRUD operation for the DAO object {@link Attendee}
 *
 * @author Evangelos Georgiou
 */
@RestController
@RequestMapping("/attendee")
public class AttendeeController implements IAttendeeController {

	private final IAttendeeRepository attendeeRepository;
	private final Function<Attendee, AttendeeDto> attendeeToAttendeeDtoConverter;
	private final Function<AttendeeDto, Attendee> attendeeDtoToAttendeeConverter;

	public AttendeeController(@Autowired final IAttendeeRepository attendeeRepository,
			@Autowired @Qualifier("attendeeToAttendeeDtoConverter") final Function<Attendee, AttendeeDto> attendeeToAttendeeDtoConverter,
			@Autowired @Qualifier("attendeeDtoToAttendeeConverter") final Function<AttendeeDto, Attendee> attendeeDtoToAttendeeConverter) {

		this.attendeeRepository = requireNonNull(attendeeRepository);
		this.attendeeToAttendeeDtoConverter = requireNonNull(attendeeToAttendeeDtoConverter);
		this.attendeeDtoToAttendeeConverter = requireNonNull(attendeeDtoToAttendeeConverter);
	}

	/**
	 * {@inheritDoc}
	 *
	 * @throws URISyntaxException
	 */
	@Override
	public ResponseEntity<AttendeeDto> postAttendee(final AttendeeDto attendeeDto) {

		final Attendee attendee = this.attendeeRepository.save(this.attendeeDtoToAttendeeConverter.apply(attendeeDto));
		final AttendeeDto newDto = this.attendeeToAttendeeDtoConverter.apply(attendee);

		try {

			return ResponseEntity.created(new URI("/attendee/")).body(newDto);
		} catch (final URISyntaxException e) {

			return new ResponseEntity<>(newDto, HttpStatus.CREATED);
		}

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ResponseEntity<AttendeeDto> getAttendee(final UUID attendeeId) {

		final var optionalAttendee = this.attendeeRepository.findById(attendeeId);

		final AttendeeDto attendeeDto = this.attendeeToAttendeeDtoConverter
				.apply(optionalAttendee.orElseThrow(() -> new ObjectNotFoundException(attendeeId, AttendeeDto.class)));

		return ResponseEntity.ok(attendeeDto);
	}

	/**
	 * {@inheritDoc}
	 *
	 */
	@Override
	public ResponseEntity<AttendeeDto> putAttendee(final UUID attendeeId, final AttendeeDto attendeeDto) {

		if (this.attendeeRepository.existsById(attendeeId)) {

			final Attendee attendee = this.attendeeRepository
					.save(this.attendeeDtoToAttendeeConverter.apply(attendeeDto));

			final AttendeeDto newDto = this.attendeeToAttendeeDtoConverter.apply(attendee);

			try {
				return ResponseEntity.created(new URI("/attendee/" + attendeeId)).body(newDto);
			} catch (final URISyntaxException e) {

				return new ResponseEntity<>(newDto, HttpStatus.CREATED);
			}
		}

		throw new ObjectNotFoundException(attendeeId, Attendee.class);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ResponseEntity<?> deleteAttendee(final UUID attendeeId) {

		if (!this.attendeeRepository.existsById(attendeeId)) {

			throw new ObjectNotFoundException(attendeeId, Attendee.class);
		}

		this.attendeeRepository.deleteById(attendeeId);

		return ResponseEntity.noContent().build();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ResponseEntity<Collection<AttendeeDto>> getAttendees() {

		final List<AttendeeDto> listOfDtos = this.attendeeRepository.findAll().stream()
				.map(this.attendeeToAttendeeDtoConverter::apply).toList();

		return ResponseEntity.ok(listOfDtos);
	}

}
