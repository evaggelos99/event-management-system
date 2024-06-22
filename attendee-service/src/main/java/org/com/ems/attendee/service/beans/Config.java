package org.com.ems.attendee.service.beans;

import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClient.Builder;

@EnableDiscoveryClient
@Configuration
public class Config {

    @Bean
    @LoadBalanced
    Builder webClient() {

	return WebClient.builder();

    }
}
