package com.cakk.core.dto.response.like

import com.cakk.domain.mysql.dto.param.like.HeartCakeImageResponseParam

data class HeartCakeImageListResponse(
    val cakeImages: List<HeartCakeImageResponseParam>,
    val lastCakeHeartId: Long?,
    val size: Int
)
