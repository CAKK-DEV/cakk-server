package com.cakk.api.event.user.listener;

import com.cakk.domain.event.user.CertificationEvent;
import com.cakk.api.service.slack.SlackService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@RequiredArgsConstructor
@Component
public class UserEventListener {
	private final SlackService slackService;

	@TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
	public void sendMessageToSlack(CertificationEvent certificationEvent) {
	}
}
