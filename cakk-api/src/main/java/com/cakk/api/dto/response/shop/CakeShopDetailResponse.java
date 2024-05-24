package com.cakk.api.dto.response.shop;

import java.util.List;

import lombok.Builder;

import com.cakk.common.enums.Days;
import com.cakk.domain.dto.param.shop.CakeShopDetailParam;
import com.cakk.domain.dto.param.shop.CakeShopLinkParam;

@Builder
public record CakeShopDetailResponse(
	Long cakeShopId,
	String shopName,
	String thumbnailUrl,
	String shopBio,
	String shopDescription,
	List<Days> operationDay,
	List<CakeShopLinkParam> links
) {

	public static CakeShopDetailResponse from(CakeShopDetailParam param) {
		return CakeShopDetailResponse.builder()
			.cakeShopId(param.cakeShopId())
			.shopName(param.shopName())
			.thumbnailUrl(param.thumbnailUrl())
			.shopBio(param.shopBio())
			.shopDescription(param.shopDescription())
			.operationDay(param.operationDay())
			.links(param.links())
			.build();
	}
}
