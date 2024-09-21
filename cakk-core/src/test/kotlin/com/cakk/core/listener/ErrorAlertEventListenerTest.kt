package com.cakk.core.listener

import jakarta.servlet.http.HttpServletRequest

import io.kotest.assertions.throwables.shouldNotThrow

import org.junit.jupiter.api.BeforeEach
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.*

import net.gpedro.integrations.slack.SlackMessage

import com.cakk.core.common.annotation.TestWithDisplayName
import com.cakk.core.common.base.MockitoTest
import com.cakk.core.dto.event.ErrorAlertEvent
import com.cakk.core.mapper.supplyErrorAlertMessageBy
import com.cakk.external.extractor.MessageExtractor
import com.cakk.external.sender.MessageSender
import com.cakk.external.template.MessageTemplate
import com.cakk.external.vo.message.ErrorAlertMessage

internal class ErrorAlertEventListenerTest : MockitoTest() {

    @InjectMocks
    private lateinit var errorAlertEventListener: ErrorAlertEventListener

    @Mock
    private lateinit var messageTemplate: MessageTemplate

    @Mock
    private lateinit var messageExtractor: MessageExtractor<ErrorAlertMessage, SlackMessage>

    @Mock
    private lateinit var messageSender: MessageSender<SlackMessage>

    private val mockRequest: HttpServletRequest = mock(HttpServletRequest::class.java)

    @BeforeEach
    fun setUp() {
        `when`(mockRequest.contextPath).thenReturn("/test")
        `when`(mockRequest.requestURL).thenReturn(StringBuffer("http://localhost/test"))
        `when`(mockRequest.method).thenReturn("GET")
        `when`<Map<String, Array<String>>>(mockRequest.parameterMap).thenReturn(mapOf("param1" to arrayOf("value1")))
        `when`(mockRequest.remoteAddr).thenReturn("127.0.0.1")
        `when`(mockRequest.getHeader("User-Agent")).thenReturn("Mozilla/5.0")
    }

    @TestWithDisplayName("에러 알림 이벤트가 발생하면, 메시지 전송 메서드가 호출되어야 한다.")
    fun sendMessageToSlack() {
        // given
        val errorAlertEvent = ErrorAlertEvent(Exception("error"), mockRequest, "test")

        // when
		shouldNotThrow<Exception> {
			errorAlertEventListener.sendMessageToSlack(errorAlertEvent)
		}

        // then
        verify(messageTemplate, times(1)).sendMessage(
			supplyErrorAlertMessageBy(errorAlertEvent), messageExtractor, messageSender
        )
    }
}
