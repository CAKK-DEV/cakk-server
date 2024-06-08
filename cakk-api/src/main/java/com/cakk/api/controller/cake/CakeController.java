package com.cakk.api.controller.cake;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

import com.cakk.api.dto.request.cake.CakeImageSearchRequest;
import com.cakk.api.dto.request.cake.CakeSearchByCategoryRequest;
import com.cakk.api.dto.request.cake.CakeSearchByShopRequest;
import com.cakk.api.dto.response.cake.CakeImageListResponse;
import com.cakk.api.service.cake.CakeService;
import com.cakk.common.response.ApiResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/cakes")
public class CakeController {

	private final CakeService cakeService;

	@GetMapping("/search/categories")
	public ApiResponse<CakeImageListResponse> listByCategory(
		@Valid @ModelAttribute CakeSearchByCategoryRequest request
	) {
		final CakeImageListResponse response = cakeService.findCakeImagesByCursorAndCategory(request);

		return ApiResponse.success(response);
	}

	@GetMapping("/search/shops")
	public ApiResponse<CakeImageListResponse> listByShop(
		@Valid @ModelAttribute CakeSearchByShopRequest request
	) {
		final CakeImageListResponse response = cakeService.findCakeImagesByCursorAndCakeShopId(request);

		return ApiResponse.success(response);
	}

	@GetMapping("/search/cakes")
	public ApiResponse<CakeImageListResponse> searchCakes(
		@Valid @ModelAttribute CakeImageSearchRequest request
	) {
		final CakeImageListResponse response = cakeService.findCakeImagesByCursorAndSearch(request);

		return ApiResponse.success(response);
	}
}
