package org.com.ems.sponsor.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * Main class
 *
 * @author Evangelos Georgiou
 */
@SpringBootApplication(scanBasePackages = { "org.com.ems.common.api", "org.com.ems.sponsor.api",
	"org.com.ems.sponsor.service" })
public class SponsorServiceApplication extends SpringBootServletInitializer {

    public static void main(final String[] args) {

	SpringApplication.run(SponsorServiceApplication.class, args);

    }

}