package com.cakk.api.dto.request.cake

data class CakeSearchByViewsRequest(
	 val offset: Long = 0,
	 val pageSize: Int = 10
)
