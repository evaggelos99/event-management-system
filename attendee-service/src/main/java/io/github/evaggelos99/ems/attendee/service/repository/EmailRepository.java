package io.github.evaggelos99.ems.attendee.service.repository;

import io.github.evaggelos99.ems.attendee.api.Flow;
import io.github.evaggelos99.ems.attendee.api.repo.IEmailRepository;
import io.github.evaggelos99.ems.common.api.db.CrudQueriesOperations;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@Component
public class EmailRepository implements IEmailRepository {

    private final DatabaseClient databaseClient;
    private final Map<CrudQueriesOperations, String> emailQueriesProperties;

    public EmailRepository(final DatabaseClient databaseClient, @Qualifier("emailQueriesProperties") Map<CrudQueriesOperations, String> emailQueriesProperties) {
        this.databaseClient = databaseClient;
        this.emailQueriesProperties = emailQueriesProperties;
    }

    @Override
    public Mono<Void> saveEmail(final Flow.EmailDto emailDto) {

        DatabaseClient.GenericExecuteSpec bind = databaseClient.sql(emailQueriesProperties.get(CrudQueriesOperations.SAVE))
                .bind(0, emailDto.from().getValue())
                .bind(1, emailDto.to().getValue())
                .bind(2, emailDto.cc()!=null ? emailDto.cc().toArray() : new String[]{})
                .bind(3, emailDto.body())
                .bind(4, emailDto.from().getKey())
                .bind(5, emailDto.to().getKey())
                .bind(6, emailDto.subject());

        return bind
                .fetch()
                .rowsUpdated().then();
    }

    @Override
    public Flux<Flow.EmailDto> getEmails(final String email) {
        return databaseClient.sql(emailQueriesProperties.get(CrudQueriesOperations.GET_ALL))
                .bind(1, email)
                .map((r, rm) -> {

                    final Pair<String, String> from = Pair.of(r.get("from_name", String.class), r.get("from_email", String.class));
                    final Pair<String, String> to = Pair.of(r.get("to_name", String.class), r.get("to_email", String.class));
                    final String subject = r.get("subject", String.class);
                    final String body = r.get("body", String.class);
                    final Object cc = r.get("cc", Object.class);

                    System.out.println(cc);

                    return new Flow.EmailDto(from, to, subject,body, List.of());
                })
                .all();
    }
}
