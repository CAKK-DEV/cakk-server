package com.cakk.api.controller.shop;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

import com.cakk.api.annotation.SignInUser;
import com.cakk.api.dto.request.shop.CakeShopSearchRequest;
import com.cakk.api.dto.request.shop.CreateShopRequest;
import com.cakk.api.dto.request.shop.PromotionRequest;
import com.cakk.api.dto.request.shop.SearchShopByLocationRequest;
import com.cakk.api.dto.request.shop.UpdateShopRequest;
import com.cakk.api.dto.request.user.CertificationRequest;
import com.cakk.api.dto.response.shop.CakeShopByMapResponse;
import com.cakk.api.dto.response.shop.CakeShopDetailResponse;
import com.cakk.api.dto.response.shop.CakeShopInfoResponse;
import com.cakk.api.dto.response.shop.CakeShopSearchResponse;
import com.cakk.api.dto.response.shop.CakeShopSimpleResponse;
import com.cakk.api.service.like.HeartService;
import com.cakk.api.service.shop.ShopService;
import com.cakk.common.response.ApiResponse;
import com.cakk.domain.mysql.entity.user.User;

@RestController
@RequiredArgsConstructor
@RequestMapping("/shops")
public class ShopController {

	private final ShopService shopService;
	private final HeartService heartService;

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

	@GetMapping("/{cakeShopId}/simple")
	public ApiResponse<CakeShopSimpleResponse> simple(
		@PathVariable Long cakeShopId
	) {
		final CakeShopSimpleResponse response = shopService.searchSimpleById(cakeShopId);
		return ApiResponse.success(response);
	}

	@GetMapping("/{cakeShopId}")
	public ApiResponse<CakeShopDetailResponse> detail(
		@PathVariable Long cakeShopId
	) {
		final CakeShopDetailResponse response = shopService.searchDetailById(cakeShopId);
		return ApiResponse.success(response);
	}

	@GetMapping("/{cakeShopId}/info")
	public ApiResponse<CakeShopInfoResponse> detailInfo(
		@PathVariable Long cakeShopId
	) {
		final CakeShopInfoResponse response = shopService.searchInfoById(cakeShopId);
		return ApiResponse.success(response);
	}

	@GetMapping("/location-based")
	public ApiResponse<CakeShopByMapResponse> searchShop(
		@Valid @ModelAttribute SearchShopByLocationRequest request
	) {
		final CakeShopByMapResponse response = shopService.searchShop(request);
		return ApiResponse.success(response);
	}

	@PutMapping("/{cakeShopId}/heart")
	public ApiResponse<Void> heart(
		@SignInUser User user,
		@PathVariable Long cakeShopId
	) {
		heartService.heartCakeShopWithLock(user, cakeShopId);

		return ApiResponse.success();
	}

	@GetMapping("/search/shops")
	public ApiResponse<CakeShopSearchResponse> searchShopByKeyword(
		@Valid @ModelAttribute CakeShopSearchRequest request
	) {
		final CakeShopSearchResponse response = shopService.searchShopByKeyword(request);
		return ApiResponse.success(response);
	}

	@PutMapping("/{cakeShopId}")
	public ApiResponse<Void> updateDefaultInformation(
		@SignInUser User user,
		@PathVariable Long cakeShopId,
		@Valid @RequestBody UpdateShopRequest request
	) {
		shopService.updateDefaultInformation(request.toParam(user, cakeShopId));

		return ApiResponse.success();
	}
}
