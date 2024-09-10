package io.github.evaggelos99.ems.event.api.repo;

import io.github.evaggelos99.ems.common.api.db.IRepository;
import io.github.evaggelos99.ems.event.api.Event;
import io.github.evaggelos99.ems.event.api.EventDto;

/**
 * Event's Repository
 *
 * @author Evangelos Georgiou
 *
 */
public interface IEventRepository extends IRepository<Event, EventDto> {

}
