package io.github.evaggelos99.scheduler.service.config;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.Produces;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.simplejavamail.api.mailer.Mailer;
import org.simplejavamail.mailer.MailerBuilder;
import org.simplejavamail.mailer.internal.MailerImpl;

@ApplicationScoped
public class JavaMailConfiguration {

    @ApplicationScoped
    Mailer mailer(@ConfigProperty(name = "io.github.evaggelos99.ems.scheduler-service.mail.host") String host,
                  @ConfigProperty(name = "io.github.evaggelos99.ems.scheduler-service.mail.port") String port,
                  @ConfigProperty(name = "io.github.evaggelos99.ems.scheduler-service.mail.username") String username,
                  @ConfigProperty(name = "io.github.evaggelos99.ems.scheduler-service.mail.password") String password) {

        return MailerBuilder
                .withSMTPServer(host, Integer.parseInt(port), username, password)
                .buildMailer();
    }
}
