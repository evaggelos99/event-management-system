package org.com.ems.event.api.repo;

import org.com.ems.common.api.db.IRepository;
import org.com.ems.event.api.Event;
import org.com.ems.event.api.EventDto;

/**
 * Event's Repository
 *
 * @author Evangelos Georgiou
 *
 */
public interface IEventRepository extends IRepository<Event, EventDto> {

}
