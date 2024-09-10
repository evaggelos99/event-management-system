package io.github.evaggelos99.ems.sponsor.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * Main class
 *
 * @author Evangelos Georgiou
 */
@SpringBootApplication(scanBasePackages = { "io.github.evaggelos99.ems.common.api",
		"io.github.evaggelos99.ems.sponsor.api", "io.github.evaggelos99.ems.sponsor.service" }, exclude = {
				org.springframework.boot.autoconfigure.gson.GsonAutoConfiguration.class }) // work around to disable
// auto configuration for
// Gson used by eureka
public class SponsorServiceApplication extends SpringBootServletInitializer {

	public static void main(final String[] args) {

		SpringApplication.run(SponsorServiceApplication.class, args);

	}

}