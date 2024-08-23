package com.cakk.admin.mapper

import com.cakk.admin.dto.request.CakeShopCreateByAdminRequest
import com.cakk.admin.dto.response.CakeShopCreateResponse
import com.cakk.domain.mysql.dto.param.shop.ShopOperationParam
import com.cakk.domain.mysql.entity.shop.CakeShop
import com.cakk.domain.mysql.entity.shop.CakeShopOperation

fun supplyCakeShopBy(dto: CakeShopCreateByAdminRequest): CakeShop {
    return CakeShop.builder()
        .shopName(dto.shopName)
        .shopBio(dto.shopBio)
        .shopDescription(dto.shopDescription)
        .shopAddress(dto.shopAddress)
        .location(supplyPointBy(dto.latitude, dto.longitude))
        .build()
}

fun supplyCakeShopOperationsBy(
    cakeShop: CakeShop?,
    operationDays: List<ShopOperationParam>
): List<CakeShopOperation> {
    val cakeShopOperations = mutableListOf<CakeShopOperation>()

    operationDays.forEach {
        cakeShopOperations.add(
            CakeShopOperation.builder()
                .operationDay(it.operationDay)
                .operationStartTime(it.operationStartTime)
                .operationEndTime(it.operationEndTime)
                .cakeShop(cakeShop)
                .build()
        )
    }

    return cakeShopOperations
}

fun supplyCakeShopCreateResponseBy(cakeShop: CakeShop): CakeShopCreateResponse {
    return CakeShopCreateResponse(cakeShop.id)
}
