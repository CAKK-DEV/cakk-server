package com.cakk.client.config;

import java.time.Duration;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestClientConfig {

	@Bean
	public RestClient restClient() {
		RestTemplate restTemplate = new RestTemplateBuilder()
			.setConnectTimeout(Duration.ofSeconds(10))
			.setReadTimeout(Duration.ofSeconds(5))
			.build();

		return RestClient.create(restTemplate);
	}
}
