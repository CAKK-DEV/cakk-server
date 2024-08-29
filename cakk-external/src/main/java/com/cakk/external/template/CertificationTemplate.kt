package com.cakk.external.template

import com.cakk.external.extractor.CertificationMessageExtractor
import com.cakk.external.sender.MessageSender
import com.cakk.external.vo.CertificationMessage

class CertificationTemplate<T>(
	private val messageSender: MessageSender<T>,
	private val certificationMessageExtractor: CertificationMessageExtractor<T>
) {

	fun sendMessageForCertification(
		receiver: String,
		certificationMessage: CertificationMessage
	) {
		this.sendMessageForCertification(
			receiver = receiver,
			certificationMessage = certificationMessage,
			certificationMessageExtractor = certificationMessageExtractor,
			messageSender = messageSender
		)
	}

	private fun <T> sendMessageForCertification(
		receiver: String,
		certificationMessage: CertificationMessage,
		certificationMessageExtractor: CertificationMessageExtractor<T>,
		messageSender: MessageSender<T>
	) {
		val extractMessage: T = certificationMessageExtractor.extract(certificationMessage) as T
		messageSender.send(receiver, extractMessage)
	}
}
