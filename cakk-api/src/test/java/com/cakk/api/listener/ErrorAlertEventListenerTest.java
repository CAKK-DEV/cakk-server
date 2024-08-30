package com.cakk.api.listener;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;

import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import com.cakk.api.common.annotation.TestWithDisplayName;
import com.cakk.api.common.base.MockitoTest;
import com.cakk.api.dto.event.ErrorAlertEvent;
import com.cakk.external.template.MessageTemplate;

class ErrorAlertEventListenerTest extends MockitoTest {

	@InjectMocks
	private ErrorAlertEventListener errorAlertEventListener;

	@Mock
	private  MessageTemplate messageTemplate;

	private HttpServletRequest mockRequest = Mockito.mock(HttpServletRequest.class);

	@BeforeEach
	void setUp() {
		Mockito.when(mockRequest.getContextPath()).thenReturn("/test");
		Mockito.when(mockRequest.getRequestURL()).thenReturn(new StringBuffer("http://localhost/test"));
		Mockito.when(mockRequest.getMethod()).thenReturn("GET");
		Mockito.when(mockRequest.getParameterMap()).thenReturn(Map.of("param1", new String[]{"value1"}));
		Mockito.when(mockRequest.getRemoteAddr()).thenReturn("127.0.0.1");
		Mockito.when(mockRequest.getHeader("User-Agent")).thenReturn("Mozilla/5.0");
	}

	@TestWithDisplayName("에러 알림 이벤트가 발생하면, 메시지 전송 메서드가 호출되어야 한다.")
	void sendMessageToSlack() {
		// given
		ErrorAlertEvent errorAlertEvent = new ErrorAlertEvent(
			new Exception("error"),
			mockRequest,
			"test"
		);

		doNothing().when(messageTemplate).sendMessage(any(), any(), any());

		// when
		assertDoesNotThrow(() -> errorAlertEventListener.sendMessageToSlack(errorAlertEvent));

		// then
		verify(messageTemplate, times(1)).sendMessage(any(), any(), any());
	}
}
