package com.cakk.domain.dto.param.shop;

import java.util.List;

import com.cakk.common.enums.Days;

public record CakeShopDetailParam(
	Long cakeShopId,
	String shopName,
	String thumbnailUrl,
	String shopBio,
	String shopDescription,
	List<Days> operationDay,
	List<CakeShopLinkParam> links
) {
}
