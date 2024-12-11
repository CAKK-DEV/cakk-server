package com.cakk.api.mapper

import com.cakk.api.dto.request.like.HeartCakeSearchRequest
import com.cakk.api.dto.request.like.HeartCakeShopSearchRequest
import com.cakk.api.dto.request.search.TopSearchedListRequest
import com.cakk.api.dto.request.shop.CakeShopSearchByViewsRequest
import com.cakk.api.dto.request.shop.CakeShopSearchRequest
import com.cakk.api.dto.request.shop.SearchShopByLocationRequest
import com.cakk.core.dto.param.search.*
import com.cakk.core.mapper.supplyPointBy
import com.cakk.infrastructure.persistence.entity.user.UserEntity

fun supplyTopSearchedListParamBy(
	topSearchedListRequest: TopSearchedListRequest
): TopSearchedListParam {
	return TopSearchedListParam(topSearchedListRequest.count!!)
}

fun supplySearchShopByLocationParamBy(request: SearchShopByLocationRequest): SearchShopByLocationParam {
	return SearchShopByLocationParam(request.latitude!!, request.longitude!!, request.distance!!)
}

fun supplyCakeShopSearchParamBy(request: CakeShopSearchRequest): com.cakk.infrastructure.persistence.param.shop.CakeShopSearchParam {
	return com.cakk.infrastructure.persistence.param.shop.CakeShopSearchParam(
		request.cakeShopId,
		request.keyword,
		supplyPointBy(request.latitude, request.longitude),
		request.pageSize
	)
}

fun supplyCakeShopSearchByViewsParam(
	request: CakeShopSearchByViewsRequest
): CakeShopSearchByViewsParam {
	return CakeShopSearchByViewsParam(request.offset, request.pageSize!!)
}

fun supplyHeartCakeShopSearchParamBy(
	request: HeartCakeShopSearchRequest,
	userEntity: UserEntity
): HeartCakeShopSearchParam {
	return HeartCakeShopSearchParam(request.cakeShopHeartId, request.pageSize!!, userEntity)
}

fun supplyHeartCakeSearchParamBy(
	request: HeartCakeSearchRequest,
	userEntity: UserEntity
): HeartCakeSearchParam {
	return HeartCakeSearchParam(request.cakeHeartId, request.pageSize!!, userEntity)
}
