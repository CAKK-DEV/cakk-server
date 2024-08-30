package com.cakk.external.template

import com.cakk.external.extractor.MessageExtractor
import com.cakk.external.sender.MessageSender

class MessageTemplate<T, U> {

	fun sendMessage(
		message: T,
		messageExtractor: MessageExtractor<T, U>,
		messageSender: MessageSender<U>,
	) {
		val extractMessage: U = messageExtractor.extract(message)
		messageSender.send(extractMessage)
	}
}
