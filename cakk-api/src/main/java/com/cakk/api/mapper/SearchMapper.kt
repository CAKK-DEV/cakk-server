package com.cakk.api.mapper;

import java.util.List;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import com.cakk.api.dto.request.like.HeartCakeSearchRequest;
import com.cakk.api.dto.request.like.HeartCakeShopSearchRequest;
import com.cakk.api.dto.request.search.TopSearchedListRequest;
import com.cakk.api.dto.request.shop.CakeShopSearchByViewsRequest;
import com.cakk.api.dto.request.shop.CakeShopSearchRequest;
import com.cakk.api.dto.request.shop.SearchShopByLocationRequest;
import com.cakk.core.dto.param.search.CakeShopSearchByViewsParam;
import com.cakk.core.dto.param.search.HeartCakeSearchParam;
import com.cakk.core.dto.param.search.HeartCakeShopSearchParam;
import com.cakk.core.dto.param.search.SearchShopByLocationParam;
import com.cakk.core.dto.param.search.TopSearchedListParam;
import com.cakk.core.dto.response.search.TopSearchedListResponse;
import com.cakk.domain.mysql.dto.param.shop.CakeShopSearchParam;
import com.cakk.domain.mysql.entity.user.User;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SearchMapper {

	public static TopSearchedListResponse supplyTopSearchedListResponseBy(final List<String> keywordList) {
		return new TopSearchedListResponse(keywordList, keywordList.size());
	}

	public static TopSearchedListParam supplyTopSearchedListParamBy(
		final TopSearchedListRequest topSearchedListRequest) {
		return new TopSearchedListParam(topSearchedListRequest.count());
	}

	public static SearchShopByLocationParam supplySearchShopByLocationParamBy(final SearchShopByLocationRequest request) {
		return new SearchShopByLocationParam(request.latitude(), request.longitude(), request.distance());
	}

	public static CakeShopSearchParam supplyCakeShopSearchParamBy(final CakeShopSearchRequest request) {
		return new CakeShopSearchParam(
			request.cakeShopId(),
			request.keyword(),
			PointMapper.supplyPointBy(request.latitude(), request.longitude()),
			request.pageSize()
		);
	}

	public static CakeShopSearchByViewsParam supplyCakeShopSearchByViewsParam(
		final CakeShopSearchByViewsRequest request) {
		return new CakeShopSearchByViewsParam(request.offset(), request.pageSize());
	}

	public static HeartCakeShopSearchParam supplyHeartCakeShopSearchParamBy(
		final HeartCakeShopSearchRequest request,
		final User user) {
		return new HeartCakeShopSearchParam(request.cakeShopHeartId(), request.pageSize(), user);
	}

	public static HeartCakeSearchParam supplyHeartCakeSearchParamBy(
		final HeartCakeSearchRequest request,
		final User user) {
		return new HeartCakeSearchParam(request.cakeHeartId(), request.pageSize(), user);
	}
}
