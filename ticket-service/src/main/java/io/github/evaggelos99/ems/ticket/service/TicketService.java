package io.github.evaggelos99.ems.ticket.service;

import static java.util.Objects.requireNonNull;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.github.evaggelos99.ems.common.api.controller.exceptions.ObjectNotFoundException;
import io.github.evaggelos99.ems.common.api.service.IService;
import io.github.evaggelos99.ems.ticket.api.Ticket;
import io.github.evaggelos99.ems.ticket.api.TicketDto;
import io.github.evaggelos99.ems.ticket.api.repo.ITicketRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class TicketService implements IService<Ticket, TicketDto> {

	private final ITicketRepository ticketRepository;

	/**
	 * C-or
	 *
	 * @param ticketRepository {@link TicketRepository} the repository that
	 *                         communicates with the database
	 */
	public TicketService(@Autowired final ITicketRepository ticketRepository) {

		this.ticketRepository = requireNonNull(ticketRepository);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Mono<Ticket> add(final TicketDto ticketDto) {

		return ticketRepository.save(ticketDto);

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Mono<Ticket> get(final UUID uuid) {

		return ticketRepository.findById(uuid);

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Mono<Boolean> delete(final UUID uuid) {

		return ticketRepository.deleteById(uuid);

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Mono<Ticket> edit(final UUID uuid, final TicketDto ticketDto) {

		return !uuid.equals(ticketDto.uuid()) ? Mono.error(() -> new ObjectNotFoundException(uuid, TicketDto.class))
				: ticketRepository.edit(ticketDto);

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Flux<Ticket> getAll() {

		return ticketRepository.findAll();

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Mono<Boolean> existsById(final UUID ticketId) {

		return ticketRepository.existsById(ticketId);

	}

	@Override
	public Mono<Boolean> ping() {

		return Mono.just(true);
	}

}
