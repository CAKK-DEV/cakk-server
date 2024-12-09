package com.cakk.core.dto.response.cake

import com.cakk.infrastructure.persistence.param.cake.CakeImageWithShopInfoResponseParam

data class CakeImageWithShopInfoListResponse(
    val cakeImages: List<com.cakk.infrastructure.persistence.param.cake.CakeImageWithShopInfoResponseParam>,
    val lastCakeId: Long?,
    val size: Int
)

