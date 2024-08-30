package com.cakk.external.extractor

import net.gpedro.integrations.slack.SlackMessage

fun interface SlackMessageExtractor<T> : MessageExtractor<T, SlackMessage> {

	override fun extract(message: T): SlackMessage
}
