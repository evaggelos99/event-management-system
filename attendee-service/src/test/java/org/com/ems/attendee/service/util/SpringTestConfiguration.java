package org.com.ems.attendee.service.util;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.com.ems.attendee.api.Attendee;
import org.com.ems.attendee.api.AttendeeDto;
import org.com.ems.attendee.api.repo.IAttendeeRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Configuration
@Profile("integration-tests")
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

}
