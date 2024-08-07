package com.cakk.api.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import net.gpedro.integrations.slack.SlackApi;

import com.cakk.api.template.CertificationTemplate;
import com.cakk.external.executor.CertificationApiExecutor;
import com.cakk.external.executor.CertificationSlackApiExecutor;
import com.cakk.external.extractor.CertificationMessageExtractor;
import com.cakk.external.extractor.CertificationSlackMessageExtractor;

@Configuration
public class CertificationTemplateConfig {

	private final SlackApi slackApi;
	private final boolean isEnable;

	public CertificationTemplateConfig(
		SlackApi slackApi,
		@Value("${slack.webhook.is-enable}")
		boolean isEnable) {
		this.slackApi = slackApi;
		this.isEnable = isEnable;
	}

	@Bean
	public CertificationTemplate certificationTemplate() {
		return new CertificationTemplate(certificationApiExecutor(), certificationMessageExtractor());
	}

	@Bean
	public CertificationApiExecutor certificationApiExecutor() {
		return new CertificationSlackApiExecutor(slackApi, isEnable);
	}

	@Bean
	CertificationMessageExtractor certificationMessageExtractor() {
		return new CertificationSlackMessageExtractor();
	}
}
