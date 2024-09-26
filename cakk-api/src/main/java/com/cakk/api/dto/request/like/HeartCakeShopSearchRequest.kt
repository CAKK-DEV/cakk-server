package com.cakk.api.dto.request.like

import com.cakk.common.utils.DEFAULT_PAGE_SIZE

data class HeartCakeShopSearchRequest(
    val cakeShopHeartId: Long? = null,
    val pageSize: Int? = DEFAULT_PAGE_SIZE
)
