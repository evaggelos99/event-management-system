package org.com.ems.db;

import java.util.UUID;

import org.com.ems.api.domainobjects.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Ticket's Repository
 *
 * @author Evangelos Georgiou
 *
 */
public interface ITicketRepository extends JpaRepository<Ticket, UUID> {

}
