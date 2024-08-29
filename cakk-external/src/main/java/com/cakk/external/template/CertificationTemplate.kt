package com.cakk.external.template

import com.cakk.external.extractor.CertificationMessageExtractor
import com.cakk.external.sender.MessageSender
import com.cakk.external.vo.CertificationMessage

class CertificationTemplate<T>(
	private val messageSender: MessageSender<T>,
	private val certificationMessageExtractor: CertificationMessageExtractor<T>
) {

	fun sendMessageForCertification(certificationMessage: CertificationMessage) {
		this.sendMessageForCertification(
			certificationMessage = certificationMessage,
			certificationMessageExtractor = certificationMessageExtractor,
			messageSender = messageSender
		)
	}

	private fun <T> sendMessageForCertification(
		certificationMessage: CertificationMessage,
		certificationMessageExtractor: CertificationMessageExtractor<T>,
		messageSender: MessageSender<T>
	) {
		val extractMessage: T = certificationMessageExtractor.extract(certificationMessage)
		messageSender.send(extractMessage)
	}
}
