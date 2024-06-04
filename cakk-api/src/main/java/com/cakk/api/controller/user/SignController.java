package com.cakk.api.controller.user;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

import com.cakk.api.annotation.RefreshToken;
import com.cakk.api.dto.request.user.UserSignInRequest;
import com.cakk.api.dto.request.user.UserSignUpRequest;
import com.cakk.api.dto.response.user.JwtResponse;
import com.cakk.api.service.user.SignService;
import com.cakk.common.response.ApiResponse;

@RestController
@RequiredArgsConstructor
public class SignController {

	private final SignService signService;

	@PostMapping("/sign-up")
	public ApiResponse<JwtResponse> signUp(
		@Valid @RequestBody UserSignUpRequest request
	) {
		return ApiResponse.success(signService.signUp(request));
	}

	@PostMapping("/sign-in")
	public ApiResponse<JwtResponse> signIn(
		@Valid @RequestBody UserSignInRequest request
	) {
		return ApiResponse.success(signService.signIn(request));
	}

	@PostMapping("/recreate-token")
	public ApiResponse<JwtResponse> recreate(
		@RefreshToken String refreshToken
	) {
		final JwtResponse response = signService.recreateToken(refreshToken);

		return ApiResponse.success(response);
	}
}
