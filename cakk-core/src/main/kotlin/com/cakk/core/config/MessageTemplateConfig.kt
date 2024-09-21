package com.cakk.core.config

import com.cakk.external.extractor.CertificationSlackMessageExtractor
import com.cakk.external.extractor.ErrorAlertSlackMessageExtractor
import com.cakk.external.extractor.MessageExtractor
import com.cakk.external.extractor.VerificationCodeMimeMessageExtractor
import com.cakk.external.sender.EmailMessageSender
import com.cakk.external.sender.MessageSender
import com.cakk.external.sender.SlackMessageSender
import com.cakk.external.template.MessageTemplate
import com.cakk.external.vo.message.CertificationMessage
import com.cakk.external.vo.message.ErrorAlertMessage
import com.cakk.external.vo.message.VerificationMessage
import jakarta.mail.internet.MimeMessage
import net.gpedro.integrations.slack.SlackApi
import net.gpedro.integrations.slack.SlackMessage
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.mail.javamail.JavaMailSender

@Configuration
class MessageTemplateConfig(
    private val javaMailSender: JavaMailSender,
    private val slackApi: SlackApi,
    @param:Value("\${spring.mail.username}")
	private val senderEmail: String,
    @param:Value("\${slack.webhook.is-enable}")
	private val isEnable: Boolean
) {
    @Bean
    fun certificationTemplate(): MessageTemplate {
        return MessageTemplate()
    }

    @Bean
    fun certificationMessageExtractor(): MessageExtractor<CertificationMessage, SlackMessage> {
        return CertificationSlackMessageExtractor()
    }

    @Bean
    fun errorAlertMessageExtractor(): MessageExtractor<ErrorAlertMessage, SlackMessage> {
        return ErrorAlertSlackMessageExtractor()
    }

    @Bean
    fun verificationCodeMimeMessageExtractor(): MessageExtractor<VerificationMessage, MimeMessage> {
        return VerificationCodeMimeMessageExtractor(javaMailSender, senderEmail)
    }

    @Bean
    fun emailMessageSender(): MessageSender<MimeMessage> {
        return EmailMessageSender(javaMailSender)
    }

    @Bean
    fun slackMessageSender(): MessageSender<SlackMessage> {
        return SlackMessageSender(slackApi, isEnable)
    }
}
