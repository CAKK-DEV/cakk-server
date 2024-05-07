package com.cakk.api.controller.user;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cakk.api.dto.request.user.CertificationRequest;
import com.cakk.api.service.user.UserService;
import com.cakk.common.response.ApiResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

	private final UserService userService;

	@PostMapping("/certification")
	public ApiResponse<Void> requestCertification(@Valid @RequestBody CertificationRequest certificationRequest) {
		return userService.requestCertificationShopKeeper(certificationRequest.from());
	}
}
