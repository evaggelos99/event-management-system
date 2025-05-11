package io.github.evaggelos99.ems.user.service.config;

import io.github.evaggelos99.ems.security.lib.IOnboardingIdentityManagerService;
import io.github.evaggelos99.ems.security.service.FusionAuthOnboardingIdentityManagerService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfiguration {

    /**
     * Depending on a configuration return fusionauth/keycloak
     *
     */
    @Bean
    IOnboardingIdentityManagerService onboardingIdentityManagerService(@Value("${io.github.evaggelos99.ems.user.service.fusionauth.api-key}") final String apiKey,
                                                                       @Value("${spring.security.oauth2.resourceserver.jwt.issuer-uri}") final String baseUrl,
                                                                       @Value("${io.github.evaggelos99.ems.user.service.fusionauth.application-name}") final String applicationName) {

        return new FusionAuthOnboardingIdentityManagerService(apiKey, baseUrl, applicationName);
    }

}
