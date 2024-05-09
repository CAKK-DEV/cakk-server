package com.cakk.api.controller.user;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

import com.cakk.api.dto.request.user.UserSignInRequest;
import com.cakk.api.dto.request.user.UserSignUpRequest;
import com.cakk.api.dto.response.user.JwtResponse;
import com.cakk.api.service.user.UserService;
import com.cakk.common.response.ApiResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class SignController {

	private final UserService userService;

	@PostMapping("/sign-up")
	public ApiResponse<JwtResponse> signUp(@RequestBody UserSignUpRequest request) {
		return ApiResponse.success(userService.signUp(request));
	}

	@PostMapping("/sign-in")
	public ApiResponse<JwtResponse> signIn(@RequestBody UserSignInRequest request) {
		return ApiResponse.success(userService.signIn(request));
	}
}
