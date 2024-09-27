package com.cakk.core.dto.response.shop

data class CakeShopSimpleResponse(
    val cakeShopId: Long,
    val thumbnailUrl: String,
    val cakeShopName: String,
    val cakeShopBio: String
)
