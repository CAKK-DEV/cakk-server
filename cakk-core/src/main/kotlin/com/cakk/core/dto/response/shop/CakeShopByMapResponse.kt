package com.cakk.core.dto.response.shop

import com.cakk.infrastructure.persistence.param.shop.CakeShopLocationResponseParam

data class CakeShopByMapResponse(
    val cakeShops: List<com.cakk.infrastructure.persistence.param.shop.CakeShopLocationResponseParam>
)

