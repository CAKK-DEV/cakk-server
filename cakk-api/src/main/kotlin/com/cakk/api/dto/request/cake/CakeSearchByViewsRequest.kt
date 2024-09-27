package com.cakk.api.dto.request.cake

import com.cakk.common.utils.DEFAULT_PAGE_SIZE

data class CakeSearchByViewsRequest(
	 val offset: Long = 0,
	 val pageSize: Int? = DEFAULT_PAGE_SIZE
)
