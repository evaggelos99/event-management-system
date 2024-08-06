package com.github.evaggelos99.ems.organizer.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * Main class
 *
 * @author Evangelos Georgiou
 */
@SpringBootApplication(scanBasePackages = { "com.github.evaggelos99.ems.common.api",
		"com.github.evaggelos99.ems.organizer.api", "com.github.evaggelos99.ems.organizer.service" }, exclude = {
				org.springframework.boot.autoconfigure.gson.GsonAutoConfiguration.class }) // work around to disable
// auto configuration for
// Gson used by eureka
public class OrganizerServiceApplication extends SpringBootServletInitializer {

	public static void main(final String[] args) {

		SpringApplication.run(OrganizerServiceApplication.class, args);

	}

}