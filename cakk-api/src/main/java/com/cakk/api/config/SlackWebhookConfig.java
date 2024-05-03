package com.cakk.api.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import net.gpedro.integrations.slack.SlackApi;

@Configuration
public class SlackWebhookConfig {

	@Value("${slack.webhook.url}")
	private String slackWebhookUrl;

	@Bean
	public SlackApi slackApi() {
		return new SlackApi(slackWebhookUrl);
	}
}
