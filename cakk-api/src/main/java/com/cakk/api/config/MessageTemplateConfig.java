package com.cakk.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.cakk.external.extractor.CertificationSlackMessageExtractor;
import com.cakk.external.extractor.ErrorAlertSlackMessageExtractor;
import com.cakk.external.extractor.MessageExtractor;
import com.cakk.external.template.MessageTemplate;

@Configuration
public class MessageTemplateConfig {

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
}
