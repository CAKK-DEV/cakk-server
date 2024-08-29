package com.cakk.api.listener;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.mockito.InjectMocks;
import org.mockito.Mock;

import net.jqwik.api.Arbitraries;

import com.cakk.api.common.annotation.TestWithDisplayName;
import com.cakk.api.common.base.MockitoTest;
import com.cakk.api.dto.event.EmailWithVerificationCodeSendEvent;
import com.cakk.external.sender.MessageSender;
import com.cakk.external.template.VerificationCodeSendTemplate;

class EmailSendEventListenerTest extends MockitoTest {

	@InjectMocks
	private EmailSendEventListener emailSendEventListener;

	@Mock
	private VerificationCodeSendTemplate verificationCodeSendTemplate;

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

		doNothing().when(verificationCodeSendTemplate).sendMessageForVerificationCode(any());

		// when
		assertDoesNotThrow(() -> emailSendEventListener.sendEmailIncludeVerificationCode(event));

		// then
		verify(verificationCodeSendTemplate, times(1)).sendMessageForVerificationCode(any());
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
		verify(verificationCodeSendTemplate, times(0)).sendMessageForVerificationCode(any());
	}
}
