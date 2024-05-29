package com.cakk.domain.mysql.dto.param.shop;

public record CakeShopSimpleParam(
	Long cakeShopId,
	String thumbnailUrl,
	String cakeShopName,
	String cakeShopBio
) {
}
