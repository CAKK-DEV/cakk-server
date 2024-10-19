package com.cakk.admin.dto.request

import jakarta.validation.constraints.NotNull

import com.cakk.domain.mysql.dto.param.shop.ShopOperationParam

data class ShopOperationUpdateByAdminRequest(
	@field:NotNull
	val operationDays: List<ShopOperationParam>?
)
