package com.cakk.core.dto.response.like

import com.cakk.infrastructure.persistence.param.like.HeartCakeShopResponseParam

data class HeartCakeShopListResponse(
    val cakeShops: List<com.cakk.infrastructure.persistence.param.like.HeartCakeShopResponseParam>,
    val lastCakeShopHeartId: Long?,
    val size: Int
)
