package com.cakk.core.dto.response.shop

import com.cakk.domain.mysql.dto.param.shop.CakeShopOperationParam

data class CakeShopInfoResponse(
    val shopAddress: String,
    val latitude: Double,
    val longitude: Double,
    val shopOperationDays: List<CakeShopOperationParam>
)
