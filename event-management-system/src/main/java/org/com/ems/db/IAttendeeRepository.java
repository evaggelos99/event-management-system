package org.com.ems.db;

import java.util.UUID;

import org.com.ems.api.dao.Attendee;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Attendee's Repository
 *
 * @author Evangelos Georgiou
 *
 */
public interface IAttendeeRepository extends JpaRepository<Attendee, UUID> {

}
