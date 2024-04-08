package org.com.ems.db;

import java.util.UUID;

import org.com.ems.api.dao.Sponsor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ISponsorRepository extends JpaRepository<Sponsor, UUID> {

}
