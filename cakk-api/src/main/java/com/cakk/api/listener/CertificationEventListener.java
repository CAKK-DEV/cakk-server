package com.cakk.api.listener;

import org.springframework.scheduling.annotation.Async;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import lombok.RequiredArgsConstructor;

import com.cakk.api.annotation.ApplicationEventListener;
import com.cakk.api.mapper.EventMapper;
import com.cakk.api.service.slack.SlackService;
import com.cakk.domain.mysql.event.shop.CertificationEvent;
import com.cakk.external.template.CertificationTemplate;
import com.cakk.external.vo.CertificationMessage;

@RequiredArgsConstructor
@ApplicationEventListener
public class CertificationEventListener {

	private final CertificationTemplate certificationTemplate;

	@Async
	@TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
	public void sendMessageToSlack(CertificationEvent certificationEvent) {
		CertificationMessage certificationMessage = EventMapper.supplyCertificationMessageBy(certificationEvent);
		certificationTemplate.sendMessageForCertification(certificationMessage);
	}
}
