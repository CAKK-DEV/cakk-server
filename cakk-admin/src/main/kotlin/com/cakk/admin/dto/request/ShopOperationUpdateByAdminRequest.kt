package com.cakk.admin.dto.request

import jakarta.validation.constraints.NotNull

import com.cakk.infrastructure.persistence.param.shop.ShopOperationParam

data class ShopOperationUpdateByAdminRequest(
	@field:NotNull
	val operationDays: List<com.cakk.infrastructure.persistence.param.shop.ShopOperationParam>?
)
