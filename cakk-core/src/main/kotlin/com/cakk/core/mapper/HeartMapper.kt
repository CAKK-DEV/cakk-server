package com.cakk.core.mapper

import com.cakk.core.dto.response.like.HeartResponse

fun supplyHeartResponseBy(isHeart: Boolean): HeartResponse {
    return HeartResponse(isHeart)
}
