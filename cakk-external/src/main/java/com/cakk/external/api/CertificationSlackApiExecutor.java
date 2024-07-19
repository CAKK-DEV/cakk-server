package com.cakk.external.api;

import net.gpedro.integrations.slack.SlackApi;
import net.gpedro.integrations.slack.SlackMessage;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CertificationSlackApiExecutor implements CertificationApiExecutor {

	private final SlackApi slackApi;

	@Override
	public <T> void send(T message, Class<T> messageType) {
		ObjectMapper objectMapper = new ObjectMapper();
		slackApi.call(objectMapper.convertValue(message, SlackMessage.class));
	}
}
