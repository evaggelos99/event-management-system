package io.github.evaggelos99.ems.event.api.repo;

import io.github.evaggelos99.ems.common.api.db.IRepository;
import io.github.evaggelos99.ems.common.api.transport.EventStreamPayload;
import io.github.evaggelos99.ems.event.api.Event;
import io.github.evaggelos99.ems.event.api.EventDto;
import io.github.evaggelos99.ems.event.api.EventStream;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

/**
 * Event's Repository
 *
 * @author Evangelos Georgiou
 */
public interface IEventRepository extends IRepository<Event, EventDto> {

    Mono<EventStream> saveOneEventStreamPayload(EventStreamPayload payload);

    Flux<EventStream> saveMultipleEventStreamPayload(List<EventStreamPayload> payload);

    Flux<EventStream> findAllEventStreams(UUID eventId);

}
