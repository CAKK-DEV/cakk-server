package com.cakk.api.controller.cake

import jakarta.validation.Valid

import org.springframework.web.bind.annotation.*

import com.cakk.api.annotation.SignInUser
import com.cakk.api.dto.request.cake.*
import com.cakk.api.mapper.*
import com.cakk.core.dto.response.cake.CakeDetailResponse
import com.cakk.core.dto.response.like.HeartResponse
import com.cakk.core.service.cake.CakeService
import com.cakk.core.service.like.HeartService
import com.cakk.common.response.ApiResponse
import com.cakk.core.dto.response.cake.CakeImageWithShopInfoListResponse
import com.cakk.infrastructure.persistence.entity.user.User

@RestController
@RequestMapping("/cakes")
class CakeController(
	private val cakeService: CakeService,
	private val heartService: HeartService
) {

	@GetMapping("/search/categories")
	fun listByCategory(
		@ModelAttribute @Valid request: CakeSearchByCategoryRequest
	): ApiResponse<CakeImageWithShopInfoListResponse> {
		val param = supplyCakeSearchByCategoryParamBy(request)
		val response = cakeService.findCakeImagesByCursorAndCategory(param)

		return ApiResponse.success(response)
	}

	@GetMapping("/search/shops")
	fun listByShop(
		@ModelAttribute @Valid request: CakeSearchByShopRequest
	): ApiResponse<CakeImageWithShopInfoListResponse> {
		val param = supplyCakeSearchByShopParamBy(request)
		val response = cakeService.findCakeImagesByCursorAndCakeShopId(param)

		return ApiResponse.success(response)
	}

	@GetMapping("/search/cakes")
	fun listByKeywordAndLocation(
		@ModelAttribute @Valid request: CakeSearchByLocationRequest
	): ApiResponse<CakeImageWithShopInfoListResponse> {
		val param = supplyCakeSearchParamBy(request)
		val response = cakeService.findCakeImagesByCursorAndSearch(param)

		return ApiResponse.success(response)
	}

	@GetMapping("/search/views")
	fun listByViews(
		@ModelAttribute @Valid request: CakeSearchByViewsRequest
	): ApiResponse<CakeImageWithShopInfoListResponse> {
		val param = supplyCakeSearchByViewsParamBy(request)
		val response = cakeService.searchCakeImagesByCursorAndViews(param)

		return ApiResponse.success(response)
	}

	@GetMapping("/{cakeId}")
	fun details(@PathVariable cakeId: Long): ApiResponse<CakeDetailResponse> {
		val response = cakeService.findCakeDetailById(cakeId)

		return ApiResponse.success(response)
	}

	@PostMapping("/{cakeShopId}")
	fun create(
		@SignInUser user: com.cakk.infrastructure.persistence.entity.user.User,
		@PathVariable cakeShopId: Long,
		@RequestBody @Valid request: CakeCreateRequest
	): ApiResponse<Unit> {
		val param = supplyCakeCreateParamBy(request, user, cakeShopId)
		cakeService.createCake(param)

		return ApiResponse.success()
	}

	@GetMapping("/{cakeId}/heart")
	fun isHeart(
		@SignInUser user: com.cakk.infrastructure.persistence.entity.user.User,
		@PathVariable cakeId: Long
	): ApiResponse<HeartResponse> {
		val response = heartService.isHeartCake(user, cakeId)

		return ApiResponse.success(response)
	}

	@PutMapping("/{cakeId}/heart")
	fun heart(
		@SignInUser user: com.cakk.infrastructure.persistence.entity.user.User,
		@PathVariable cakeId: Long
	): ApiResponse<Unit> {
		heartService.heartCake(user, cakeId)

		return ApiResponse.success()
	}

	@PutMapping("/{cakeId}")
	fun update(
		@SignInUser user: com.cakk.infrastructure.persistence.entity.user.User,
		@PathVariable cakeId: Long,
		@RequestBody request: @Valid CakeUpdateRequest
	): ApiResponse<Unit> {
		val param = supplyCakeUpdateParamBy(request, user, cakeId)
		cakeService.updateCake(param)

		return ApiResponse.success()
	}

	@DeleteMapping("/{cakeId}")
	fun delete(
		@SignInUser user: com.cakk.infrastructure.persistence.entity.user.User,
		@PathVariable cakeId: Long
	): ApiResponse<Unit> {
		cakeService.deleteCake(user, cakeId)

		return ApiResponse.success()
	}
}
