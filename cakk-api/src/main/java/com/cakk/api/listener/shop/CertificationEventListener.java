package com.cakk.api.listener.shop;

import org.springframework.scheduling.annotation.Async;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import lombok.RequiredArgsConstructor;

import com.cakk.api.annotation.ApplicationEventListener;
import com.cakk.api.service.slack.SlackService;
import com.cakk.domain.mysql.event.shop.CertificationEvent;

@RequiredArgsConstructor
@ApplicationEventListener
public class CertificationEventListener {

	private final SlackService slackService;

	@Async
	@TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
	public void sendMessageToSlack(CertificationEvent certificationEvent) {
		slackService.sendSlackForCertification(certificationEvent);
	}
}
