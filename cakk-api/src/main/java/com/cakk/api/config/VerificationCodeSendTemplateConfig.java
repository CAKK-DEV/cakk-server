package com.cakk.api.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.cakk.external.extractor.VerificationCodeMessageExtractor;
import com.cakk.external.sender.MessageSender;
import com.cakk.external.template.VerificationCodeSendTemplate;

@Configuration
public class VerificationCodeSendTemplateConfig {

	private final MessageSender messageSender;
	private final VerificationCodeMessageExtractor verificationCodeMessageExtractor;

	public VerificationCodeSendTemplateConfig(
		@Qualifier("emailSender")
		MessageSender messageSender,
		VerificationCodeMessageExtractor verificationCodeMessageExtractor
	) {
		this.messageSender = messageSender;
		this.verificationCodeMessageExtractor = verificationCodeMessageExtractor;
	}

	@Bean
	public VerificationCodeSendTemplate verificationCodeSendTemplate() {
		return new VerificationCodeSendTemplate(messageSender, verificationCodeMessageExtractor);
	}
}
