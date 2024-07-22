package com.cakk.external.executor;

import net.gpedro.integrations.slack.SlackApi;
import net.gpedro.integrations.slack.SlackMessage;

public class CertificationSlackApiExecutor implements CertificationApiExecutor<SlackMessage> {

	private final SlackApi slackApi;
	private final boolean isEnable;

	public CertificationSlackApiExecutor(
		SlackApi slackApi,
		boolean isEnable
	) {
		this.slackApi = slackApi;
		this.isEnable = isEnable;
	}

	@Override
	public void send(SlackMessage message) {
		if (!isEnable) {
			return;
		}

		slackApi.call(message);
	}
}

