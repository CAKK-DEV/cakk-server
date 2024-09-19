package com.cakk.api.listener;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.mockito.InjectMocks;
import org.mockito.Mock;

import net.jqwik.api.Arbitraries;

import com.cakk.api.common.annotation.TestWithDisplayName;
import com.cakk.api.common.base.MockitoTest;
import com.cakk.api.dto.event.EmailWithVerificationCodeSendEvent;
import com.cakk.external.template.MessageTemplate;

class EmailSendEventListenerTest extends MockitoTest {

	@InjectMocks
	private EmailSendEventListener emailSendEventListener;

	@Mock
	private MessageTemplate messageTemplate;

	private EmailWithVerificationCodeSendEvent eventFixture() {
		return getConstructorMonkey().giveMeBuilder(EmailWithVerificationCodeSendEvent.class)
			.set("email", Arbitraries.strings().withCharRange('a', 'z').ofMinLength(5).ofMaxLength(10))
			.set("code", Arbitraries.strings().withCharRange('a', 'z').ofMinLength(5).ofMaxLength(10))
			.sample();
	}

	@TestWithDisplayName("이메일 전송 이벤트가 발행되면 메일 전송 메서드가 호출되어야 한다.")
	void sendEmailIncludeVerificationCode() {
		// given
		EmailWithVerificationCodeSendEvent event = eventFixture();

		doNothing().when(messageTemplate).sendMessage(any(), any(), any());

		// when
		assertDoesNotThrow(() -> emailSendEventListener.sendEmailIncludeVerificationCode(event));

		// then
		verify(messageTemplate, times(1)).sendMessage(any(), any(), any());
	}
}
