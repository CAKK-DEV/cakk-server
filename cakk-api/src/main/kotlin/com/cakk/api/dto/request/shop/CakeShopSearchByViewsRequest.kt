package com.cakk.api.dto.request.shop

import com.cakk.common.utils.DEFAULT_PAGE_SIZE

data class CakeShopSearchByViewsRequest(
    val offset: Long?,
    val pageSize: Int? = DEFAULT_PAGE_SIZE
)
