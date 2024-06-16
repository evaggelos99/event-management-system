//package org.com.ems.services.impl;
//
//import java.util.NoSuchElementException;
//import java.util.Optional;
//import java.util.UUID;
//
//import org.com.ems.api.domainobjects.Sponsor;
//import org.com.ems.api.dto.SponsorDto;
//import org.com.ems.db.ISponsorRepository;
//import org.com.ems.util.RandomObjectGenerator;
//import org.com.ems.util.SpringTestConfiguration;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.test.context.ContextConfiguration;
//import org.springframework.test.context.junit.jupiter.SpringExtension;
//
//@ExtendWith(SpringExtension.class)
//@ContextConfiguration(classes = { SpringTestConfiguration.class })
//@ActiveProfiles("service-tests")
//class SponsorServiceTest {
//
//    @Autowired
//    private ISponsorRepository sponsorRepository;
//
//    private SponsorService service;
//
//    @BeforeEach
//    void setUp() {
//
//	this.service = new SponsorService(this.sponsorRepository);
//
//    }
//
//    @Test
//    void add_get_getAll_existsById_delete_existsById_invokedWithValidDto_thenExpectToBeAdded_thenExpectItCanBeFetched_thenExpectToFetchAll_thenExpectItExists_thenExpectItCanBeDeleted() {
//
//	final SponsorDto dto = RandomObjectGenerator.generateSponsorDto();
//
//	final Sponsor sponsor = Assertions.assertDoesNotThrow(() -> this.service.add(dto));
//
//	Assertions.assertEquals(dto.uuid(), sponsor.getUuid());
//	Assertions.assertEquals(dto.denomination(), sponsor.getDenomination());
//	Assertions.assertEquals(dto.website(), sponsor.getWebsite());
//	Assertions.assertEquals(dto.financialContribution(), sponsor.getFinancialContribution());
//	Assertions.assertEquals(dto.contactInformation(), sponsor.getContactInformation());
//
//	final Optional<Sponsor> optionalSponsor = this.service.get(dto.uuid());
//
//	Assertions.assertEquals(sponsor, optionalSponsor.orElseThrow(() -> new AssertionError("Optional is null")));
//
//	Assertions.assertEquals(1, this.service.getAll().size());
//
//	Assertions.assertTrue(() -> this.service.existsById(dto.uuid()));
//
//	Assertions.assertDoesNotThrow(() -> this.service.delete(dto.uuid()));
//
//    }
//
//    @Test
//    void existsById_edit_invokedWithInvalidSponsorId__thenExpectToNotExist_thenExpectToThrow() {
//
//	final UUID sponsorId = UUID.randomUUID();
//
//	Assertions.assertFalse(() -> this.service.existsById(sponsorId));
//	Assertions.assertThrows(NoSuchElementException.class, () -> this.service.edit(sponsorId, null));
//
//    }
//
//    @Test
//    void add_edit_delete_invokedWithValidDto_thenExpectToBeAdded_thenExpectItCanBeFetched_thenExpectToFetchAll_thenExpectItExists_thenExpectItCanBeDeleted() {
//
//	final SponsorDto dto = RandomObjectGenerator.generateSponsorDto();
//
//	final Sponsor sponsor = Assertions.assertDoesNotThrow(() -> this.service.add(dto));
//
//	final SponsorDto newDto = RandomObjectGenerator.generateSponsorDto();
//
//	final Sponsor newSponsor = Assertions.assertDoesNotThrow(() -> this.service.edit(dto.uuid(), newDto));
//
//	Assertions.assertNotEquals(sponsor, newSponsor);
//
//	Assertions.assertDoesNotThrow(() -> this.service.delete(dto.uuid()));
//
//    }
//
//}
