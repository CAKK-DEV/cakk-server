package com.cakk.api.listener;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;

import com.cakk.api.annotation.ApplicationEventListener;
import com.cakk.api.dto.event.EmailWithVerificationCodeSendEvent;
import com.cakk.external.sender.MessageSender;

@ApplicationEventListener
public class EmailSendEventListener {


	private final MessageSender messageSender;

	public EmailSendEventListener(
		@Qualifier("emailSender")
		MessageSender messageSender
	) {
		this.messageSender = messageSender;
	}

	@Async
	@EventListener
	public void sendEmailIncludeVerificationCode(EmailWithVerificationCodeSendEvent event) {
		messageSender.send(Objects.requireNonNull(event.email()), Objects.requireNonNull(event.code()));
	}
}
