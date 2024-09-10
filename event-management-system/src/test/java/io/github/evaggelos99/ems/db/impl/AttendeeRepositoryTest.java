//package io.github.evaggelos99.ems.db.impl;
//
//import static org.mockito.Mockito.when;
//
//import java.util.Properties;
//import java.util.function.Function;
//
//import org.com.ems.api.domainobjects.Attendee;
//import org.com.ems.api.dto.AttendeeDto;
//import org.com.ems.db.queries.Queries.CrudQueriesOperations;
//import org.com.ems.util.RandomObjectGenerator;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.Mock;
//import org.mockito.Mockito;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.jdbc.core.JdbcTemplate;
//import org.springframework.jdbc.core.RowMapper;
//
//@ExtendWith(MockitoExtension.class)
//class AttendeeRepositoryTest {
//
//    @Mock
//    private JdbcTemplate jdbcTemplateMock;
//    @Mock
//    private RowMapper<Attendee> attendeeRowMapperMock;
//    @Mock
//    private Function<AttendeeDto, Attendee> attendeeDtoToAttendeeConverterMock;
//    @Mock
//    private Properties attendeeQueriesPropertiesMock;
//
//    AttendeeRepository repository;
//
//    @BeforeEach
//    void setUp() {
//
//	this.repository = null;
//		new AttendeeRepository(this.jdbcTemplateMock, this.attendeeRowMapperMock,
//		this.attendeeDtoToAttendeeConverterMock, this.attendeeQueriesPropertiesMock);
//
//    }
//
//    @Test
//    void save_invokedWithDto_thenExpectForJdbcToBeInvokedAndDomainObjectToBeCorrect() {
//
//	final AttendeeDto dto = RandomObjectGenerator.generateAttendeeDto();
//	final Attendee expectedAttendee = RandomObjectGenerator.generateAttendee();
//
//	final String sql = "SQL SAVE QUERY";
//
//	when(this.attendeeQueriesPropertiesMock.getProperty(CrudQueriesOperations.SAVE.name())).thenReturn(sql);
//
//	when(this.attendeeDtoToAttendeeConverterMock.apply(Mockito.any(AttendeeDto.class)))
//		.thenReturn(expectedAttendee);
//
//	final Attendee actualAttendee = this.repository.save(dto);
//
//	Mockito.verify(this.jdbcTemplateMock).update(Mockito.eq(sql), Mockito.eq(dto.uuid()), Mockito.any(),
//		Mockito.any(), Mockito.eq(dto.firstName()), Mockito.eq(dto.lastName()), Mockito.any());
//
//	Assertions.assertEquals(expectedAttendee.getFirstName(), actualAttendee.getFirstName());
//	Assertions.assertEquals(expectedAttendee.getLastName(), actualAttendee.getLastName());
//	Assertions.assertEquals(expectedAttendee.getTicketIDs(), actualAttendee.getTicketIDs());
//	Assertions.assertEquals(expectedAttendee.getUuid(), actualAttendee.getUuid());
//
//    }
//
//}
