package io.github.evaggelos99.ems.sponsor.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

/**
 * Main class
 *
 * @author Evangelos Georgiou
 */
@SpringBootApplication(scanBasePackages = {"io.github.evaggelos99.ems.common.api",
        "io.github.evaggelos99.ems.sponsor.api", "io.github.evaggelos99.ems.sponsor.service", "io.github.evaggelos99.ems.security.lib"})
public class SponsorServiceApplication {

    public static void main(final String[] args) {
        new SpringApplicationBuilder(SponsorServiceApplication.class).web(WebApplicationType.REACTIVE).run();
    }

}