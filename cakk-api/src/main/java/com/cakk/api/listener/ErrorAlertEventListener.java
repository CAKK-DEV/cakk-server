package com.cakk.api.listener;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Async;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import com.cakk.api.annotation.ApplicationEventListener;
import com.cakk.api.dto.event.ErrorAlertEvent;
import com.cakk.api.mapper.EventMapper;
import com.cakk.external.extractor.MessageExtractor;
import com.cakk.external.sender.MessageSender;
import com.cakk.external.template.MessageTemplate;
import com.cakk.external.vo.ErrorAlertMessage;

@ApplicationEventListener
public class ErrorAlertEventListener {

	private final MessageTemplate messageTemplate;
	private final MessageExtractor messageExtractor;
	private final MessageSender messageSender;

	public ErrorAlertEventListener(
		MessageTemplate messageTemplate,
		@Qualifier("errorAlertMessageExtractor") MessageExtractor messageExtractor,
		@Qualifier("slackMessageSender") MessageSender messageSender
	) {
		this.messageTemplate = messageTemplate;
		this.messageExtractor = messageExtractor;
		this.messageSender = messageSender;
	}

	@Async
	@TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
	public void sendMessageToSlack(ErrorAlertEvent errorAlertEvent) {
		ErrorAlertMessage certificationMessage = EventMapper.supplyErrorAlertMessageBy(errorAlertEvent);
		messageTemplate.sendMessage(certificationMessage, messageExtractor, messageSender);
	}
}
