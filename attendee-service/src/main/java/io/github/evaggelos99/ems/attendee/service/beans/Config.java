package io.github.evaggelos99.ems.attendee.service.beans;

import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClient.Builder;

import io.netty.resolver.DefaultAddressResolverGroup;
import reactor.netty.http.client.HttpClient;

@EnableDiscoveryClient
@Configuration
@Profile(value = { "dev", "prod" })
public class Config {

	@Bean
	@LoadBalanced
	Builder webClient() {

		final HttpClient resolver = HttpClient.create().resolver(DefaultAddressResolverGroup.INSTANCE);

		return WebClient.builder().clientConnector(new ReactorClientHttpConnector(resolver));
	}
}
