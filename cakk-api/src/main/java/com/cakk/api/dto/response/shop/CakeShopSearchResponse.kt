package com.cakk.api.dto.response.shop

import com.fasterxml.jackson.annotation.JsonTypeInfo

import com.cakk.domain.mysql.dto.param.shop.CakeShopSearchResponseParam

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "@class")
data class CakeShopSearchResponse(
	val cakeShops: List<CakeShopSearchResponseParam>,
	val lastCakeShopId: Long?,
	val size: Int
)
