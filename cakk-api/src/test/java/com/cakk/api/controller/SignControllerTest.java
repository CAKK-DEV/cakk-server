package com.cakk.api.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.springframework.http.MediaType;

import com.cakk.api.common.annotation.TestWithDisplayName;
import com.cakk.api.common.base.MockMvcTest;
import com.cakk.api.dto.request.user.GenerateCodeRequest;
import com.cakk.api.dto.request.user.VerifyEmailRequest;
import com.cakk.api.dto.response.user.JwtResponse;
import com.cakk.core.dto.param.user.UserSignInParam;
import com.cakk.core.dto.param.user.UserSignUpParam;

class SignControllerTest extends MockMvcTest {

	@TestWithDisplayName("")
	void signUp() throws Exception {
		// given
		UserSignUpParam param = getConstructorMonkey().giveMeOne(UserSignUpParam.class);
		JwtResponse jwt = getConstructorMonkey().giveMeBuilder(JwtResponse.class)
			.setNotNull("accessToken")
			.setNotNull("refreshToken")
			.setNotNull("grantType")
			.sample();

		doReturn(jwt).when(signService).signUp(param);

		// when & then
		mockMvc.perform(post("/sign-up")
			.content(objectMapper.writeValueAsString(param))
			.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.data.accessToken").exists())
			.andExpect(jsonPath("$.data.refreshToken").exists());
	}

	@TestWithDisplayName("")
	void signIn() throws Exception {
		// given
		UserSignInParam param = getConstructorMonkey().giveMeOne(UserSignInParam.class);
		JwtResponse jwt = getConstructorMonkey().giveMeBuilder(JwtResponse.class)
			.setNotNull("accessToken")
			.setNotNull("refreshToken")
			.setNotNull("grantType")
			.sample();

		doReturn(jwt).when(signService).signIn(param);

		// when & then
		mockMvc.perform(post("/sign-in")
				.content(objectMapper.writeValueAsString(param))
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
