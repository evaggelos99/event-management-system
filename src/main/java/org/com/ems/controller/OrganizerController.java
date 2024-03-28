package org.com.ems.controller;

import static java.util.Objects.requireNonNull;

import java.util.UUID;

import org.com.ems.api.dao.Event;
import org.com.ems.api.dao.Organizer;
import org.com.ems.controller.api.IOrganizerController;
import org.com.ems.controller.exceptions.ObjectNotFoundException;
import org.com.ems.controller.utils.CommonControllerUtils;
import org.com.ems.db.IOrganizerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for CRUD operation for the DAO object {@link Organizer}
 * 
 * @author Evangelos Georgiou
 */
@RestController
@RequestMapping("/organizer")
public class OrganizerController implements IOrganizerController {

	private final IOrganizerRepository organizerRepository;

	public OrganizerController(@Autowired IOrganizerRepository organizerRepository) {

		this.organizerRepository = requireNonNull(organizerRepository);
	}

	@Override
	public Organizer postOrganizer(Organizer organizer) {

		return organizerRepository.save(organizer);
	}

	@Override
	public Organizer getOrganizer(String organizerId) {

		UUID uuid = CommonControllerUtils.stringToUUID(organizerId);
		return organizerRepository.findById(uuid)
				.orElseThrow(() -> new ObjectNotFoundException(uuid, Organizer.class));
	}

	@Override
	public Organizer updateOrganizer(String organizerId, Organizer organizer) {

		UUID uuid = CommonControllerUtils.stringToUUID(organizerId);
		if (organizerRepository.existsById(uuid)) {
			organizerRepository.deleteById(uuid);
			return organizerRepository.save(organizer);
		}

		throw new ObjectNotFoundException(uuid, Organizer.class);
	}

	@Override
	public void deleteOrganizer(String organizerId) {

		organizerRepository.deleteById(CommonControllerUtils.stringToUUID(organizerId));
	}

}
