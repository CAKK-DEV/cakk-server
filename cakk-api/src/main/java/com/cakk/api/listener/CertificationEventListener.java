package com.cakk.api.listener;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Async;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import com.cakk.api.annotation.ApplicationEventListener;
import com.cakk.api.mapper.EventMapper;
import com.cakk.domain.mysql.event.shop.CertificationEvent;
import com.cakk.external.extractor.MessageExtractor;
import com.cakk.external.sender.MessageSender;
import com.cakk.external.template.MessageTemplate;
import com.cakk.external.vo.CertificationMessage;

@ApplicationEventListener
public class CertificationEventListener {

	private final MessageTemplate messageTemplate;
	private final MessageExtractor messageExtractor;
	private final MessageSender messageSender;

	public CertificationEventListener(
		MessageTemplate messageTemplate,
		@Qualifier("certificationMessageExtractor") MessageExtractor messageExtractor,
		@Qualifier("slackMessageSender") MessageSender messageSender
	) {
		this.messageTemplate = messageTemplate;
		this.messageExtractor = messageExtractor;
		this.messageSender = messageSender;
	}

	@Async
	@TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
	public void sendMessageToSlack(CertificationEvent certificationEvent) {
		CertificationMessage certificationMessage = EventMapper.supplyCertificationMessageBy(certificationEvent);
		messageTemplate.sendMessage(certificationMessage, messageExtractor, messageSender);
	}
}
