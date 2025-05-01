package io.github.evaggelos99.ems.user.api.repo;

import io.github.evaggelos99.ems.common.api.db.IRepository;
import io.github.evaggelos99.ems.user.api.User;
import io.github.evaggelos99.ems.user.api.UserDto;
import reactor.core.publisher.Mono;

import java.util.UUID;

/**
 * Repository that runs specific queries based on all database schemas
 *
 * @author Evangelos Georgiou
 */
public interface IReportRepository {

    /**
     *
     */
    Mono<Object> attendeesCame(UUID eventUuid);

}
