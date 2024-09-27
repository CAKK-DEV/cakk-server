package com.cakk.api.controller

import org.mockito.Mockito.doReturn
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

import com.cakk.api.common.annotation.TestWithDisplayName
import com.cakk.api.common.base.MockMvcTest
import com.cakk.api.common.fixture.FixtureCommon.fixtureMonkey
import com.cakk.api.dto.request.user.GenerateCodeRequest
import com.cakk.api.dto.request.user.VerifyEmailRequest
import com.cakk.core.dto.param.user.UserSignInParam
import com.cakk.core.dto.param.user.UserSignUpParam
import com.cakk.core.dto.response.user.JwtResponse

internal class SignControllerTest : MockMvcTest() {

	@TestWithDisplayName("")
	fun signUp() {
		// given
		val param = fixtureMonkey.giveMeBuilder(UserSignUpParam::class.java)
			.setNotNull("provider")
			.setNotNull("idToken")
			.setNotNull("nickname")
			.setNotNull("email")
			.setNotNull("birthday")
			.setNotNull("gender")
			.sample()
		val jwt = fixtureMonkey.giveMeBuilder(JwtResponse::class.java)
			.setNotNull("accessToken")
			.setNotNull("refreshToken")
			.setNotNull("grantType")
			.sample()

		doReturn(jwt).`when`(signService).signUp(param)

		// when & then
		mockMvc.perform(
			post("/sign-up")
				.content(objectMapper.writeValueAsString(param))
				.contentType(MediaType.APPLICATION_JSON)
		)
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.data.accessToken").exists())
			.andExpect(jsonPath("$.data.refreshToken").exists())
	}

	@TestWithDisplayName("")
	@Throws(Exception::class)
	fun signIn() {
		// given
		val param = fixtureMonkey.giveMeBuilder(UserSignInParam::class.java)
			.setNotNull("provider")
			.setNotNull("idToken")
			.sample()
		val jwt = fixtureMonkey.giveMeBuilder(JwtResponse::class.java)
			.setNotNull("accessToken")
			.setNotNull("refreshToken")
			.setNotNull("grantType")
			.sample()

		doReturn(jwt).`when`(signService).signIn(param)

		// when & then
		mockMvc.perform(
			post("/sign-in")
				.content(objectMapper.writeValueAsString(param))
				.contentType(MediaType.APPLICATION_JSON)
		)
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.data.accessToken").exists())
			.andExpect(jsonPath("$.data.refreshToken").exists())
	}

	@TestWithDisplayName("")
	@Throws(Exception::class)
	fun sendEmailForVerification() {
		// given
		val dto = fixtureMonkey.giveMeOne(GenerateCodeRequest::class.java)

		// when & then
		mockMvc.perform(
			post("/email/request-code")
				.content(objectMapper.writeValueAsString(dto))
				.contentType(MediaType.APPLICATION_JSON)
		)
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.data").doesNotExist())
	}

	@TestWithDisplayName("")
	@Throws(Exception::class)
	fun verifyEmail() {
		// given
		val dto = fixtureMonkey.giveMeOne(VerifyEmailRequest::class.java)

		// when & then
		mockMvc.perform(
			post("/email/verify-email")
				.content(objectMapper.writeValueAsString(dto))
				.contentType(MediaType.APPLICATION_JSON)
		)
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.data").doesNotExist())
	}
}
