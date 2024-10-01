package com.cakk.api.controller

import org.mockito.Mockito.doReturn
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

import com.cakk.api.common.annotation.TestWithDisplayName
import com.cakk.api.common.base.MockMvcTest
import com.cakk.api.common.fixture.FixtureCommon.fixtureMonkey
import com.cakk.api.common.fixture.FixtureCommon.getDateFixture
import com.cakk.api.common.fixture.FixtureCommon.getEnumFixture
import com.cakk.api.common.fixture.FixtureCommon.getStringFixtureBw
import com.cakk.api.dto.request.user.GenerateCodeRequest
import com.cakk.api.dto.request.user.UserSignUpRequest
import com.cakk.api.dto.request.user.VerifyEmailRequest
import com.cakk.common.enums.Gender
import com.cakk.common.enums.Provider
import com.cakk.core.dto.param.user.UserSignInParam
import com.cakk.core.dto.response.user.JwtResponse
import org.mockito.kotlin.any

internal class SignControllerTest : MockMvcTest() {

	@TestWithDisplayName("")
	fun signUp() {
		// given
		val dto = fixtureMonkey.giveMeBuilder(UserSignUpRequest::class.java)
			.set("provider", getEnumFixture(Provider::class.java))
			.set("idToken", getStringFixtureBw(100, 200))
			.set("nickname", getStringFixtureBw(2, 10))
			.set("email", getStringFixtureBw(5, 20))
			.set("birthday", getDateFixture())
			.set("gender", getEnumFixture(Gender::class.java))
			.sample()
		val jwt = fixtureMonkey.giveMeBuilder(JwtResponse::class.java)
			.setNotNull("accessToken")
			.setNotNull("refreshToken")
			.setNotNull("grantType")
			.sample()

		doReturn(jwt).`when`(signService).signUp(any())

		// when & then
		mockMvc.perform(
			post("/sign-up")
				.content(objectMapper.writeValueAsString(dto))
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
