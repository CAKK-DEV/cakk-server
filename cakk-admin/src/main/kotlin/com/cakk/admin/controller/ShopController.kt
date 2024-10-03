package com.cakk.admin.controller

import jakarta.validation.Valid

import org.springframework.web.bind.annotation.*

import com.cakk.admin.annotation.AdminUser
import com.cakk.admin.dto.request.*
import com.cakk.admin.mapper.*
import com.cakk.common.response.ApiResponse
import com.cakk.core.dto.response.shop.CakeShopCreateResponse
import com.cakk.core.service.shop.ShopService
import com.cakk.domain.mysql.entity.user.User

@RestController
@RequestMapping("/shops")
class ShopController(
	private val shopService: ShopService
) {

	@PostMapping
	fun createByAdmin(
		@RequestBody @Valid request: CakeShopCreateByAdminRequest
	): ApiResponse<CakeShopCreateResponse> {
		val param = supplyCreateShopParamBy(request)
		val response = shopService.createCakeShopByCertification(param)

		return ApiResponse.success(response)
	}

	@PutMapping("/{cakeShopId}")
	fun updateBasicInformation(
		@PathVariable cakeShopId: Long,
		@RequestBody @Valid request: CakeShopUpdateByAdminRequest,
		@AdminUser admin: User
	): ApiResponse<Unit> {
		val param = supplyCakeShopUpdateParamBy(request, admin, cakeShopId)
		shopService.updateBasicInformation(param)

		return ApiResponse.success()
	}

	@PutMapping("/{cakeShopId}/links")
	fun updateLinks(
		@PathVariable cakeShopId: Long,
		@RequestBody @Valid request: LinkUpdateByAdminRequest,
		@AdminUser admin: User
	): ApiResponse<Unit> {
		val param = supplyUpdateLinkParamBy(request, admin, cakeShopId)
		shopService.updateShopLinks(param)

		return ApiResponse.success()
	}

	@PutMapping("/{cakeShopId}/operation-days")
	fun updateOperationDays(
		@PathVariable cakeShopId: Long,
		@RequestBody @Valid request: ShopOperationUpdateByAdminRequest,
		@AdminUser admin: User
	): ApiResponse<Unit> {
		val param = supplyUpdateShopOperationParamBy(request, admin, cakeShopId)
		shopService.updateShopOperationDays(param)

		return ApiResponse.success()
	}

	@PutMapping("/{cakeShopId}/address")
	fun updateAddress(
		@PathVariable cakeShopId: Long,
		@RequestBody @Valid request: AddressUpdateByAdminRequest,
		@AdminUser admin: User
	): ApiResponse<Unit> {
		val param = supplyUpdateShopAddressParamBy(request, admin, cakeShopId)
		shopService.updateShopAddress(param)

		return ApiResponse.success()
	}
}
