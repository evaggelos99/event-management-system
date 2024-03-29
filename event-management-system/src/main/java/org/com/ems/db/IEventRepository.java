package org.com.ems.db;

import java.util.UUID;

import org.com.ems.api.dao.Event;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IEventRepository extends JpaRepository<Event, UUID> {

}
