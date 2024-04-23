package org.com.ems.integrationtests.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import org.com.ems.EventManagementSystemApplication;
import org.com.ems.api.dto.AttendeeDto;
import org.com.ems.integrationtests.controllers.util.Attributes;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest(classes = EventManagementSystemApplication.class, webEnvironment = WebEnvironment.RANDOM_PORT)
public class AttendeeControllerIntegrationTest {

	private static final String HOSTNAME = "http://localhost";
	private static final String RELATIVE_ENDPOINT = "/attendee";

	@LocalServerPort
	private int port;

	@Autowired
	private TestRestTemplate restTemplate;

	@Order(value = 0)
	@Test
	public void postAttendeeThenPutAttendee_thenExpectToHave() throws JsonMappingException, JsonProcessingException {

		final AttendeeDto dto = new AttendeeDto(null, null, "first", "last", List.of());

		final ResponseEntity<String> responseEntity = this.restTemplate
				.postForEntity(HOSTNAME + ":" + this.port + RELATIVE_ENDPOINT, dto, String.class);

		@SuppressWarnings("unchecked")
		final HashMap<String, Object> result = new ObjectMapper().readValue(responseEntity.getBody(), HashMap.class);

		Assertions.assertEquals(201, responseEntity.getStatusCode().value());

		Assertions
				.assertTrue(Attributes.attendeeNotNullAttributes.stream().map(result::get).allMatch(Objects::nonNull));
	}

	@Order(value = 1)
	@Test
	public void testAllEmployees() {

		final ResponseEntity<?> ss = this.restTemplate.getForObject(HOSTNAME + ":" + this.port + RELATIVE_ENDPOINT,
				ResponseEntity.class);

		System.out.println(ss);
		Assertions.assertTrue(ss != null);

	}
}
