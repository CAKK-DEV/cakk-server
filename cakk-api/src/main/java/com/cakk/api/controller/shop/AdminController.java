package com.cakk.api.controller.shop;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

import com.cakk.api.dto.request.shop.CreateShopRequest;
import com.cakk.api.dto.request.shop.PromotionRequest;
import com.cakk.api.dto.response.shop.CakeShopCreateResponse;
import com.cakk.api.dto.response.shop.CakeShopOwnerCandidateResponse;
import com.cakk.api.service.shop.ShopService;
import com.cakk.common.response.ApiResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {

	private final ShopService shopService;

	@GetMapping("/shops/candidates")
	public ApiResponse<CakeShopOwnerCandidateResponse> getBusinessOwnerCandidates() {
		final CakeShopOwnerCandidateResponse response =  shopService.getBusinessOwnerCandidates();

		return ApiResponse.success(response);
	}

	@PostMapping("/shops/create")
	public ApiResponse<CakeShopCreateResponse> createByAdmin(
		@Valid @RequestBody CreateShopRequest createShopRequest
	) {
		final CakeShopCreateResponse response = shopService.createCakeShopByCertification(createShopRequest);

		return ApiResponse.success(response);
	}

	@PutMapping("/shops/promote")
	public ApiResponse<Void> promoteUser(
		@Valid @RequestBody PromotionRequest promotionRequest
	) {
		shopService.promoteUserToBusinessOwner(promotionRequest);
		return ApiResponse.success();
	}
}

