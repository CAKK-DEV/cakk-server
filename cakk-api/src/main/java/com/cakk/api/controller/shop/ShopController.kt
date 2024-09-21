package com.cakk.api.controller.shop

import com.cakk.api.annotation.SignInUser
import com.cakk.api.dto.request.link.UpdateLinkRequest
import com.cakk.api.dto.request.operation.UpdateShopOperationRequest
import com.cakk.api.dto.request.shop.*
import com.cakk.api.dto.request.user.CertificationRequest
import com.cakk.api.dto.response.like.HeartResponse
import com.cakk.api.dto.response.shop.*
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
		@RequestBody @Valid certificationRequest: CertificationRequest
	): ApiResponse<Void> {
        shopService.requestCertificationBusinessOwner(certificationRequest.from(user))

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
        val response = shopService.searchShop(request)

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
    ): ApiResponse<Void> {
        heartService.heartCakeShop(user, cakeShopId)

		return ApiResponse.success()
    }

    @PutMapping("/{cakeShopId}/like")
    fun like(
		@SignInUser user: User,
		@PathVariable cakeShopId: Long
    ): ApiResponse<Void> {
        likeService.likeCakeShop(user, cakeShopId)

		return ApiResponse.success()
    }

    @GetMapping("/search/shops")
    fun listByKeywordAndLocation(
		@ModelAttribute @Valid request: CakeShopSearchRequest
    ): ApiResponse<CakeShopSearchResponse> {
        val response = shopService.searchShopByKeyword(request)

		return ApiResponse.success(response)
    }

    @PutMapping("/{cakeShopId}")
    fun updateBasicInformation(
		@SignInUser user: User,
		@PathVariable cakeShopId: Long,
		@RequestBody @Valid request: UpdateShopRequest
    ): ApiResponse<Void> {
        shopService.updateBasicInformation(request.toParam(user, cakeShopId))

		return ApiResponse.success()
    }

    @PutMapping("/{cakeShopId}/links")
    fun updateLinks(
		@SignInUser user: User,
		@PathVariable cakeShopId: Long,
		@RequestBody @Valid request: UpdateLinkRequest
    ): ApiResponse<Void> {
        shopService.updateShopLinks(request.toParam(user, cakeShopId))
        return ApiResponse.success()
    }

    @PutMapping("/{cakeShopId}/address")
    fun updateAddress(
		@SignInUser user: User,
		@PathVariable cakeShopId: Long,
		@RequestBody @Valid request: UpdateShopAddressRequest
    ): ApiResponse<Void> {
        shopService.updateShopAddress(request.toParam(user, cakeShopId))

		return ApiResponse.success()
    }

    @PutMapping("/{cakeShopId}/operation-days")
    fun updateOperationDays(
		@SignInUser user: User,
		@PathVariable cakeShopId: Long,
		@RequestBody @Valid request: UpdateShopOperationRequest
    ): ApiResponse<Void> {
        shopService.updateShopOperationDays(request.toParam(user, cakeShopId))

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
        val response = shopService.searchCakeShopsByCursorAndViews(request)

		return ApiResponse.success(response)
    }
}
