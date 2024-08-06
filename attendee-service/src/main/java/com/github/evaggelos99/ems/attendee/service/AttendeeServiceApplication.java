package com.github.evaggelos99.ems.attendee.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * Main class
 *
 * @author Evangelos Georgiou
 */
@SpringBootApplication(scanBasePackages = { "com.github.evaggelos99.ems.common.api",
		"com.github.evaggelos99.ems.attendee.api", "com.github.evaggelos99.ems.attendee.service" })
public class AttendeeServiceApplication extends SpringBootServletInitializer {

	public static void main(final String[] args) {

		SpringApplication.run(AttendeeServiceApplication.class, args);

	}

}