package org.com.ems.db;

import java.util.UUID;

import org.com.ems.api.dao.Organizer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IOrganizerRepository extends JpaRepository<Organizer, UUID> {

}
