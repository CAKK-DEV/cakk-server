package com.cakk.api.mapper

import com.cakk.api.dto.request.like.HeartCakeSearchRequest
import com.cakk.api.dto.request.like.HeartCakeShopSearchRequest
import com.cakk.api.dto.request.search.TopSearchedListRequest
import com.cakk.api.dto.request.shop.CakeShopSearchByViewsRequest
import com.cakk.api.dto.request.shop.CakeShopSearchRequest
import com.cakk.api.dto.request.shop.SearchShopByLocationRequest
import com.cakk.core.dto.param.search.*
import com.cakk.core.mapper.supplyPointBy
import com.cakk.domain.mysql.dto.param.shop.CakeShopSearchParam
import com.cakk.domain.mysql.entity.user.User

fun supplyTopSearchedListParamBy(
	topSearchedListRequest: TopSearchedListRequest
): TopSearchedListParam {
	return TopSearchedListParam(topSearchedListRequest.count!!)
}

fun supplySearchShopByLocationParamBy(request: SearchShopByLocationRequest): SearchShopByLocationParam {
	return SearchShopByLocationParam(request.latitude, request.longitude, request.distance)
}

fun supplyCakeShopSearchParamBy(request: CakeShopSearchRequest): CakeShopSearchParam {
	return CakeShopSearchParam(
		request.cakeShopId,
		request.keyword,
		supplyPointBy(request.latitude, request.longitude),
		request.pageSize
	)
}

fun supplyCakeShopSearchByViewsParam(
	request: CakeShopSearchByViewsRequest
): CakeShopSearchByViewsParam {
	return CakeShopSearchByViewsParam(request.offset, request.pageSize)
}

fun supplyHeartCakeShopSearchParamBy(
	request: HeartCakeShopSearchRequest,
	user: User
): HeartCakeShopSearchParam {
	return HeartCakeShopSearchParam(request.cakeShopHeartId, request.pageSize!!, user)
}

fun supplyHeartCakeSearchParamBy(
	request: HeartCakeSearchRequest,
	user: User
): HeartCakeSearchParam {
	return HeartCakeSearchParam(request.cakeHeartId, request.pageSize!!, user)
}
