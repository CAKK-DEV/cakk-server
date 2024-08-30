package com.cakk.external.extractor

import com.cakk.external.vo.ErrorAlertMessage
import net.gpedro.integrations.slack.SlackAttachment
import net.gpedro.integrations.slack.SlackField
import net.gpedro.integrations.slack.SlackMessage
import java.time.LocalDateTime

class ErrorAlertSlackMessageExtractor : SlackMessageExtractor<ErrorAlertMessage> {

	override fun extract(message: ErrorAlertMessage): SlackMessage {
		val slackAttachment = SlackAttachment()
		slackAttachment.setFallback("Error")
		slackAttachment.setColor("danger")
		slackAttachment.setTitle("Error Detect")
		slackAttachment.setTitleLink(message.contextPath)
		slackAttachment.setText(message.stackTrace)

		slackAttachment.setFields(
			listOf(
				SlackField().setTitle("Request URL").setValue(message.requestURL),
				SlackField().setTitle("Request Method").setValue(message.method),
				SlackField().setTitle("Request Time").setValue(LocalDateTime.now().toString()),
				SlackField().setTitle("Request IP").setValue(message.remoteAddr),
				SlackField().setTitle("Request User-Agent").setValue(message.header),
				message.parameterMap?.run {
					SlackField().setTitle("Request Parameter").setValue(getRequestParameters(message.parameterMap))
				}
			)
		)

		val slackMessage = SlackMessage()

		slackMessage.setAttachments(listOf(slackAttachment))
		slackMessage.setChannel("#log_server-error")
		slackMessage.setUsername("${message.serverProfile} API Error")
		slackMessage.setIcon(":alert:")
		slackMessage.setText("${message.serverProfile} api 에러 발생")

		return slackMessage
	}

	private fun getRequestParameters(parameterMap: Map<String, Array<String>>): String {
		val sb = StringBuilder()

		parameterMap.forEach { (key, values) ->
			sb.append("Parameter Name: ").append(key)

			values.forEach {
				sb.append("Parameter Value: ").append(it)
			}
		}

		return sb.toString()
	}
}
