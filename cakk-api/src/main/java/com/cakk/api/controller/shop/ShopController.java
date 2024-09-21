package com.cakk.api.controller.shop;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

import com.cakk.api.annotation.SignInUser;
import com.cakk.api.dto.request.link.UpdateLinkRequest;
import com.cakk.api.dto.request.operation.UpdateShopOperationRequest;
import com.cakk.api.dto.request.shop.CakeShopSearchByViewsRequest;
import com.cakk.api.dto.request.shop.CakeShopSearchRequest;
import com.cakk.api.dto.request.shop.SearchShopByLocationRequest;
import com.cakk.api.dto.request.shop.UpdateShopAddressRequest;
import com.cakk.api.dto.request.shop.UpdateShopRequest;
import com.cakk.api.dto.request.user.CertificationRequest;
import com.cakk.api.dto.response.like.HeartResponse;
import com.cakk.api.dto.response.shop.CakeShopByMapResponse;
import com.cakk.api.dto.response.shop.CakeShopByMineResponse;
import com.cakk.api.dto.response.shop.CakeShopDetailResponse;
import com.cakk.api.dto.response.shop.CakeShopInfoResponse;
import com.cakk.api.dto.response.shop.CakeShopOwnerResponse;
import com.cakk.api.dto.response.shop.CakeShopSearchResponse;
import com.cakk.api.dto.response.shop.CakeShopSimpleResponse;
import com.cakk.api.service.like.HeartService;
import com.cakk.api.service.like.LikeService;
import com.cakk.api.service.shop.ShopService;
import com.cakk.common.response.ApiResponse;
import com.cakk.core.service.views.ViewsService;
import com.cakk.domain.mysql.entity.user.User;

@RestController
@RequiredArgsConstructor
@RequestMapping("/shops")
public class ShopController {

	private final ShopService shopService;
	private final HeartService heartService;
	private final LikeService likeService;
	private final ViewsService viewsService;

	@PostMapping("/certification")
	public ApiResponse<Void> requestCertification(
		@SignInUser User user,
		@Valid @RequestBody CertificationRequest certificationRequest) {
		shopService.requestCertificationBusinessOwner(certificationRequest.from(user));
		return ApiResponse.success();
	}

	@GetMapping("/{cakeShopId}/simple")
	public ApiResponse<CakeShopSimpleResponse> simple(
		@PathVariable Long cakeShopId,
		@RequestParam(required = false) Long cakeId
	) {
		final CakeShopSimpleResponse response = shopService.searchSimpleById(cakeShopId);
		viewsService.increaseCakeViews(cakeId);

		return ApiResponse.success(response);
	}

	@GetMapping("/{cakeShopId}")
	public ApiResponse<CakeShopDetailResponse> details(
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
	public ApiResponse<CakeShopByMapResponse> listByLocation(
		@Valid @ModelAttribute SearchShopByLocationRequest request
	) {
		final CakeShopByMapResponse response = shopService.searchShop(request);
		return ApiResponse.success(response);
	}

	@GetMapping("/{cakeShopId}/heart")
	public ApiResponse<HeartResponse> isHeart(
		@SignInUser User user,
		@PathVariable Long cakeShopId
	) {
		final HeartResponse response = heartService.isHeartCakeShop(user, cakeShopId);
		return ApiResponse.success(response);
	}

	@GetMapping("/mine")
	public ApiResponse<CakeShopByMineResponse> getMyBusinessId(
		@SignInUser User user
	) {
		final CakeShopByMineResponse response = shopService.getMyBusinessId(user);
		return ApiResponse.success(response);
	}

	@PutMapping("/{cakeShopId}/heart")
	public ApiResponse<Void> heart(
		@SignInUser User user,
		@PathVariable Long cakeShopId
	) {
		heartService.heartCakeShop(user, cakeShopId);

		return ApiResponse.success();
	}

	@PutMapping("/{cakeShopId}/like")
	public ApiResponse<Void> like(
		@SignInUser User user,
		@PathVariable Long cakeShopId
	) {
		likeService.likeCakeShop(user, cakeShopId);

		return ApiResponse.success();
	}

	@GetMapping("/search/shops")
	public ApiResponse<CakeShopSearchResponse> listByKeywordAndLocation(
		@Valid @ModelAttribute CakeShopSearchRequest request
	) {
		final CakeShopSearchResponse response = shopService.searchShopByKeyword(request);
		return ApiResponse.success(response);
	}

	@PutMapping("/{cakeShopId}")
	public ApiResponse<Void> updateBasicInformation(
		@SignInUser User user,
		@PathVariable Long cakeShopId,
		@Valid @RequestBody UpdateShopRequest request
	) {
		shopService.updateBasicInformation(request.toParam(user, cakeShopId));

		return ApiResponse.success();
	}

	@PutMapping("/{cakeShopId}/links")
	public ApiResponse<Void> updateLinks(
		@SignInUser User user,
		@PathVariable Long cakeShopId,
		@Valid @RequestBody UpdateLinkRequest request
	) {
		shopService.updateShopLinks(request.toParam(user, cakeShopId));

		return ApiResponse.success();
	}

	@PutMapping("/{cakeShopId}/address")
	public ApiResponse<Void> updateAddress(
		@SignInUser User user,
		@PathVariable Long cakeShopId,
		@Valid @RequestBody UpdateShopAddressRequest request
	) {
		shopService.updateShopAddress(request.toParam(user, cakeShopId));

		return ApiResponse.success();
	}

	@PutMapping("/{cakeShopId}/operation-days")
	public ApiResponse<Void> updateOperationDays(
		@SignInUser User user,
		@PathVariable Long cakeShopId,
		@Valid @RequestBody UpdateShopOperationRequest request
	) {
		shopService.updateShopOperationDays(request.toParam(user, cakeShopId));

		return ApiResponse.success();
	}

	@GetMapping("/{cakeShopId}/owner")
	public ApiResponse<CakeShopOwnerResponse> existBusinessInformation(
		@SignInUser User user,
		@PathVariable Long cakeShopId
	) {
		final CakeShopOwnerResponse response = shopService.isExistBusinessInformation(user, cakeShopId);

		return ApiResponse.success(response);
	}

	@GetMapping("/search/views")
	public ApiResponse<CakeShopSearchResponse> listByViews(
		@Valid @ModelAttribute CakeShopSearchByViewsRequest request
	) {
		final CakeShopSearchResponse response = shopService.searchCakeShopsByCursorAndViews(request);

		return ApiResponse.success(response);
	}
}
