package com.github.evaggelos99.ems.ticket.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * Main class
 *
 * @author Evangelos Georgiou
 */
@SpringBootApplication(scanBasePackages = { "com.github.evaggelos99.ems.common.api",
		"com.github.evaggelos99.ems.ticket.api", "com.github.evaggelos99.ems.ticket.service" }, exclude = {
				org.springframework.boot.autoconfigure.gson.GsonAutoConfiguration.class }) // work around to disable
// auto configuration for
// Gson used by eureka
public class TicketServiceApplication extends SpringBootServletInitializer {

	public static void main(final String[] args) {

		SpringApplication.run(TicketServiceApplication.class, args);

	}

}