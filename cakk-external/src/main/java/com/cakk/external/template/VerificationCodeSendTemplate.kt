package com.cakk.external.template

import com.cakk.external.extractor.VerificationCodeMessageExtractor
import com.cakk.external.sender.MessageSender
import com.cakk.external.vo.VerificationMessage

class VerificationCodeSendTemplate<T>(
	private val messageSender: MessageSender<T>,
	private val verificationCodeMessageExtractor: VerificationCodeMessageExtractor<T>
) {

	fun sendMessageForVerificationCode(verificationMessage: VerificationMessage) {
		this.sendMessageForVerificationCode(
			verificationMessage = verificationMessage,
			verificationCodeMessageExtractor = verificationCodeMessageExtractor,
			messageSender = messageSender
		)
	}

	private fun <T> sendMessageForVerificationCode(
		verificationMessage: VerificationMessage,
		verificationCodeMessageExtractor: VerificationCodeMessageExtractor<T>,
		messageSender: MessageSender<T>
	) {
		val extractMessage: T = verificationCodeMessageExtractor.extract(verificationMessage)
		messageSender.send(extractMessage)
	}
}
