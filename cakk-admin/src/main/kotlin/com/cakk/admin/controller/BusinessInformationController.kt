package com.cakk.admin.controller

import jakarta.validation.Valid

import org.springframework.web.bind.annotation.*

import com.cakk.admin.dto.request.PromotionRequest
import com.cakk.admin.mapper.supplyPromotionParamBy
import com.cakk.common.response.ApiResponse
import com.cakk.core.dto.response.shop.CakeShopOwnerCandidateResponse
import com.cakk.core.dto.response.shop.CakeShopOwnerCandidatesResponse
import com.cakk.core.service.shop.ShopService

@RestController
@RequestMapping("/business-information")
class BusinessInformationController(
	private val shopService: ShopService
) {

	@GetMapping("/candidates")
	fun getBusinessOwnerCandidates(): ApiResponse<CakeShopOwnerCandidatesResponse> {
		val response = shopService.getBusinessOwnerCandidates()
		return ApiResponse.success(response)
	}

	@GetMapping("/candidates/{userId}")
	fun getCandidateSpecificationInformation(
		@PathVariable userId: Long
	): ApiResponse<CakeShopOwnerCandidateResponse> {
		val response = shopService.getCandidateInformation(userId)

		return ApiResponse.success(response)
	}

	@PutMapping("/promote")
	fun promoteUser(
		@RequestBody @Valid promotionRequest: PromotionRequest
	): ApiResponse<Unit> {
		val param = supplyPromotionParamBy(promotionRequest)
		shopService.promoteUserToBusinessOwner(param)

		return ApiResponse.success()
	}
}
