package io.github.evaggelos99.ems.user.service;

import io.github.evaggelos99.ems.user.api.repo.IReportRepository;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Component
public class ReportRepository implements IReportRepository {

    @Override
    public Mono<Object> attendeesCame(final UUID eventUuid) {
        return null;
    }
}