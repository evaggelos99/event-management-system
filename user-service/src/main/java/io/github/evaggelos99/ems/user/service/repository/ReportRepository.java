package io.github.evaggelos99.ems.user.service.repository;

import io.github.evaggelos99.ems.common.api.db.ReportQueriesOperations;
import io.github.evaggelos99.ems.user.api.repo.IReportRepository;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.UUID;

@Component
public class ReportRepository implements IReportRepository {

    private final Map<ReportQueriesOperations, String> reportQueriesOperations;
    private final DatabaseClient databaseClient;

    public ReportRepository(final Map<ReportQueriesOperations, String> reportQueriesOperations, final DatabaseClient databaseClient) {
        this.reportQueriesOperations = reportQueriesOperations;
        this.databaseClient = databaseClient;
    }

    @Override
    public Mono<Object> attendeesCame(final UUID eventUuid) {
        return Mono.empty();
    }
}