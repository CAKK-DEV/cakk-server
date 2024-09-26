package com.cakk.core.listener

import jakarta.mail.internet.MimeMessage

import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.*

import net.jqwik.api.Arbitraries

import com.cakk.core.common.annotation.TestWithDisplayName
import com.cakk.core.common.base.MockitoTest
import com.cakk.core.common.fixture.FixtureCommon.fixtureMonkey
import com.cakk.core.dto.event.EmailWithVerificationCodeSendEvent
import com.cakk.core.mapper.supplyVerificationMessageBy
import com.cakk.external.extractor.MessageExtractor
import com.cakk.external.sender.MessageSender
import com.cakk.external.template.MessageTemplate
import com.cakk.external.vo.message.VerificationMessage
import io.kotest.assertions.throwables.shouldNotThrow


internal class EmailSendEventListenerTest : MockitoTest() {

    @InjectMocks
    private lateinit var emailSendEventListener: EmailSendEventListener

    @Mock
    private lateinit var messageTemplate: MessageTemplate

    @Mock
    private lateinit var messageExtractor: MessageExtractor<VerificationMessage, MimeMessage>

    @Mock
    private lateinit var messageSender: MessageSender<MimeMessage>

    private fun eventFixture(): EmailWithVerificationCodeSendEvent {
        return fixtureMonkey.giveMeBuilder(EmailWithVerificationCodeSendEvent::class.java)
            .set("email", Arbitraries.strings().withCharRange('a', 'z').ofMinLength(5).ofMaxLength(10))
            .set("code", Arbitraries.strings().withCharRange('a', 'z').ofMinLength(5).ofMaxLength(10))
            .sample()
    }

    @TestWithDisplayName("이메일 전송 이벤트가 발행되면 메일 전송 메서드가 호출되어야 한다.")
    fun sendEmailIncludeVerificationCode() {
        // given
        val event = eventFixture()

        // when
		shouldNotThrow<Exception> {
			emailSendEventListener.sendEmailIncludeVerificationCode(event)
		}

        // then
        verify(messageTemplate, times(1))
			.sendMessage(supplyVerificationMessageBy(event), messageExtractor, messageSender)
    }
}
