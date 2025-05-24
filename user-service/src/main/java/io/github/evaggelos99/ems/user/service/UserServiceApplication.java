package io.github.evaggelos99.ems.user.service;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

/**
 * Main class
 *
 * @author Evangelos Georgiou
 */
@SpringBootApplication(scanBasePackages = {"io.github.evaggelos99.ems.common.api.controller", "io.github.evaggelos99.ems.common.api.db",
        "io.github.evaggelos99.ems.user.api", "io.github.evaggelos99.ems.user.service", "io.github.evaggelos99.ems.security.lib"})
public class UserServiceApplication {

    public static void main(final String[] args) {

        new SpringApplicationBuilder(UserServiceApplication.class).web(WebApplicationType.REACTIVE).run(args);
    }

}