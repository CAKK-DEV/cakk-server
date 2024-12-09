package com.cakk.core.dto.response.like

import com.cakk.infrastructure.persistence.param.like.HeartCakeImageResponseParam

data class HeartCakeImageListResponse(
    val cakeImages: List<com.cakk.infrastructure.persistence.param.like.HeartCakeImageResponseParam>,
    val lastCakeHeartId: Long?,
    val size: Int
)
