package org.com.ems.services;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

import org.com.ems.api.domainobjects.Sponsor;
import org.com.ems.api.dto.SponsorDto;
import org.springframework.stereotype.Service;

@Service
public class SponsorService implements IService<Sponsor, SponsorDto> {

    @Override
    public Sponsor add(final SponsorDto object) {

	// TODO Auto-generated method stub
	return null;

    }

    @Override
    public Optional<Sponsor> get(final UUID uuid) {

	// TODO Auto-generated method stub
	return Optional.empty();

    }

    @Override
    public void delete(final UUID uuid) {

	// TODO Auto-generated method stub

    }

    @Override
    public Sponsor edit(final UUID uuid,
			final SponsorDto object) {

	// TODO Auto-generated method stub
	return null;

    }

    @Override
    public Collection<Sponsor> getAll() {

	// TODO Auto-generated method stub
	return null;

    }

    @Override
    public boolean existsById(final UUID objectId) {

	// TODO Auto-generated method stub
	return false;

    }

//    private final ISponsorRepository sponsorRepository;
//
//    public SponsorService(@Autowired final ISponsorRepository sponsorRepository) {
//
//	this.sponsorRepository = sponsorRepository;
//
//    }
//
//    @Override
//    public Sponsor add(final Sponsor attendee) {
//
//	return this.sponsorRepository.save(attendee);
//
//    }
//
//    @Override
//    public Optional<Sponsor> get(final UUID uuid) {
//
//	return this.sponsorRepository.findById(uuid);
//
//    }
//
//    @Override
//    public void delete(final UUID uuid) {
//
//	this.sponsorRepository.deleteById(uuid);
//
//    }
//
//    @Override
//    public Sponsor edit(final UUID uuid,
//			final Sponsor attendee) {
//
//	if (!this.sponsorRepository.existsById(uuid))
//	    throw new NoSuchElementException();
//
//	return this.sponsorRepository.save(attendee);
//
//    }
//
//    @Override
//    public Collection<Sponsor> getAll() {
//
//	return this.sponsorRepository.findAll();
//
//    }
//
//    @Override
//    public boolean existsById(final UUID attendeeId) {
//
//	return this.sponsorRepository.existsById(attendeeId);
//
//    }

}
