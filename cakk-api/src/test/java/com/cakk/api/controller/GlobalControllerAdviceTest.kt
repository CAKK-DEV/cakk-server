package com.cakk.api.controller

import org.mockito.Mockito
import org.mockito.Mockito.*
import org.mockito.kotlin.any
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

import com.cakk.api.common.annotation.TestWithDisplayName
import com.cakk.api.common.base.MockMvcTest
import com.cakk.common.enums.ReturnCode
import com.cakk.common.exception.CakkException

internal class GlobalControllerAdviceTest : MockMvcTest() {

	@TestWithDisplayName("CakkException이 발생하면 BAD_REQUEST를 반환한다.")
	fun handleCakkException1() {
		// given
		val returnCode = ReturnCode.WRONG_JWT_TOKEN
		Mockito.doThrow(CakkException(returnCode)).`when`(shopService).searchDetailById(1L)

		// when & then
		mockMvc.perform(get("/shops/1"))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.returnCode").value(returnCode.code))
			.andExpect(jsonPath("$.returnMessage").value(returnCode.message))
	}

	@TestWithDisplayName("CakkException이 발생했고 INTERNAL_SERVER_ERROR 일 때 BAD_REQUEST를 반환한다.")
	fun handleCakkException2() {
		// given
		val returnCode = ReturnCode.INTERNAL_SERVER_ERROR

		doThrow(CakkException(returnCode)).`when`(shopService).searchDetailById(1L)
		doNothing().`when`(errorAlertEventListener).sendMessageToSlack(any())

		// when & then
		mockMvc.perform(get("/shops/1"))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.returnCode").value(returnCode.code))
			.andExpect(jsonPath("$.returnMessage").value(returnCode.message))

		verify(errorAlertEventListener, times(1)).sendMessageToSlack(any())
	}

	@TestWithDisplayName("CakkException이 발생했고 EXTERNAL_SERVER_ERROR 일 때 BAD_REQUEST를 반환한다.")
	fun handleCakkException3() {
		// given
		val returnCode = ReturnCode.EXTERNAL_SERVER_ERROR

		doThrow(CakkException(returnCode)).`when`(shopService).searchDetailById(1L)
		doNothing().`when`(errorAlertEventListener).sendMessageToSlack(any())

		// when & then
		mockMvc.perform(get("/shops/1"))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.returnCode").value(returnCode.code))
			.andExpect(jsonPath("$.returnMessage").value(returnCode.message))

		verify(errorAlertEventListener, times(1)).sendMessageToSlack(any())
	}

	@TestWithDisplayName("HttpMessageNotReadableException이 발생하면 BAD_REQUEST를 반환한다.")
	fun handleRequestException1() {
		// given
		Mockito.doThrow(HttpMessageNotReadableException::class.java).`when`(shopService).searchDetailById(1L)

		// when & then
		mockMvc.perform(get("/shops/1"))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.returnCode").value(ReturnCode.WRONG_PARAMETER.code))
			.andExpect(jsonPath("$.returnMessage").value(ReturnCode.WRONG_PARAMETER.message))
	}

	@TestWithDisplayName("HttpMessageNotReadableException이 발생하면 BAD_REQUEST를 반환한다.")
	fun handleRequestException2() {
		// given
		doThrow(HttpMessageNotReadableException::class.java).`when`(shopService).searchDetailById(1L)

		// when & then
		mockMvc.perform(get("/shops/1"))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.returnCode").value(ReturnCode.WRONG_PARAMETER.code))
			.andExpect(jsonPath("$.returnMessage").value(ReturnCode.WRONG_PARAMETER.message))
	}

	@TestWithDisplayName("HttpRequestMethodNotSupportedException 발생하면 BAD_REQUEST를 반환한다.")
	fun handleMethodNotSupportedException() {
		// when & then
		mockMvc.perform(post("/shops/1"))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.returnCode").value(ReturnCode.METHOD_NOT_ALLOWED.code))
			.andExpect(jsonPath("$.returnMessage").value(ReturnCode.METHOD_NOT_ALLOWED.message))
	}

	@TestWithDisplayName("HttpRequestMethodNotSupportedException 발생하면 BAD_REQUEST를 반환한다.")
	fun handleMethodArgNotValidException() {
		// when & then
		mockMvc.perform(get("/shops/location-based"))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.returnCode").value(ReturnCode.WRONG_PARAMETER.code))
			.andExpect(jsonPath("$.returnMessage").value(ReturnCode.WRONG_PARAMETER.message))
	}

	@TestWithDisplayName("SQLException이 발생하면 BAD_REQUEST를 반환한다.")
	fun handleServerException1() {
		// given
		doThrow(RuntimeException()).`when`(shopService).searchDetailById(1L)

		// when & then
		mockMvc.perform(get("/shops/1"))
			.andExpect(status().isInternalServerError())
			.andExpect(jsonPath("$.returnCode").value(ReturnCode.INTERNAL_SERVER_ERROR.code))
			.andExpect(jsonPath("$.returnMessage").value(ReturnCode.INTERNAL_SERVER_ERROR.message))
	}
}
