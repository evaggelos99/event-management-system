package org.com.ems.util;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.com.ems.api.domainobjects.Attendee;
import org.com.ems.api.domainobjects.Event;
import org.com.ems.api.domainobjects.Organizer;
import org.com.ems.api.domainobjects.Sponsor;
import org.com.ems.api.domainobjects.Ticket;
import org.com.ems.api.dto.AttendeeDto;
import org.com.ems.api.dto.EventDto;
import org.com.ems.api.dto.OrganizerDto;
import org.com.ems.api.dto.SponsorDto;
import org.com.ems.api.dto.TicketDto;
import org.com.ems.db.IAttendeeRepository;
import org.com.ems.db.IEventRepository;
import org.com.ems.db.IOrganizerRepository;
import org.com.ems.db.ISponsorRepository;
import org.com.ems.db.ITicketRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("service-tests")
public class SpringTestConfiguration {

    @Bean
    IAttendeeRepository attendeeRepository() {

	return new IAttendeeRepository() {

	    Map<UUID, Attendee> list = new HashMap<>();

	    @Override
	    public Attendee save(final AttendeeDto dto) {

		final Attendee attendee = new Attendee(dto.uuid(), dto.createdAt().toInstant(),
			dto.lastUpdated().toInstant(), dto.firstName(), dto.lastName(), dto.ticketIDs());
		this.list.put(dto.uuid(), attendee);

		return attendee;

	    }

	    @Override
	    public Optional<Attendee> findById(final UUID uuid) {

		return Optional.ofNullable(this.list.get(uuid));

	    }

	    @Override
	    public Collection<Attendee> findAll() {

		return this.list.values();

	    }

	    @Override
	    public boolean existsById(final UUID uuid) {

		return this.list.containsKey(uuid);

	    }

	    @Override
	    public Attendee edit(final AttendeeDto dto) {

		final Attendee attendee = new Attendee(dto.uuid(), dto.createdAt().toInstant(),
			dto.lastUpdated().toInstant(), dto.firstName(), dto.lastName(), dto.ticketIDs());

		this.list.put(dto.uuid(), attendee);
		return attendee;

	    }

	    @Override
	    public boolean deleteById(final UUID uuid) {

		return this.list.remove(uuid) != null;

	    }
	};

    }

    @Bean
    IEventRepository eventRepository() {

	return new IEventRepository() {

	    Map<UUID, Event> list = new HashMap<>();

	    @Override
	    public Event save(final EventDto dto) {

		final Event attendee = new Event(dto.uuid(), dto.createdAt().toInstant(), dto.lastUpdated().toInstant(),
			dto.denomination(), dto.place(), dto.eventType(), dto.attendeesIds(), dto.organizerId(),
			dto.limitOfPeople(), dto.sponsorsIds(), dto.startTimeOfEvent(), dto.duration()

		);
		this.list.put(dto.uuid(), attendee);

		return attendee;

	    }

	    @Override
	    public Optional<Event> findById(final UUID uuid) {

		return Optional.ofNullable(this.list.get(uuid));

	    }

	    @Override
	    public Collection<Event> findAll() {

		return this.list.values();

	    }

	    @Override
	    public boolean existsById(final UUID uuid) {

		return this.list.containsKey(uuid);

	    }

	    @Override
	    public Event edit(final EventDto dto) {

		final Event attendee = new Event(dto.uuid(), dto.createdAt().toInstant(), dto.lastUpdated().toInstant(),
			dto.denomination(), dto.place(), dto.eventType(), dto.attendeesIds(), dto.organizerId(),
			dto.limitOfPeople(), dto.sponsorsIds(), dto.startTimeOfEvent(), dto.duration()

		);

		this.list.put(dto.uuid(), attendee);
		return attendee;

	    }

	    @Override
	    public boolean deleteById(final UUID uuid) {

		return this.list.remove(uuid) != null;

	    }
	};

    }

