package com.cakk.api.dto.response.shop;

import java.util.List;

import com.cakk.domain.mysql.dto.param.shop.CakeShopByLocationParam;


public record CakeShopByMapResponse(
	List<CakeShopByLocationParam> cakeShops
) {
}

