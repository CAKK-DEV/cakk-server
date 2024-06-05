package com.cakk.api.dto.response.shop;

import java.util.List;
import com.cakk.domain.mysql.dto.param.shop.CakeShopMapParam;


public record CakeShopByMapResponse(
	List<CakeShopMapParam> cakeShops
) {
}
