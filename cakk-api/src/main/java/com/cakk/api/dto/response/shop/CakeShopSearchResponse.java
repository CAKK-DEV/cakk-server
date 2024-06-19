package com.cakk.api.dto.response.shop;

import java.util.List;

import lombok.Builder;

import com.cakk.domain.mysql.dto.param.shop.CakeShopBySearchParam;

@Builder
public record CakeShopSearchResponse(
	List<CakeShopBySearchParam> cakeShops,
	Long lastCakeShopId,
	int size
) {
}
