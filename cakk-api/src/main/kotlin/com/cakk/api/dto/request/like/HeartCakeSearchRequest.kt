package com.cakk.api.dto.request.like

import com.cakk.common.utils.DEFAULT_PAGE_SIZE

data class HeartCakeSearchRequest(
    val cakeHeartId: Long? = null,
    val pageSize: Int? = DEFAULT_PAGE_SIZE
)
