package io.github.evaggelos99.ems.attendee.service;

import io.github.evaggelos99.ems.attendee.api.Attendee;
import io.github.evaggelos99.ems.attendee.api.Flow;
import io.github.evaggelos99.ems.attendee.api.repo.IAttendeeRepository;
import io.github.evaggelos99.ems.attendee.api.repo.IEmailRepository;
import io.github.evaggelos99.ems.attendee.service.remote.UserLookUpRemoteService;
import io.github.evaggelos99.ems.ticket.api.TicketDto;
import io.github.evaggelos99.ems.user.api.UserDto;
import jakarta.validation.constraints.NotNull;
import org.apache.commons.lang3.tuple.Pair;
import org.simplejavamail.api.mailer.Mailer;
import org.simplejavamail.email.EmailBuilder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

@Service
public class EmailService {

    private static final String ADMIN_EMAIL = "noreply@event-management-system.com";

    private final IAttendeeRepository attendeeRepository;
    private final UserLookUpRemoteService userLookUpRemoteService;
    private final Mailer mailer;
    private final IEmailRepository emailRepository;

    public EmailService(final IAttendeeRepository attendeeRepository, final UserLookUpRemoteService userLookUpRemoteService, final Mailer mailer, final IEmailRepository emailRepository) {

        this.attendeeRepository = attendeeRepository;
        this.userLookUpRemoteService = userLookUpRemoteService;
        this.mailer = mailer;
        this.emailRepository = emailRepository;
    }

    public Mono<Void> sendPurchaseTicketEmail(final UUID attendeeId, final TicketDto ticketDto) {

        return attendeeRepository.findById(attendeeId)
                .map(Attendee::getUuid)
                .flatMap(userLookUpRemoteService::lookUpEntity)
                .flatMap(att -> sendEmail(att, ticketDto))
                .flatMap(emailRepository::saveEmail);
    }

    private Mono<Flow.EmailDto> sendEmail(final UserDto attendeeUser, final TicketDto ticketDto) {

        final String fromName = "Event management System";
        final String toName = String.format("%s %s", attendeeUser.firstName(), attendeeUser.lastName());
        final String toEmail = attendeeUser.email();
        final String subject = "Ticket purchased!";
        final String body = String.format("Hi %s.\n" +
                "Hope you are doing well!\n" +
                "Your ticket was purchased successfully!" +
                "The type of ticket is %s and is it " + transferableOrNot(ticketDto.transferable()) + "." +
                "You are sitting in %s", attendeeUser.firstName(), ticketDto.ticketType(), ticketDto.seatInformation());

        return Mono.just(mailer.sendMail(EmailBuilder.startingBlank()
                .from(fromName, ADMIN_EMAIL)
                .to(toName, toEmail)
                .withSubject(subject)
                .withPlainText(body)
                        .buildEmail()).complete(null))
                .map(ignored -> new Flow.EmailDto(Pair.of(fromName, ADMIN_EMAIL), Pair.of(toName, toEmail), subject, body, null));
    }

    private String transferableOrNot(final @NotNull Boolean transferable) {
        return transferable ? "transferable" : "nontransferable";
    }

}
