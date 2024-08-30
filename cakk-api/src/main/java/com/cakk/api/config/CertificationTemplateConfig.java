package com.cakk.api.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.cakk.external.extractor.CertificationMessageExtractor;
import com.cakk.external.extractor.CertificationSlackMessageExtractor;
import com.cakk.external.sender.MessageSender;
import com.cakk.external.template.CertificationTemplate;

@Configuration
public class CertificationTemplateConfig {

	private final MessageSender messageSender;

	public CertificationTemplateConfig(
		@Qualifier("slackMessageSender")
		MessageSender messageSender
	) {
		this.messageSender = messageSender;
	}

	@Bean
	public CertificationTemplate certificationTemplate() {
		return new CertificationTemplate(messageSender, certificationMessageExtractor());
	}

	@Bean
	public CertificationMessageExtractor certificationMessageExtractor() {
		return new CertificationSlackMessageExtractor();
	}
}
