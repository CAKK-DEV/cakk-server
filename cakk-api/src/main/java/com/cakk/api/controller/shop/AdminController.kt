package com.cakk.api.controller.shop

import com.cakk.api.dto.request.shop.CreateShopRequest
import com.cakk.api.dto.request.shop.PromotionRequest
import com.cakk.api.dto.response.shop.CakeShopCreateResponse
import com.cakk.api.dto.response.shop.CakeShopOwnerCandidateResponse
import com.cakk.api.dto.response.shop.CakeShopOwnerCandidatesResponse
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
		@RequestBody @Valid createShopRequest: CreateShopRequest
    ): ApiResponse<CakeShopCreateResponse> {
        val response = shopService.createCakeShopByCertification(createShopRequest)

        return ApiResponse.success(response)
    }

    @PutMapping("/shops/promote")
    fun promoteUser(
		@RequestBody @Valid promotionRequest: PromotionRequest
    ): ApiResponse<Void> {
        shopService.promoteUserToBusinessOwner(promotionRequest)

		return ApiResponse.success()
    }
}

