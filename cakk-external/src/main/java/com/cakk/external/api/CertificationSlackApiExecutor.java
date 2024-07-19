package com.cakk.external.api;

import org.springframework.beans.factory.annotation.Value;

import net.gpedro.integrations.slack.SlackApi;
import net.gpedro.integrations.slack.SlackMessage;

public class CertificationSlackApiExecutor implements CertificationApiExecutor<SlackMessage> {

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
	public void send(SlackMessage message) {
		slackApi.call(message);
	}
}
