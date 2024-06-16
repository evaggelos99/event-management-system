package org.com.ems.util;

import java.util.HashMap;
import java.util.Map;
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

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Configuration
@Profile("service-tests")
public class SpringTestConfiguration {

    @Bean
    IAttendeeRepository attendeeRepository() {

	return new IAttendeeRepository() {

	    Map<UUID, Attendee> list = new HashMap<>();

	    @Override
	    public Mono<Attendee> save(final AttendeeDto dto) {

		final Attendee attendee = new Attendee(dto.uuid(), dto.createdAt().toInstant(),
			dto.lastUpdated().toInstant(), dto.firstName(), dto.lastName(), dto.ticketIDs());
		this.list.put(dto.uuid(), attendee);

		return Mono.just(attendee);

	    }

	    @Override
	    public Mono<Attendee> findById(final UUID uuid) {

		return Mono.just(this.list.get(uuid));

	    }

	    @Override
	    public Flux<Attendee> findAll() {

		return Flux.fromIterable(this.list.values());

	    }

	    @Override
	    public Mono<Boolean> existsById(final UUID uuid) {

		return Mono.just(this.list.containsKey(uuid));

	    }

	    @Override
	    public Mono<Attendee> edit(final AttendeeDto dto) {

		final Attendee attendee = new Attendee(dto.uuid(), dto.createdAt().toInstant(),
			dto.lastUpdated().toInstant(), dto.firstName(), dto.lastName(), dto.ticketIDs());

		this.list.put(dto.uuid(), attendee);
		return Mono.just(attendee);

	    }

	    @Override
	    public Mono<Boolean> deleteById(final UUID uuid) {

		return Mono.just(this.list.remove(uuid) != null);

	    }
	};

    }

    @Bean
    IEventRepository eventRepository() {

	return new IEventRepository() {

	    Map<UUID, Event> list = new HashMap<>();

	    @Override
	    public Mono<Event> save(final EventDto dto) {

		final Event attendee = new Event(dto.uuid(), dto.createdAt().toInstant(), dto.lastUpdated().toInstant(),
			dto.denomination(), dto.place(), dto.eventType(), dto.attendeesIds(), dto.organizerId(),
			dto.limitOfPeople(), dto.sponsorsIds(), dto.startTimeOfEvent(), dto.duration()

		);
		this.list.put(dto.uuid(), attendee);

		return Mono.just(attendee);

	    }

	    @Override
	    public Mono<Event> findById(final UUID uuid) {

		return Mono.just(this.list.get(uuid));

	    }

	    @Override
	    public Flux<Event> findAll() {

		return Flux.fromIterable(this.list.values());

	    }

	    @Override
	    public Mono<Boolean> existsById(final UUID uuid) {

		return Mono.just(this.list.containsKey(uuid));

	    }

	    @Override
	    public Mono<Event> edit(final EventDto dto) {

		final Event attendee = new Event(dto.uuid(), dto.createdAt().toInstant(), dto.lastUpdated().toInstant(),
			dto.denomination(), dto.place(), dto.eventType(), dto.attendeesIds(), dto.organizerId(),
			dto.limitOfPeople(), dto.sponsorsIds(), dto.startTimeOfEvent(), dto.duration()

		);

		this.list.put(dto.uuid(), attendee);
		return Mono.just(attendee);

	    }

	    @Override
	    public Mono<Boolean> deleteById(final UUID uuid) {

		return Mono.just(this.list.remove(uuid) != null);

	    }
	};

    }

