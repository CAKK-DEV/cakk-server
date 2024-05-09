package com.cakk.api.event.shop.listener;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import lombok.RequiredArgsConstructor;

import com.cakk.api.service.slack.SlackService;
import com.cakk.domain.event.shop.CertificationEvent;

@RequiredArgsConstructor
@Component
public class CertificationEventListener {

	private final SlackService slackService;

	@Async
	@TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
	public void sendMessageToSlack(CertificationEvent certificationEvent) {
		slackService.sendSlackForCertification(certificationEvent);
	}
}
