package com.cakk.api.dto.response.shop;

import lombok.Builder;

@Builder
public record CakeShopSimpleResponse(
	Long cakeShopId,
	String thumbnailUrl,
	String cakeShopName,
	String cakeShopBio
) {
}
