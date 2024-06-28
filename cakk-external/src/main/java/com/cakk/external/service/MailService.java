package com.cakk.external.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.cakk.common.enums.ReturnCode;
import com.cakk.common.exception.CakkException;

@Service
public class MailService {

	private final JavaMailSender mailSender;

	private final String senderEmail;

	public MailService(
		JavaMailSender mailSender,
		@Value("${spring.mail.username}") String username
	) {
		this.mailSender = mailSender;
		this.senderEmail = username;
	}

	public void sendEmail(final String receiverEmail, final String code) {
		try {
			final MimeMessage emailForm = createMailFrom(receiverEmail, code);

			mailSender.send(emailForm);
		} catch (RuntimeException e) {
			e.printStackTrace();
			throw new CakkException(ReturnCode.SEND_EMAIL_ERROR);
		}
	}

	private MimeMessage createMailFrom(final String receiverMail, final String code) {
		final MimeMessage message = mailSender.createMimeMessage();

		try {
			message.setFrom(senderEmail);
			message.setRecipients(MimeMessage.RecipientType.TO, receiverMail);
			message.setSubject("[케이크크] 이메일 인증");

			String body = """
				<h3>인증코드를 확인해 주세요.</h3>
				<h1>%s</h1>
				이메일 인증 절차에 따라 이메일 인증코드를 발급해드립니다.<br>
				인증코드는 이메일 발송시점으로부터 3분 동안 유효합니다.<br>
				만약 본인 요청에 의한 이메일 인증이 아니라면, cakk.contact@gmail.com 으로 관련 내용을 전달해 주세요.
				더욱 편리한 서비스를 제공하기 위해 항상 최선을 다하는 케이크크가 되겠습니다.
				<br><br>

				감사합니다.
				""".formatted(code);
			message.setText(body, "UTF-8", "html");
		} catch (MessagingException e) {
			throw new CakkException(ReturnCode.SEND_EMAIL_ERROR);
		}

		return message;
	}
}
