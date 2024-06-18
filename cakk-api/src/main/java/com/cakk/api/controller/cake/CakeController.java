package com.cakk.api.controller.cake;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

import com.cakk.api.annotation.SignInUser;
import com.cakk.api.dto.request.cake.CakeSearchByCategoryRequest;
import com.cakk.api.dto.request.cake.CakeSearchByLocationRequest;
import com.cakk.api.dto.request.cake.CakeSearchByShopRequest;
import com.cakk.api.dto.request.cake.CakeSearchByViewsRequest;
import com.cakk.api.dto.request.cake.CakeUpdateRequest;
import com.cakk.api.dto.response.cake.CakeImageListResponse;
import com.cakk.api.service.cake.CakeService;
import com.cakk.api.service.like.HeartService;
import com.cakk.common.response.ApiResponse;
import com.cakk.domain.mysql.entity.user.User;

@RestController
@RequiredArgsConstructor
@RequestMapping("/cakes")
public class CakeController {

	private final CakeService cakeService;
	private final HeartService heartService;

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
		@Valid @ModelAttribute CakeSearchByLocationRequest request
	) {
		final CakeImageListResponse response = cakeService.findCakeImagesByCursorAndSearch(request);

		return ApiResponse.success(response);
	}

	@GetMapping("/search/views")
	public ApiResponse<CakeImageListResponse> listByViews(
		@Valid @ModelAttribute CakeSearchByViewsRequest request
	) {
		final CakeImageListResponse response = cakeService.searchCakeImagesByCursorAndViews(request);

		return ApiResponse.success(response);
	}

	@PutMapping("/{cakeId}/heart")
	public ApiResponse<Void> heart(
		@SignInUser User user,
		@PathVariable Long cakeId
	) {
		heartService.heartCake(user, cakeId);

		return ApiResponse.success();
	}

	@PutMapping("/{cakeId}")
	public ApiResponse<Void> updateCake(
		@SignInUser User user,
		@PathVariable Long cakeId,
		@Valid @RequestBody CakeUpdateRequest request) {
		cakeService.updateCake(request.toParam(user, cakeId));

		return ApiResponse.success();
	}

	@DeleteMapping("/{cakeId}")
	public ApiResponse<Void> deleteCake(
		@SignInUser User user,
		@PathVariable Long cakeId
	) {
		cakeService.deleteCake(user, cakeId);

		return ApiResponse.success();
	}
}
