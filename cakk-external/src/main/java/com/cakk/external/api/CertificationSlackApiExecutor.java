package com.cakk.external.api;

import org.springframework.beans.factory.annotation.Value;

import net.gpedro.integrations.slack.SlackApi;
import net.gpedro.integrations.slack.SlackMessage;

import com.fasterxml.jackson.databind.ObjectMapper;

public class CertificationSlackApiExecutor implements CertificationApiExecutor {

	private final SlackApi slackApi;

	private final String profile;
	private final boolean isEnable;

	public CertificationSlackApiExecutor(
		SlackApi slackApi,
		@Value("${spring.profiles.active}")
		String profile,
		@Value("${slack.webhook.is-enable}")
		boolean isEnable
	) {
		this.slackApi = slackApi;
		this.profile = profile;
		this.isEnable = isEnable;
	}

	@Override
	public <T> void send(T message, Class<T> messageType) {
		ObjectMapper objectMapper = new ObjectMapper();
		slackApi.call(objectMapper.convertValue(message, SlackMessage.class));
	}
}
