package com.cakk.api.controller.user;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

import com.cakk.api.annotation.AccessToken;
import com.cakk.api.annotation.RefreshToken;
import com.cakk.api.dto.request.user.GenerateCodeRequest;
import com.cakk.api.dto.request.user.UserSignInRequest;
import com.cakk.api.dto.request.user.UserSignUpRequest;
import com.cakk.api.dto.request.user.VerifyEmailRequest;
import com.cakk.api.dto.response.user.JwtResponse;
import com.cakk.api.service.user.EmailVerificationService;
import com.cakk.api.service.user.SignService;
import com.cakk.common.response.ApiResponse;

@RestController
@RequiredArgsConstructor
public class SignController {

	private final SignService signService;
	private final EmailVerificationService emailVerificationService;

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

	@PostMapping("/sign-out")
	public ApiResponse<Void> signOut(
		@AccessToken String accessToken,
		@RefreshToken String refreshToken
	) {
		signService.signOut(accessToken, refreshToken);
		return ApiResponse.success();
	}

	@PostMapping("/recreate-token")
	public ApiResponse<JwtResponse> recreate(
		@RefreshToken String refreshToken
	) {
		final JwtResponse response = signService.recreateToken(refreshToken);

		return ApiResponse.success(response);
	}

	@PostMapping("/email/request-code")
	public ApiResponse<Void> sendEmailForVerification(
		@Valid @RequestBody GenerateCodeRequest request
	) {
		emailVerificationService.sendEmailForVerification(request);

		return ApiResponse.success();
	}

	@PostMapping("/email/verify-email")
	public ApiResponse<Void> verifyEmail(
		@Valid @RequestBody VerifyEmailRequest request
	) {
		emailVerificationService.checkEmailVerificationCode(request);

		return ApiResponse.success();
	}
}
