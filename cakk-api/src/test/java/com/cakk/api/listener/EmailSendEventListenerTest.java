package com.cakk.api.listener;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.mockito.InjectMocks;
import org.mockito.Mock;

import net.jqwik.api.Arbitraries;

import com.cakk.api.common.annotation.TestWithDisplayName;
import com.cakk.api.common.base.EventListenerTest;
import com.cakk.api.dto.event.EmailWithVerificationCodeSendEvent;
import com.cakk.api.listener.user.EmailSendEventListener;
import com.cakk.external.service.MailService;

class EmailSendEventListenerTest extends EventListenerTest {

	@InjectMocks
	private EmailSendEventListener emailSendEventListener;

	@Mock
	private MailService mailService;

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

		doNothing().when(mailService).sendEmail(event.email(), event.code());

		// when
		assertDoesNotThrow(() -> emailSendEventListener.sendEmailIncludeVerificationCode(event));

		// then
		verify(mailService, times(1)).sendEmail(event.email(), event.code());
	}

	@TestWithDisplayName("이벤트에 null 데이터가 포함돼 있으면, 메일 전송 메서드가 호출 시, 에러를 반환한다.")
	void sendEmailIncludeVerificationCode2() {
		// given
		EmailWithVerificationCodeSendEvent event = new EmailWithVerificationCodeSendEvent(null, null);

		// when
		assertThrows(
			NullPointerException.class,
			() -> emailSendEventListener.sendEmailIncludeVerificationCode(event)
		);

		// then
		verify(mailService, times(0)).sendEmail(event.email(), event.code());
	}
}
