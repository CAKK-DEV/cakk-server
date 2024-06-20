package com.cakk.api.dto.response.shop;

import java.util.List;

import lombok.Builder;

import com.cakk.domain.mysql.dto.param.shop.CakeShopSearchResponseParam;

@Builder
public record CakeShopSearchResponse(
	List<CakeShopSearchResponseParam> cakeShops,
	Long lastCakeShopId,
	int size
) {
}
