package com.cakk.api.dto.request.operation

import jakarta.validation.constraints.NotNull

import com.cakk.core.dto.param.shop.ShopOperationParam

data class UpdateShopOperationRequest(
	@field:NotNull
    val operationDays: MutableList<ShopOperationParam>?
)
