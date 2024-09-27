package com.cakk.core.dto.param.search

data class SearchShopByLocationParam(
	val latitude: Double,
	val longitude: Double,
	val distance: Double
)

