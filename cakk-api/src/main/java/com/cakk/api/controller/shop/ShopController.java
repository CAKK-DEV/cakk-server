package com.cakk.api.controller.shop;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cakk.api.dto.request.user.CertificationRequest;
import com.cakk.api.service.shop.ShopService;
import com.cakk.common.response.ApiResponse;
import com.cakk.domain.entity.user.User;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/shops")
public class ShopController {

	private final ShopService shopService;

	@PostMapping("/certification")
	public ApiResponse<Void> requestCertification(
		User user,
		@Valid @RequestBody CertificationRequest certificationRequest) {
		shopService.requestCertificationShopKeeper(certificationRequest.from(user));
		return ApiResponse.success(null);
	}
}
