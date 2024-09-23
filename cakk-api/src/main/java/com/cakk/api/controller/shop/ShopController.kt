package com.cakk.api.controller.shop

import com.cakk.api.annotation.SignInUser
import com.cakk.api.dto.request.link.UpdateLinkRequest
import com.cakk.api.dto.request.operation.UpdateShopOperationRequest
import com.cakk.api.dto.request.shop.*
import com.cakk.api.dto.request.user.CertificationRequest
import com.cakk.core.dto.response.like.HeartResponse
import com.cakk.api.dto.response.shop.*
import com.cakk.api.mapper.SearchMapper
import com.cakk.api.mapper.ShopMapper
import com.cakk.api.service.like.HeartService
import com.cakk.api.service.like.LikeService
import com.cakk.api.service.shop.ShopService
import com.cakk.common.response.ApiResponse
import com.cakk.core.service.views.ViewsService
import com.cakk.domain.mysql.entity.user.User
import jakarta.validation.Valid
import lombok.RequiredArgsConstructor
import org.springframework.web.bind.annotation.*

@RestController
@RequiredArgsConstructor
@RequestMapping("/shops")
class ShopController(
	private val shopService: ShopService,
	private val heartService: HeartService,
	private val likeService: LikeService,
	private val viewsService: ViewsService
) {

    @PostMapping("/certification")
    fun requestCertification(
		@SignInUser user: User,
		@RequestBody @Valid request: CertificationRequest
	): ApiResponse<Unit> {
		val param = ShopMapper.supplyCertificationParamBy(request, user)
        shopService.requestCertificationBusinessOwner(param)

		return ApiResponse.success()
    }

    @GetMapping("/{cakeShopId}/simple")
    fun simple(
		@PathVariable cakeShopId: Long,
		@RequestParam(required = false) cakeId: Long?
    ): ApiResponse<CakeShopSimpleResponse> {
        val response = shopService.searchSimpleById(cakeShopId)
		viewsService.increaseCakeViews(cakeId)

		return ApiResponse.success(response)
    }

    @GetMapping("/{cakeShopId}")
    fun details(
		@PathVariable cakeShopId: Long
    ): ApiResponse<CakeShopDetailResponse> {
        val response = shopService.searchDetailById(cakeShopId)

		return ApiResponse.success(response)
    }

    @GetMapping("/{cakeShopId}/info")
    fun detailInfo(
		@PathVariable cakeShopId: Long
    ): ApiResponse<CakeShopInfoResponse> {
        val response = shopService.searchInfoById(cakeShopId)

		return ApiResponse.success(response)
    }

    @GetMapping("/location-based")
    fun listByLocation(
		@ModelAttribute @Valid request: SearchShopByLocationRequest
    ): ApiResponse<CakeShopByMapResponse> {
		val param = SearchMapper.supplySearchShopByLocationParamBy(request)
        val response = shopService.searchShop(param)

		return ApiResponse.success(response)
    }

    @GetMapping("/{cakeShopId}/heart")
    fun isHeart(
		@SignInUser user: User,
		@PathVariable cakeShopId: Long
    ): ApiResponse<HeartResponse> {
        val response = heartService.isHeartCakeShop(user, cakeShopId)

		return ApiResponse.success(response)
    }

    @GetMapping("/mine")
    fun getMyBusinessId(
		@SignInUser user: User
    ): ApiResponse<CakeShopByMineResponse> {
        val response = shopService.getMyBusinessId(user)

		return ApiResponse.success(response)
    }

    @PutMapping("/{cakeShopId}/heart")
    fun heart(
		@SignInUser user: User,
		@PathVariable cakeShopId: Long
    ): ApiResponse<Unit> {
        heartService.heartCakeShop(user, cakeShopId)

		return ApiResponse.success()
    }

    @PutMapping("/{cakeShopId}/like")
    fun like(
		@SignInUser user: User,
		@PathVariable cakeShopId: Long
    ): ApiResponse<Unit> {
        likeService.likeCakeShop(user, cakeShopId)

		return ApiResponse.success()
    }

    @GetMapping("/search/shops")
    fun listByKeywordAndLocation(
		@ModelAttribute @Valid request: CakeShopSearchRequest
    ): ApiResponse<CakeShopSearchResponse> {
		val param = SearchMapper.supplyCakeShopSearchParamBy(request)
        val response = shopService.searchShopByKeyword(param)

		return ApiResponse.success(response)
    }

    @PutMapping("/{cakeShopId}")
    fun updateBasicInformation(
		@SignInUser user: User,
		@PathVariable cakeShopId: Long,
		@RequestBody @Valid request: UpdateShopRequest
    ): ApiResponse<Unit> {
		val param = ShopMapper.supplyCakeShopUpdateParamBy(request, user, cakeShopId)
        shopService.updateBasicInformation(param)

		return ApiResponse.success()
    }

    @PutMapping("/{cakeShopId}/links")
    fun updateLinks(
		@SignInUser user: User,
		@PathVariable cakeShopId: Long,
		@RequestBody @Valid request: UpdateLinkRequest
    ): ApiResponse<Unit> {
		val param = ShopMapper.supplyUpdateLinkParamBy(request, user, cakeShopId)
        shopService.updateShopLinks(param)

		return ApiResponse.success()
    }

    @PutMapping("/{cakeShopId}/address")
    fun updateAddress(
		@SignInUser user: User,
		@PathVariable cakeShopId: Long,
		@RequestBody @Valid request: UpdateShopAddressRequest
    ): ApiResponse<Unit> {
        shopService.updateShopAddress(request.toParam(user, cakeShopId))

		return ApiResponse.success()
    }

    @PutMapping("/{cakeShopId}/operation-days")
    fun updateOperationDays(
		@SignInUser user: User,
		@PathVariable cakeShopId: Long,
		@RequestBody @Valid request: UpdateShopOperationRequest
    ): ApiResponse<Unit> {
		val param = ShopMapper.supplyUpdateShopOperationParamBy(request, user, cakeShopId)
        shopService.updateShopOperationDays(param)

		return ApiResponse.success()
    }

    @GetMapping("/{cakeShopId}/owner")
    fun existBusinessInformation(
		@SignInUser user: User,
		@PathVariable cakeShopId: Long
    ): ApiResponse<CakeShopOwnerResponse> {
        val response = shopService.isExistBusinessInformation(user, cakeShopId)

		return ApiResponse.success(response)
    }

    @GetMapping("/search/views")
    fun listByViews(
		@ModelAttribute @Valid request: CakeShopSearchByViewsRequest
    ): ApiResponse<CakeShopSearchResponse> {
		val param = SearchMapper.supplyCakeShopSearchByViewsParam(request)
        val response = shopService.searchCakeShopsByCursorAndViews(param)

		return ApiResponse.success(response)
    }
}
