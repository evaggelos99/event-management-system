package io.github.evaggelos99.ems.ticket.api;

import io.github.evaggelos99.ems.common.api.service.IService;
import org.springframework.core.io.FileSystemResource;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface ITicketService extends IService<Ticket, TicketDto> {

    Mono<FileSystemResource> picture(UUID ticketId);

    Mono<Boolean> useTicket(UUID ticketId);
}
