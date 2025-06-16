package io.github.evaggelos99.ems.attendee.api.repo;

import io.github.evaggelos99.ems.attendee.api.Flow;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface IEmailRepository {

    /**
     * Save an email to the repository.
     * @param emailDto the email data transfer object containing the email details.
     * @return a {@link Mono} that completes when the email is saved.
     */
    Mono<Void> saveEmail(Flow.EmailDto emailDto);

    /**
     * Retrieve all emails associated with a specific email address.
     * @param email the email address to search for.
     * @return a {@link Flux} containing all emails associated with the given email address.
     */
    Flux<Flow.EmailDto> getEmails(String email);
}