    @Bean
    IOrganizerRepository OrganizerRepository() {

	return new IOrganizerRepository() {

	    Map<UUID, Organizer> list = new HashMap<>();

	    @Override
	    public Mono<Organizer> save(final OrganizerDto dto) {

		final Organizer organizer = new Organizer(dto.uuid(), dto.createdAt().toInstant(),
			dto.lastUpdated().toInstant(), dto.denomination(), dto.website(), dto.information(),
			dto.eventTypes(), dto.contactInformation());

		this.list.put(dto.uuid(), organizer);

		return Mono.just(organizer);

	    }

	    @Override
	    public Mono<Organizer> findById(final UUID uuid) {

		return Mono.just(this.list.get(uuid));

	    }

	    @Override
	    public Flux<Organizer> findAll() {

		return Flux.fromIterable(this.list.values());

	    }

	    @Override
	    public Mono<Boolean> existsById(final UUID uuid) {

		return Mono.just(this.list.containsKey(uuid));

	    }

	    @Override
	    public Mono<Organizer> edit(final OrganizerDto dto) {

		final Organizer organizer = new Organizer(dto.uuid(), dto.createdAt().toInstant(),
			dto.lastUpdated().toInstant(), dto.denomination(), dto.website(), dto.information(),
			dto.eventTypes(), dto.contactInformation());

		this.list.put(dto.uuid(), organizer);
		return Mono.just(organizer);

	    }

	    @Override
	    public Mono<Boolean> deleteById(final UUID uuid) {

		return Mono.just(this.list.remove(uuid) != null);

	    }
	};

    }

    @Bean
    ITicketRepository ticketRepository() {

	return new ITicketRepository() {

	    Map<UUID, Ticket> list = new HashMap<>();

	    @Override
	    public Mono<Ticket> save(final TicketDto dto) {

		final Ticket ticket = new Ticket(dto.uuid(), dto.createdAt().toInstant(), dto.lastUpdated().toInstant(),
			dto.eventID(), dto.ticketType(), dto.price(), dto.transferable(), dto.seatInformation());

		this.list.put(dto.uuid(), ticket);

		return Mono.just(ticket);

	    }

	    @Override
	    public Mono<Ticket> findById(final UUID uuid) {

		return Mono.just(this.list.get(uuid));

	    }

	    @Override
	    public Flux<Ticket> findAll() {

		return Flux.fromIterable(this.list.values());

	    }

	    @Override
	    public Mono<Boolean> existsById(final UUID uuid) {

		return Mono.just(this.list.containsKey(uuid));

	    }

	    @Override
	    public Mono<Ticket> edit(final TicketDto dto) {

		final Ticket ticket = new Ticket(dto.uuid(), dto.createdAt().toInstant(), dto.lastUpdated().toInstant(),
			dto.eventID(), dto.ticketType(), dto.price(), dto.transferable(), dto.seatInformation());

		this.list.put(dto.uuid(), ticket);
		return Mono.just(ticket);

	    }

	    @Override
	    public Mono<Boolean> deleteById(final UUID uuid) {

		return Mono.just(this.list.remove(uuid) != null);

	    }
	};

    }

    @Bean
    ISponsorRepository sponsorRepository() {

	return new ISponsorRepository() {

	    Map<UUID, Sponsor> list = new HashMap<>();

	    @Override
	    public Mono<Sponsor> save(final SponsorDto dto) {

		final Sponsor sponsor = new Sponsor(dto.uuid(), dto.createdAt().toInstant(),
			dto.lastUpdated().toInstant(), dto.denomination(), dto.website(), dto.financialContribution(),
			dto.contactInformation());

		this.list.put(dto.uuid(), sponsor);

		return Mono.just(sponsor);

	    }

	    @Override
	    public Mono<Sponsor> findById(final UUID uuid) {

		return Mono.just(this.list.get(uuid));

	    }

	    @Override
	    public Flux<Sponsor> findAll() {

		return Flux.fromIterable(this.list.values());

	    }

	    @Override
	    public Mono<Boolean> existsById(final UUID uuid) {

		return Mono.just(this.list.containsKey(uuid));

	    }

	    @Override
	    public Mono<Sponsor> edit(final SponsorDto dto) {

		final Sponsor sponsor = new Sponsor(dto.uuid(), dto.createdAt().toInstant(),
			dto.lastUpdated().toInstant(), dto.denomination(), dto.website(), dto.financialContribution(),
			dto.contactInformation());

		this.list.put(dto.uuid(), sponsor);
		return Mono.just(sponsor);

	    }

	    @Override
	    public Mono<Boolean> deleteById(final UUID uuid) {

		return Mono.just(this.list.remove(uuid) != null);

	    }
	};

    }

}
