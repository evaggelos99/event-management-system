package org.com.ems.services.impl;

import java.util.Optional;
import java.util.UUID;

import org.com.ems.api.domainobjects.EventType;
import org.com.ems.api.domainobjects.Organizer;
import org.com.ems.api.dto.OrganizerDto;
import org.com.ems.db.IOrganizerRepository;
import org.com.ems.util.RandomObjectGenerator;
import org.com.ems.util.SpringTestConfiguration;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = { SpringTestConfiguration.class })
@ActiveProfiles("service-tests")
class OrganizerServiceTest {

    @Autowired
    private IOrganizerRepository organizerRepository;

    private OrganizerService service;

    @BeforeEach
    void setUp() throws Exception {

	this.service = new OrganizerService(this.organizerRepository);

    }

    @Test
    void add_get_getAll_existsById_delete_existsById_invokedWithValidDto_thenExpectToBeAdded_thenExpectItCanBeFetched_thenExpectToFetchAll_thenExpectItExists_thenExpectItCanBeDeleted() {

	final OrganizerDto dto = RandomObjectGenerator.generateOrganizerDto(EventType.CONFERENCE);

	final Organizer organizer = Assertions.assertDoesNotThrow(() -> this.service.add(dto));

	Assertions.assertEquals(dto.uuid(), organizer.getUuid());
	Assertions.assertEquals(dto.denomination(), organizer.getDenomination());
	Assertions.assertEquals(dto.website(), organizer.getWebsite());
	Assertions.assertEquals(dto.information(), organizer.getInformation());
	Assertions.assertEquals(dto.eventTypes(), organizer.getEventTypes());
	Assertions.assertEquals(dto.contactInformation(), organizer.getContactInformation());

	final Optional<Organizer> optionalOrganizer = this.service.get(dto.uuid());

	Assertions.assertEquals(organizer, optionalOrganizer.orElseThrow(() -> new AssertionError("Optional is null")));

	Assertions.assertEquals(1, this.service.getAll().size());

	Assertions.assertTrue(() -> this.service.existsById(dto.uuid()));

	Assertions.assertDoesNotThrow(() -> this.service.delete(dto.uuid()));

    }

    @Test
    void existsById_invokedWithInvalidOrganizerId__thenExpectToNotExist() {

	final UUID organizerId = UUID.randomUUID();
	Assertions.assertFalse(() -> this.service.existsById(organizerId));

    }

    @Test
    void add_edit_delete_invokedWithValidDto_thenExpectToBeAdded_thenExpectItCanBeFetched_thenExpectToFetchAll_thenExpectItExists_thenExpectItCanBeDeleted() {

	final OrganizerDto dto = RandomObjectGenerator.generateOrganizerDto(EventType.values());

	final Organizer organizer = Assertions.assertDoesNotThrow(() -> this.service.add(dto));

	final OrganizerDto newDto = RandomObjectGenerator.generateOrganizerDto(EventType.OTHER);

	final Organizer newOrganizer = Assertions.assertDoesNotThrow(() -> this.service.edit(dto.uuid(), newDto));

	Assertions.assertNotEquals(organizer, newOrganizer);

	Assertions.assertDoesNotThrow(() -> this.service.delete(dto.uuid()));

    }

}
