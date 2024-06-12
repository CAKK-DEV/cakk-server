package com.cakk.api.dto.response.shop;

import java.util.List;

import lombok.Builder;

import com.cakk.domain.mysql.dto.param.shop.CakeShopParam;

@Builder
public record CakeShopSearchResponse(
	List<CakeShopParam> cakeShops,
	Long lastCakeShopId,
	int size
) {
}
