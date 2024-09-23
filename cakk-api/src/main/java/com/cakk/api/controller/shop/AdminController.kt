package com.cakk.api.controller.shop

import com.cakk.api.dto.request.shop.CreateShopRequest
import com.cakk.api.dto.request.shop.PromotionRequest
import com.cakk.api.dto.response.shop.CakeShopCreateResponse
import com.cakk.api.dto.response.shop.CakeShopOwnerCandidateResponse
import com.cakk.api.dto.response.shop.CakeShopOwnerCandidatesResponse
import com.cakk.api.mapper.ShopMapper
import com.cakk.api.service.shop.ShopService
import com.cakk.common.response.ApiResponse
import jakarta.validation.Valid
import lombok.RequiredArgsConstructor
import org.springframework.web.bind.annotation.*

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
class AdminController(
	private val shopService: ShopService
) {

	@GetMapping("/shops/candidates")
	fun businessOwnerCandidates(): ApiResponse<CakeShopOwnerCandidatesResponse> {
		val response = shopService.businessOwnerCandidates

		return ApiResponse.success(response)
	}

    @GetMapping("/shops/candidates/{userId}")
    fun getCandidateSpecificationInformation(
		@PathVariable userId: Long
	): ApiResponse<CakeShopOwnerCandidateResponse> {
        val response = shopService.getCandidateInformation(userId)

        return ApiResponse.success(response)
    }

    @PostMapping("/shops/create")
    fun createByAdmin(
		@RequestBody @Valid request: CreateShopRequest
    ): ApiResponse<CakeShopCreateResponse> {
		val param = ShopMapper.supplyCreateShopParamBy(request);
        val response = shopService.createCakeShopByCertification(param)

        return ApiResponse.success(response)
    }

    @PutMapping("/shops/promote")
    fun promoteUser(
		@RequestBody @Valid request: PromotionRequest
    ): ApiResponse<Unit> {
		val param = ShopMapper.supplyPromotionParamBy(request)
        shopService.promoteUserToBusinessOwner(param)

		return ApiResponse.success()
    }
}