    @Bean
    IOrganizerRepository OrganizerRepository() {

	return new IOrganizerRepository() {

	    Map<UUID, Organizer> list = new HashMap<>();

	    @Override
	    public Organizer save(final OrganizerDto dto) {

		final Organizer Organizer = new Organizer(dto.uuid(), dto.createdAt().toInstant(),
			dto.lastUpdated().toInstant(), dto.denomination(), dto.website(), dto.information(),
			dto.eventTypes(), dto.contactInformation());

		this.list.put(dto.uuid(), Organizer);

		return Organizer;

	    }

	    @Override
	    public Optional<Organizer> findById(final UUID uuid) {

		return Optional.ofNullable(this.list.get(uuid));

	    }

	    @Override
	    public Collection<Organizer> findAll() {

		return this.list.values();

	    }

	    @Override
	    public boolean existsById(final UUID uuid) {

		return this.list.containsKey(uuid);

	    }

	    @Override
	    public Organizer edit(final OrganizerDto dto) {

		final Organizer Organizer = new Organizer(dto.uuid(), dto.createdAt().toInstant(),
			dto.lastUpdated().toInstant(), dto.denomination(), dto.website(), dto.information(),
			dto.eventTypes(), dto.contactInformation());

		this.list.put(dto.uuid(), Organizer);
		return Organizer;

	    }

	    @Override
	    public boolean deleteById(final UUID uuid) {

		return this.list.remove(uuid) != null;

	    }
	};

    }

    @Bean
    ITicketRepository ticketRepository() {

	return new ITicketRepository() {

	    Map<UUID, Ticket> list = new HashMap<>();

	    @Override
	    public Ticket save(final TicketDto dto) {

		final Ticket Ticket = new Ticket(dto.uuid(), dto.createdAt().toInstant(), dto.lastUpdated().toInstant(),
			dto.eventID(), dto.ticketType(), dto.price(), dto.transferable(), dto.seatInformation());

		this.list.put(dto.uuid(), Ticket);

		return Ticket;

	    }

	    @Override
	    public Optional<Ticket> findById(final UUID uuid) {

		return Optional.ofNullable(this.list.get(uuid));

	    }

	    @Override
	    public Collection<Ticket> findAll() {

		return this.list.values();

	    }

	    @Override
	    public boolean existsById(final UUID uuid) {

		return this.list.containsKey(uuid);

	    }

	    @Override
	    public Ticket edit(final TicketDto dto) {

		final Ticket Ticket = new Ticket(dto.uuid(), dto.createdAt().toInstant(), dto.lastUpdated().toInstant(),
			dto.eventID(), dto.ticketType(), dto.price(), dto.transferable(), dto.seatInformation());

		this.list.put(dto.uuid(), Ticket);
		return Ticket;

	    }

	    @Override
	    public boolean deleteById(final UUID uuid) {

		return this.list.remove(uuid) != null;

	    }
	};

    }

    @Bean
    ISponsorRepository sponsorRepository() {

	return new ISponsorRepository() {

	    Map<UUID, Sponsor> list = new HashMap<>();

	    @Override
	    public Sponsor save(final SponsorDto dto) {

		final Sponsor attendee = new Sponsor(dto.uuid(), dto.createdAt().toInstant(),
			dto.lastUpdated().toInstant(), dto.denomination(), dto.website(), dto.financialContribution(),
			dto.contactInformation());

		this.list.put(dto.uuid(), attendee);

		return attendee;

	    }

	    @Override
	    public Optional<Sponsor> findById(final UUID uuid) {

		return Optional.ofNullable(this.list.get(uuid));

	    }

	    @Override
	    public Collection<Sponsor> findAll() {

		return this.list.values();

	    }

	    @Override
	    public boolean existsById(final UUID uuid) {

		return this.list.containsKey(uuid);

	    }

	    @Override
	    public Sponsor edit(final SponsorDto dto) {

		final Sponsor attendee = new Sponsor(dto.uuid(), dto.createdAt().toInstant(),
			dto.lastUpdated().toInstant(), dto.denomination(), dto.website(), dto.financialContribution(),
			dto.contactInformation());

		this.list.put(dto.uuid(), attendee);
		return attendee;

	    }

	    @Override
	    public boolean deleteById(final UUID uuid) {

		return this.list.remove(uuid) != null;

	    }
	};

    }

}
