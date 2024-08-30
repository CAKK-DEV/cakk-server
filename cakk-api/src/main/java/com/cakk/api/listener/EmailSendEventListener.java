package com.cakk.api.listener;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;

import com.cakk.api.annotation.ApplicationEventListener;
import com.cakk.api.dto.event.EmailWithVerificationCodeSendEvent;
import com.cakk.api.mapper.EventMapper;
import com.cakk.external.template.VerificationCodeSendTemplate;
import com.cakk.external.vo.VerificationMessage;

@ApplicationEventListener
public class EmailSendEventListener {


	private final VerificationCodeSendTemplate verificationCodeSendTemplate;

	public EmailSendEventListener(
		VerificationCodeSendTemplate verificationCodeSendTemplate
	) {
		this.verificationCodeSendTemplate = verificationCodeSendTemplate;
	}

	@Async
	@EventListener
	public void sendEmailIncludeVerificationCode(EmailWithVerificationCodeSendEvent event) {
		final VerificationMessage verificationMessage = EventMapper.supplyVerificationMessageBy(event);
		verificationCodeSendTemplate.sendMessageForVerificationCode(verificationMessage);
	}
}
