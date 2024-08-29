package com.cakk.external.sender

import net.gpedro.integrations.slack.SlackApi
import net.gpedro.integrations.slack.SlackMessage

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class SlackMessageSender(
	private val slackApi: SlackApi,
	@Value("\${slack.webhook.is-enable}")
	private val isEnable: Boolean
): MessageSender<SlackMessage> {

	override fun send(receiver: String, message: SlackMessage) {
		if (!isEnable) {
			return
		}

		message.setChannel(receiver)
		slackApi.call(message)
	}
}
