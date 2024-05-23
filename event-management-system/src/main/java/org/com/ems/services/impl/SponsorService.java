package org.com.ems.services.impl;

import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

import org.com.ems.api.domainobjects.Sponsor;
import org.com.ems.api.dto.SponsorDto;
import org.com.ems.db.ISponsorRepository;
import org.com.ems.services.api.IService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SponsorService implements IService<Sponsor, SponsorDto> {

    private final ISponsorRepository sponsorRepository;

    public SponsorService(@Autowired final ISponsorRepository sponsorRepository) {

	this.sponsorRepository = sponsorRepository;

    }

    @Override
    public Sponsor add(final SponsorDto attendee) {

	return this.sponsorRepository.save(attendee);

    }

    @Override
    public Optional<Sponsor> get(final UUID uuid) {

	return this.sponsorRepository.findById(uuid);

    }

    @Override
    public void delete(final UUID uuid) {

	this.sponsorRepository.deleteById(uuid);

    }

    @Override
    public Sponsor edit(final UUID uuid,
			final SponsorDto attendee) {

	if (!this.sponsorRepository.existsById(uuid))
	    throw new NoSuchElementException();

	return this.sponsorRepository.edit(attendee);

    }

    @Override
    public Collection<Sponsor> getAll() {

	return this.sponsorRepository.findAll();

    }

    @Override
    public boolean existsById(final UUID attendeeId) {

	return this.sponsorRepository.existsById(attendeeId);

    }

}
