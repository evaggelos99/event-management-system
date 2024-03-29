package org.com.ems.controller;

import static java.util.Objects.requireNonNull;

import java.util.Collection;
import java.util.UUID;

import org.com.ems.api.dao.Attendee;
import org.com.ems.controller.api.IAttendeeController;
import org.com.ems.controller.exceptions.ObjectNotFoundException;
import org.com.ems.controller.utils.CommonControllerUtils;
import org.com.ems.db.IAttendeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
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

	public AttendeeController(@Autowired final IAttendeeRepository attendeeRepository) {

		this.attendeeRepository = requireNonNull(attendeeRepository);
	}

	@Override
	public Attendee postAttendee(final Attendee attendee) {

		return this.attendeeRepository.save(attendee);
	}

	@Override
	public Attendee getAttendee(final String attendeeId) {

		final UUID uuid = CommonControllerUtils.stringToUUID(attendeeId);
		final var optionalAttendee = this.attendeeRepository.findById(uuid);

		return optionalAttendee.orElseThrow(() -> new ObjectNotFoundException(uuid, Attendee.class));
	}

	@Override
	public Attendee putAttendee(final String attendeeId, final Attendee attendee) {

		final UUID uuid = CommonControllerUtils.stringToUUID(attendeeId);

		if (this.attendeeRepository.existsById(uuid)) {

			return this.attendeeRepository.save(attendee);
		}

		throw new ObjectNotFoundException(uuid, Attendee.class);
	}

	@Override
	public void deleteAttendee(final String attendeeId) {

		this.attendeeRepository.deleteById(CommonControllerUtils.stringToUUID(attendeeId));
	}

	@Override
	public Collection<Attendee> getAttendees() {

		return this.attendeeRepository.findAll();
	}

}
