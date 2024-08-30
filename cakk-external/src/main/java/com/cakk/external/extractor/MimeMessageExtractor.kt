package com.cakk.external.extractor

import jakarta.mail.internet.MimeMessage

interface MimeMessageExtractor<T> : MessageExtractor<T, MimeMessage> {

	override fun extract(message: T): MimeMessage
}
