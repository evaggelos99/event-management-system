package org.com.ems.controller;

import static java.util.Objects.requireNonNull;

import java.util.UUID;

import org.com.ems.api.dao.Attendee;
import org.com.ems.controller.api.IAttendeeController;
import org.com.ems.controller.exceptions.ObjectNotFoundException;
import org.com.ems.controller.utils.CommonControllerUtils;
import org.com.ems.db.api.IAttendeeRepository;
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

	public AttendeeController(@Autowired IAttendeeRepository attendeeRepository) {

		this.attendeeRepository = requireNonNull(attendeeRepository);
	}

	@Override
	public Attendee postAttendee(Attendee attendee) {

		return attendeeRepository.save(attendee);
	}

	@Override
	public Attendee getAttendee(String attendeeId) {

		UUID uuid = CommonControllerUtils.stringToUUID(attendeeId);
		var optionalAttendee = attendeeRepository.findById(uuid);

		return optionalAttendee.orElseThrow(() -> new ObjectNotFoundException(uuid,Attendee.class));
	}

	@Override
	public Attendee updateAttendee(String attendeeId, Attendee attendee) {

		UUID uuid = CommonControllerUtils.stringToUUID(attendeeId);
		
		if (attendeeRepository.existsById(uuid)) {
			
			attendeeRepository.deleteById(uuid);
			return attendeeRepository.save(attendee);
		}

		throw new ObjectNotFoundException(uuid, Attendee.class);
	}

	@Override
	public void deleteAttendee(String attendeeId) {

		attendeeRepository.deleteById(CommonControllerUtils.stringToUUID(attendeeId));
	}

}
