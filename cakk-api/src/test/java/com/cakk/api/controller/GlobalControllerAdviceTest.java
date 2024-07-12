package com.cakk.api.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.springframework.http.converter.HttpMessageNotReadableException;

import com.cakk.api.common.annotation.TestWithDisplayName;
import com.cakk.api.common.base.MockMvcTest;
import com.cakk.common.enums.ReturnCode;
import com.cakk.common.exception.CakkException;

public class GlobalControllerAdviceTest extends MockMvcTest {

	@TestWithDisplayName("CakkException이 발생하면 BAD_REQUEST를 반환한다.")
	void handleCakkException1() throws Exception {
		// given
		final ReturnCode returnCode = ReturnCode.WRONG_JWT_TOKEN;
		doThrow(new CakkException(returnCode)).when(shopService).searchDetailById(1L);

		// when & then
		mockMvc.perform(get("/shops/1"))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.returnCode").value(returnCode.getCode()))
			.andExpect(jsonPath("$.returnMessage").value(returnCode.getMessage()));
	}

	@TestWithDisplayName("CakkException이 발생했고 INTERNAL_SERVER_ERROR 일 때 BAD_REQUEST를 반환한다.")
	void handleCakkException2() throws Exception {
		// given
		final ReturnCode returnCode = ReturnCode.INTERNAL_SERVER_ERROR;

		doThrow(new CakkException(returnCode)).when(shopService).searchDetailById(1L);
		doNothing().when(slackService).sendSlackForError(any(CakkException.class), any());

		// when & then
		mockMvc.perform(get("/shops/1"))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.returnCode").value(returnCode.getCode()))
			.andExpect(jsonPath("$.returnMessage").value(returnCode.getMessage()));

		verify(slackService, times(1)).sendSlackForError(any(CakkException.class), any());
	}

	@TestWithDisplayName("CakkException이 발생했고 EXTERNAL_SERVER_ERROR 일 때 BAD_REQUEST를 반환한다.")
	void handleCakkException3() throws Exception {
		// given
		final ReturnCode returnCode = ReturnCode.EXTERNAL_SERVER_ERROR;

		doThrow(new CakkException(returnCode)).when(shopService).searchDetailById(1L);
		doNothing().when(slackService).sendSlackForError(any(CakkException.class), any());

		// when & then
		mockMvc.perform(get("/shops/1"))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.returnCode").value(returnCode.getCode()))
			.andExpect(jsonPath("$.returnMessage").value(returnCode.getMessage()));

		verify(slackService, times(1)).sendSlackForError(any(CakkException.class), any());
	}

	@TestWithDisplayName("HttpMessageNotReadableException이 발생하면 BAD_REQUEST를 반환한다.")
	void handleRequestException1() throws Exception {
		// given
		doThrow(HttpMessageNotReadableException.class).when(shopService).searchDetailById(1L);

		// when & then
		mockMvc.perform(get("/shops/1"))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.returnCode").value(ReturnCode.WRONG_PARAMETER.getCode()))
			.andExpect(jsonPath("$.returnMessage").value(ReturnCode.WRONG_PARAMETER.getMessage()));
	}

	@TestWithDisplayName("HttpMessageNotReadableException이 발생하면 BAD_REQUEST를 반환한다.")
	void handleRequestException2() throws Exception {
		// given
		doThrow(HttpMessageNotReadableException.class).when(shopService).searchDetailById(1L);

		// when & then
		mockMvc.perform(get("/shops/1"))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.returnCode").value(ReturnCode.WRONG_PARAMETER.getCode()))
			.andExpect(jsonPath("$.returnMessage").value(ReturnCode.WRONG_PARAMETER.getMessage()));
	}

	@TestWithDisplayName("HttpRequestMethodNotSupportedException 발생하면 BAD_REQUEST를 반환한다.")
	void handleMethodNotSupportedException() throws Exception {
		// when & then
		mockMvc.perform(post("/shops/1"))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.returnCode").value(ReturnCode.METHOD_NOT_ALLOWED.getCode()))
			.andExpect(jsonPath("$.returnMessage").value(ReturnCode.METHOD_NOT_ALLOWED.getMessage()));
	}

	@TestWithDisplayName("HttpRequestMethodNotSupportedException 발생하면 BAD_REQUEST를 반환한다.")
	void handleMethodArgNotValidException() throws Exception {
		// when & then
		mockMvc.perform(get("/shops/location-based"))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.returnCode").value(ReturnCode.WRONG_PARAMETER.getCode()))
			.andExpect(jsonPath("$.returnMessage").value(ReturnCode.WRONG_PARAMETER.getMessage()));
	}

	@TestWithDisplayName("SQLException이 발생하면 BAD_REQUEST를 반환한다.")
	void handleServerException1() throws Exception {
		// given
		doThrow(new RuntimeException()).when(shopService).searchDetailById(1L);

		// when & then
		mockMvc.perform(get("/shops/1"))
			.andExpect(status().isInternalServerError())
			.andExpect(jsonPath("$.returnCode").value(ReturnCode.INTERNAL_SERVER_ERROR.getCode()))
			.andExpect(jsonPath("$.returnMessage").value(ReturnCode.INTERNAL_SERVER_ERROR.getMessage()));
	}
}
