package com.cakk.api.listener;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;

import com.cakk.api.annotation.ApplicationEventListener;
import com.cakk.api.dto.event.EmailWithVerificationCodeSendEvent;
import com.cakk.api.mapper.EventMapper;
import com.cakk.external.extractor.MessageExtractor;
import com.cakk.external.sender.MessageSender;
import com.cakk.external.template.MessageTemplate;
import com.cakk.external.vo.message.VerificationMessage;

@ApplicationEventListener
public class EmailSendEventListener {


	private final MessageTemplate messageTemplate;
	private final MessageExtractor messageExtractor;
	private final MessageSender messageSender;

	public EmailSendEventListener(
		MessageTemplate messageTemplate,
		@Qualifier("verificationCodeMimeMessageExtractor") MessageExtractor messageExtractor,
		@Qualifier("emailMessageSender") MessageSender messageSender
	) {
		this.messageTemplate = messageTemplate;
		this.messageExtractor = messageExtractor;
		this.messageSender = messageSender;
	}

	@Async
	@EventListener
	public void sendEmailIncludeVerificationCode(EmailWithVerificationCodeSendEvent event) {
		final VerificationMessage verificationMessage = EventMapper.supplyVerificationMessageBy(event);
		messageTemplate.sendMessage(verificationMessage, messageExtractor, messageSender);
	}
}
