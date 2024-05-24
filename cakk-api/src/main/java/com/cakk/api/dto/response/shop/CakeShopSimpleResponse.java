package com.cakk.api.dto.response.shop;

import lombok.Builder;

import com.cakk.domain.dto.param.shop.CakeShopSimpleParam;

@Builder
public record CakeShopSimpleResponse(
	Long cakeShopId,
	String thumbnailUrl,
	String cakeShopName,
	String cakeShopBio
) {

	public static CakeShopSimpleResponse from(CakeShopSimpleParam param) {
		return CakeShopSimpleResponse.builder()
			.cakeShopId(param.cakeShopId())
			.thumbnailUrl(param.thumbnailUrl())
			.cakeShopName(param.cakeShopName())
			.cakeShopBio(param.cakeShopBio())
			.build();
	}
}
