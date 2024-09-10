package io.github.evaggelos99.ems.ticket.api.repo;

import io.github.evaggelos99.ems.common.api.db.IRepository;
import io.github.evaggelos99.ems.ticket.api.Ticket;
import io.github.evaggelos99.ems.ticket.api.TicketDto;

/**
 * Ticket's Repository
 *
 * @author Evangelos Georgiou
 *
 */
public interface ITicketRepository extends IRepository<Ticket, TicketDto> {

}
