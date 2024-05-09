package com.cakk.api.controller.shop;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

import com.cakk.api.annotation.SignInUser;
import com.cakk.api.dto.request.shop.CreateShopRequest;
import com.cakk.api.dto.request.shop.PromotionRequest;
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
		@SignInUser User user,
		@Valid @RequestBody CertificationRequest certificationRequest) {
		shopService.requestCertificationBusinessOwner(certificationRequest.from(user));
		return ApiResponse.success();
	}

	@PostMapping("/admin/create")
	public ApiResponse<Void> createCakeShopByAdmin(
		@Valid @RequestBody CreateShopRequest createShopRequest
	) {
		shopService.createCakeShopByCertification(createShopRequest);
		return ApiResponse.success();
	}

	@PatchMapping("/admin/promote")
	public ApiResponse<Void> promoteUser(
		@Valid @RequestBody PromotionRequest promotionRequest
	) {
		shopService.promoteUserToBusinessOwner(promotionRequest);
		return ApiResponse.success();
	}

}
