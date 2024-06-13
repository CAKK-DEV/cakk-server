package com.cakk.api.dto.response.shop;

import java.util.List;

import com.cakk.domain.mysql.dto.param.shop.CakeShopParam;


public record CakeShopByMapResponse(
	List<CakeShopParam> cakeShops
) {
}

