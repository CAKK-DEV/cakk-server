package com.cakk.admin.dto.request

import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size

import com.cakk.admin.annotation.OperationDay
import com.cakk.core.dto.param.shop.ShopLinkParam
import com.cakk.core.dto.param.shop.ShopOperationParam

data class CakeShopCreateByAdminRequest(
	@field:Size(max = 20)
	val businessNumber: String?,
	@field:OperationDay
	val operationDays: List<ShopOperationParam>?,
	@field:NotBlank @field:Size(max = 30)
	val shopName: String?,
	@field:Size(max = 40)
	val shopBio: String?,
	@field:Size(max = 500)
	val shopDescription: String?,
	@field:NotBlank @field:Size(max = 50)
	val shopAddress: String?,
	@field:NotNull @field:Min(-90) @field:Max(90)
	val latitude: Double?,
	@field:NotNull @field:Min(-180) @field:Max(180)
	val longitude: Double?,
	@field:NotNull
	val links: List<ShopLinkParam>?
)
