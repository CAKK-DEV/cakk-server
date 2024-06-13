package com.cakk.domain.mysql.dto.param.shop;

public record CakeShopByKeywordParam(
	Long cakeShopId,
	String thumbnailUrl,
	String cakeShopName,
	String cakeShopBio
) {
}
