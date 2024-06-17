package org.com.ems.ticket.api.repo;

import org.com.ems.common.api.db.IRepository;
import org.com.ems.ticket.api.Ticket;
import org.com.ems.ticket.api.TicketDto;

/**
 * Ticket's Repository
 *
 * @author Evangelos Georgiou
 *
 */
public interface ITicketRepository extends IRepository<Ticket, TicketDto> {

}
