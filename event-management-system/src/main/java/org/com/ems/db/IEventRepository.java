package org.com.ems.db;

import java.util.UUID;

import org.com.ems.api.domainobjects.Event;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Event's Repository
 *
 * @author Evangelos Georgiou
 *
 */
public interface IEventRepository extends JpaRepository<Event, UUID> {

}
