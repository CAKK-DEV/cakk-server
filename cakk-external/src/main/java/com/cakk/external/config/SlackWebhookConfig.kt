package com.cakk.external.config

import net.gpedro.integrations.slack.SlackApi

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class SlackWebhookConfig(
	@Value("\${slack.webhook.url}")
	private val slackWebhookUrl: String
) {

	@Bean
	fun slackApi(): SlackApi {
		return SlackApi(slackWebhookUrl)
	}
}
