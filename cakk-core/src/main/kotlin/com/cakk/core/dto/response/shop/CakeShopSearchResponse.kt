package com.cakk.core.dto.response.shop

import com.cakk.infrastructure.persistence.param.shop.CakeShopSearchResponseParam

data class CakeShopSearchResponse(
    val cakeShops: List<com.cakk.infrastructure.persistence.param.shop.CakeShopSearchResponseParam>,
    val lastCakeShopId: Long?,
    val size: Int
)
