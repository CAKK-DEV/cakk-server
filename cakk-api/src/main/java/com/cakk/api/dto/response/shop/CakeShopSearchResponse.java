package com.cakk.api.dto.response.shop;

import java.util.List;

import com.cakk.domain.mysql.dto.param.shop.CakeShopParam;

import lombok.Builder;

@Builder
public record CakeShopSearchResponse(
	List<CakeShopParam> cakeShops,
	Long lastCakeShopId,
	int size
) {
}
