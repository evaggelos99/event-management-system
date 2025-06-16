package io.github.evaggelos99.ems.attendee.service.config;

import org.simplejavamail.api.mailer.Mailer;
import org.simplejavamail.mailer.MailerBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:properties/javamail.properties")
@ConfigurationProperties(prefix = "io.github.evaggelos99.ems.attendee-service.mail")
public class JavaMailConfiguration {

    private String host;
    private String port;
    private String username;
    private String password;

    @Bean
    Mailer mailer() {

        return MailerBuilder
                .withSMTPServer(host, Integer.parseInt(port), username, password)
                .buildMailer();
    }


    public String getHost() {
        return host;
    }

    public void setHost(final String host) {
        this.host = host;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(final String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(final String username) {
        this.username = username;
    }

    public String getPort() {
        return port;
    }

    public void setPort(final String port) {
        this.port = port;
    }
}
