package com.cakk.external.sender

import jakarta.mail.internet.MimeMessage

import org.springframework.mail.javamail.JavaMailSender
import org.springframework.stereotype.Component

import com.cakk.common.enums.ReturnCode
import com.cakk.common.exception.CakkException

@Component
class EmailSender(
	private val mailSender: JavaMailSender
) : MessageSender<MimeMessage> {

	override fun send(message: MimeMessage) {
		try {
			mailSender.send(message)
		} catch (e: RuntimeException) {
			throw CakkException(ReturnCode.SEND_EMAIL_ERROR)
		}
	}
}
