package com.github.evaggelos99.ems.event.api.repo;

import com.github.evaggelos99.ems.common.api.db.IRepository;
import com.github.evaggelos99.ems.event.api.Event;
import com.github.evaggelos99.ems.event.api.EventDto;

/**
 * Event's Repository
 *
 * @author Evangelos Georgiou
 *
 */
public interface IEventRepository extends IRepository<Event, EventDto> {

}
