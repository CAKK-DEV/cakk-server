package com.cakk.domain.mysql.dto.param.shop;

import java.util.Set;

public record CakeShopLocationResponseParam(
	Long cakeShopId,
	String thumbnailUrl,
	String cakeShopName,
	String cakeShopBio,
	Set<String> cakeImageUrls,
	Double longitude,
	Double latitude
) {
}
