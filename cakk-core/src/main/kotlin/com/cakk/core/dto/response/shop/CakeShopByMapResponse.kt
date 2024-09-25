package com.cakk.core.dto.response.shop

import com.cakk.domain.mysql.dto.param.shop.CakeShopLocationResponseParam

data class CakeShopByMapResponse(
    val cakeShops: List<CakeShopLocationResponseParam>
)

