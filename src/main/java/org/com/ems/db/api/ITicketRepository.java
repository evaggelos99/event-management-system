package org.com.ems.db.api;

import java.util.UUID;

import org.com.ems.api.dao.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ITicketRepository extends JpaRepository<Ticket, UUID> {

}
