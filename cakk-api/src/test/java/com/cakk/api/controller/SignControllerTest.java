package com.cakk.api.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.springframework.http.MediaType;

import com.cakk.api.common.annotation.TestWithDisplayName;
import com.cakk.api.common.base.MockMvcTest;
import com.cakk.api.dto.request.user.GenerateCodeRequest;
import com.cakk.api.dto.request.user.UserSignInRequest;
import com.cakk.api.dto.request.user.UserSignUpRequest;
import com.cakk.api.dto.request.user.VerifyEmailRequest;
import com.cakk.api.dto.response.user.JwtResponse;

class SignControllerTest extends MockMvcTest {

	@TestWithDisplayName("")
	void signUp() throws Exception {
		// given
		UserSignUpRequest dto = getConstructorMonkey().giveMeOne(UserSignUpRequest.class);
		JwtResponse jwt = getConstructorMonkey().giveMeBuilder(JwtResponse.class)
			.setNotNull("accessToken")
			.setNotNull("refreshToken")
			.setNotNull("grantType")
			.sample();

		doReturn(jwt).when(signService).signUp(dto);

		// when & then
		mockMvc.perform(post("/sign-up")
			.content(objectMapper.writeValueAsString(dto))
			.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.data.accessToken").exists())
			.andExpect(jsonPath("$.data.refreshToken").exists());
	}

	@TestWithDisplayName("")
	void signIn() throws Exception {
		// given
		UserSignInRequest dto = getConstructorMonkey().giveMeOne(UserSignInRequest.class);
		JwtResponse jwt = getConstructorMonkey().giveMeBuilder(JwtResponse.class)
			.setNotNull("accessToken")
			.setNotNull("refreshToken")
			.setNotNull("grantType")
			.sample();

		doReturn(jwt).when(signService).signIn(dto);

		// when & then
		mockMvc.perform(post("/sign-in")
				.content(objectMapper.writeValueAsString(dto))
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.data.accessToken").exists())
			.andExpect(jsonPath("$.data.refreshToken").exists());
	}

	@TestWithDisplayName("")
	void sendEmailForVerification() throws Exception {
		// given
		GenerateCodeRequest dto = getConstructorMonkey().giveMeOne(GenerateCodeRequest.class);

		// when & then
		mockMvc.perform(post("/email/request-code")
				.content(objectMapper.writeValueAsString(dto))
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.data").doesNotExist());
	}

	@TestWithDisplayName("")
	void verifyEmail() throws Exception {
		// given
		VerifyEmailRequest dto = getConstructorMonkey().giveMeOne(VerifyEmailRequest.class);

		// when & then
		mockMvc.perform(post("/email/verify-email")
				.content(objectMapper.writeValueAsString(dto))
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.data").doesNotExist());
	}
}
