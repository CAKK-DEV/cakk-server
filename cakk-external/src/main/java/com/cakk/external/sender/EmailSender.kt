package com.cakk.external.sender

import jakarta.mail.MessagingException
import jakarta.mail.internet.MimeMessage

import org.springframework.beans.factory.annotation.Value
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.stereotype.Component

import com.cakk.common.enums.ReturnCode
import com.cakk.common.exception.CakkException

@Component
class EmailSender(
	private val mailSender: JavaMailSender,
	@Value("\${spring.mail.username}")
	private val senderEmail: String
): MessageSender<String> {

	override fun send(receiver: String, message: String) {
		try {
			val emailForm = createMailFrom(receiver, message)

			mailSender.send(emailForm)
		} catch (e: RuntimeException) {
			throw CakkException(ReturnCode.SEND_EMAIL_ERROR)
		}
	}

	private fun createMailFrom(receiverMail: String, code: String): MimeMessage {
		val message = mailSender.createMimeMessage()

		try {
			val helper = MimeMessageHelper(message, true, "UTF-8")

			helper.setTo(receiverMail)
			helper.setFrom(senderEmail)
			helper.setSubject("[케이크크] 이메일 인증")

			val body = """
				<h3>인증코드를 확인해 주세요.</h3>
				<h1>$code</h1>
				이메일 인증 절차에 따라 이메일 인증코드를 발급해드립니다.<br>
				인증코드는 이메일 발송시점으로부터 3분 동안 유효합니다.<br>
				만약 본인 요청에 의한 이메일 인증이 아니라면, cakk.contact@gmail.com 으로 관련 내용을 전달해 주세요.
				더욱 편리한 서비스를 제공하기 위해 항상 최선을 다하는 케이크크가 되겠습니다.
				<br><br>

				감사합니다.
				""".trimIndent()
			helper.setText(body, true)
		} catch (e: MessagingException) {
			throw CakkException(ReturnCode.SEND_EMAIL_ERROR)
		}

		return message
	}
}