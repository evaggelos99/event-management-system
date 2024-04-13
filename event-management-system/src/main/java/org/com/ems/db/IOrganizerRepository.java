package org.com.ems.db;

import java.util.UUID;

import org.com.ems.api.domainobjects.Organizer;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Organizer's Repository
 *
 * @author Evangelos Georgiou
 *
 */
public interface IOrganizerRepository extends JpaRepository<Organizer, UUID> {

}
