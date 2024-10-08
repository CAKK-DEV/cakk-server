package com.cakk.external.sender

import net.gpedro.integrations.slack.SlackApi
import net.gpedro.integrations.slack.SlackMessage

import org.springframework.beans.factory.annotation.Value

class SlackMessageSender(
	private val slackApi: SlackApi,
	@Value("\${slack.webhook.is-enable}")
	private val isEnable: Boolean
): MessageSender<SlackMessage> {

	override fun send(message: SlackMessage) {
		if (!isEnable) {
			return
		}

		slackApi.call(message)
	}
}
