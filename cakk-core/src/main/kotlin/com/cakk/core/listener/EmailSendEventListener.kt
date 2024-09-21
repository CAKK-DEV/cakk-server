package com.cakk.core.listener

import jakarta.mail.internet.MimeMessage

import org.springframework.context.event.EventListener
import org.springframework.scheduling.annotation.Async

import com.cakk.core.annotation.ApplicationEventListener
import com.cakk.core.dto.event.EmailWithVerificationCodeSendEvent
import com.cakk.core.mapper.supplyVerificationMessageBy
import com.cakk.external.extractor.MessageExtractor
import com.cakk.external.sender.MessageSender
import com.cakk.external.template.MessageTemplate
import com.cakk.external.vo.message.VerificationMessage

@ApplicationEventListener
class EmailSendEventListener(
	private val messageTemplate: MessageTemplate,
	private val messageExtractor: MessageExtractor<VerificationMessage, MimeMessage>,
	private val messageSender: MessageSender<MimeMessage>
) {

    @Async
    @EventListener
    fun sendEmailIncludeVerificationCode(event: EmailWithVerificationCodeSendEvent) {
        val verificationMessage = supplyVerificationMessageBy(event)
        messageTemplate.sendMessage(verificationMessage, messageExtractor, messageSender)
    }
}
