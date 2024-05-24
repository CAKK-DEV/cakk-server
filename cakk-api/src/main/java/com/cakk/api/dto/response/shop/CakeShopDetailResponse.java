package com.cakk.api.dto.response.shop;

import java.util.Set;

import lombok.Builder;

import com.cakk.common.enums.Days;
import com.cakk.domain.dto.param.shop.CakeShopDetailParam;
import com.cakk.domain.dto.param.shop.CakeShopLinkParam;

@Builder
public record CakeShopDetailResponse(
	Long cakeShopId,
	String cakeShopName,
	String thumbnailUrl,
	String cakeShopBio,
	String cakeShopDescription,
	Set<Days> operationDays,
	Set<CakeShopLinkParam> links
) {

	public static CakeShopDetailResponse from(CakeShopDetailParam param) {
		return CakeShopDetailResponse.builder()
			.cakeShopId(param.cakeShopId())
			.cakeShopName(param.shopName())
			.thumbnailUrl(param.thumbnailUrl())
			.cakeShopBio(param.shopBio())
			.cakeShopDescription(param.shopDescription())
			.operationDays(param.operationDays())
			.links(param.links())
			.build();
	}
}
