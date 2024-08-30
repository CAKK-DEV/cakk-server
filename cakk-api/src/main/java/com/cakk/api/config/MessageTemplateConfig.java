package com.cakk.api.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;

import net.gpedro.integrations.slack.SlackApi;

import com.cakk.external.extractor.CertificationSlackMessageExtractor;
import com.cakk.external.extractor.ErrorAlertSlackMessageExtractor;
import com.cakk.external.extractor.MessageExtractor;
import com.cakk.external.extractor.VerificationCodeMimeMessageExtractor;
import com.cakk.external.sender.EmailMessageSender;
import com.cakk.external.sender.MessageSender;
import com.cakk.external.sender.SlackMessageSender;
import com.cakk.external.template.MessageTemplate;

@Configuration
public class MessageTemplateConfig {

	private final JavaMailSender javaMailSender;
	private final SlackApi slackApi;

	private final String senderEmail;
	private final Boolean isEnable;

	public MessageTemplateConfig(
		JavaMailSender javaMailSender,
		SlackApi slackApi,
		@Value("${spring.mail.username}")
		String senderEmail,
		@Value("${slack.webhook.is-enable}")
		Boolean isEnable
	) {
		this.javaMailSender = javaMailSender;
		this.senderEmail = senderEmail;
		this.isEnable = isEnable;
		this.slackApi = slackApi;
	}

	@Bean
	public MessageTemplate certificationTemplate() {
		return new MessageTemplate();
	}

	@Bean
	public MessageExtractor certificationMessageExtractor() {
		return new CertificationSlackMessageExtractor();
	}

	@Bean
	public MessageExtractor errorAlertMessageExtractor() {
		return new ErrorAlertSlackMessageExtractor();
	}

	@Bean
	public MessageExtractor verificationCodeMimeMessageExtractor() {
		return new VerificationCodeMimeMessageExtractor(javaMailSender, senderEmail);
	}

	@Bean
	public MessageSender emailMessageSender() {
		return new EmailMessageSender(javaMailSender);
	}

	@Bean
	public MessageSender slackMessageSender() {
		return new SlackMessageSender(slackApi, isEnable);
	}
}
