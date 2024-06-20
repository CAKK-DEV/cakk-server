package com.cakk.batch.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import net.gpedro.integrations.slack.SlackApi;

@Configuration
public class SlackWebhookConfig {

	private final String slackWebhookUrl;

	public SlackWebhookConfig(@Value("${slack.webhook.url}") String slackWebhookUrl) {
		this.slackWebhookUrl = slackWebhookUrl;
	}

	@Bean
	public SlackApi slackApi() {
		return new SlackApi(slackWebhookUrl);
	}
}
