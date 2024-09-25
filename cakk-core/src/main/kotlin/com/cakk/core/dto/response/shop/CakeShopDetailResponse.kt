package com.cakk.core.dto.response.shop

import com.cakk.common.enums.Days
import com.cakk.domain.mysql.dto.param.shop.CakeShopLinkParam

data class CakeShopDetailResponse(
    val cakeShopId: Long,
    val cakeShopName: String,
    val thumbnailUrl: String,
    val cakeShopBio: String,
    val cakeShopDescription: String,
    val operationDays: Set<Days>,
    val links: Set<CakeShopLinkParam>
)
