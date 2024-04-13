package org.com.ems.db;

import java.util.UUID;

import org.com.ems.api.domainobjects.Sponsor;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Sponsor's Repository
 *
 * @author Evangelos Georgiou
 *
 */
public interface ISponsorRepository extends JpaRepository<Sponsor, UUID> {

}
