package com.cakk.core.dto.response.shop

import com.cakk.infrastructure.persistence.param.shop.CakeShopOperationParam

data class CakeShopInfoResponse(
    val shopAddress: String,
    val latitude: Double,
    val longitude: Double,
    val shopOperationDays: List<com.cakk.infrastructure.persistence.param.shop.CakeShopOperationParam>
)
