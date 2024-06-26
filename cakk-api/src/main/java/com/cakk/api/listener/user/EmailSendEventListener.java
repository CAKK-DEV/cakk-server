package com.cakk.api.listener.user;

import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

import com.cakk.api.dto.event.EmailWithVerificationCodeSendEvent;
import com.cakk.external.service.MailService;

@RequiredArgsConstructor
@Component
public class EmailSendEventListener {

	private final MailService mailService;

	@Async
	@EventListener
	public void sendEmailIncludeVerificationCode(EmailWithVerificationCodeSendEvent event) {
		mailService.sendEmail(event.email(), event.code());
	}
}
