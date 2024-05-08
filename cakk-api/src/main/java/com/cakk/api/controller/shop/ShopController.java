package com.cakk.api.controller.shop;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

import com.cakk.api.dto.request.user.CertificationRequest;
import com.cakk.api.service.shop.ShopService;
import com.cakk.common.response.ApiResponse;
import com.cakk.domain.entity.user.User;

@RestController
@RequiredArgsConstructor
@RequestMapping("/shops")
public class ShopController {

	private final ShopService shopService;

	@PostMapping("/certification")
	public ApiResponse<Void> requestCertification(
		User user,
		@Valid @RequestBody CertificationRequest certificationRequest) {
		shopService.requestCertificationBusinessOwner(certificationRequest.from(user));
		return ApiResponse.success(null);
	}

}
