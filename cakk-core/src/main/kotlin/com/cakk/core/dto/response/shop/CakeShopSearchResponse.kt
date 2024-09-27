package com.cakk.core.dto.response.shop

import com.cakk.domain.mysql.dto.param.shop.CakeShopSearchResponseParam

data class CakeShopSearchResponse(
    val cakeShops: List<CakeShopSearchResponseParam>,
    val lastCakeShopId: Long?,
    val size: Int
)
