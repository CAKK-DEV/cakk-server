package com.cakk.core.dto.response.like

import com.cakk.domain.mysql.dto.param.like.HeartCakeShopResponseParam

data class HeartCakeShopListResponse(
    val cakeShops: List<HeartCakeShopResponseParam>,
    val lastCakeShopHeartId: Long?,
    val size: Int
)
