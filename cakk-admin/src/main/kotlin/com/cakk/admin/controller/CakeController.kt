package com.cakk.admin.controller

import jakarta.validation.Valid

import org.springframework.web.bind.annotation.*

import com.cakk.admin.annotation.AdminUser
import com.cakk.admin.dto.request.CakeCreateByAdminRequest
import com.cakk.admin.dto.request.CakeUpdateByAdminRequest
import com.cakk.admin.mapper.supplyCakeCreateParamBy
import com.cakk.admin.mapper.supplyCakeUpdateParamBy
import com.cakk.common.response.ApiResponse
import com.cakk.core.service.cake.CakeService
import com.cakk.domain.mysql.entity.user.User

@RestController
@RequestMapping("/shops/{cakeShopId}/cakes")
class CakeController(
	private val cakeService: CakeService
) {

	@PostMapping
	fun create(
		@PathVariable cakeShopId: Long,
		@RequestBody @Valid dto: CakeCreateByAdminRequest,
		@AdminUser admin: User
	): ApiResponse<Unit> {
		val param = supplyCakeCreateParamBy(dto, admin, cakeShopId)
		cakeService.createCake(param)

		return ApiResponse.success()
	}

	@PutMapping("/{cakeId}")
	fun update(
		@PathVariable cakeShopId: Long,
		@PathVariable cakeId: Long,
		@RequestBody @Valid dto: CakeUpdateByAdminRequest,
		@AdminUser admin: User
	): ApiResponse<Unit> {
		val param = supplyCakeUpdateParamBy(dto, admin, cakeShopId)
		cakeService.updateCake(param)

		return ApiResponse.success()
	}

	@DeleteMapping("/{cakeId}")
	fun delete(
		@PathVariable cakeShopId: Long,
		@PathVariable cakeId: Long,
		@AdminUser admin: User
	): ApiResponse<Unit> {
		cakeService.deleteCake(admin, cakeId)

		return ApiResponse.success()
	}
}
