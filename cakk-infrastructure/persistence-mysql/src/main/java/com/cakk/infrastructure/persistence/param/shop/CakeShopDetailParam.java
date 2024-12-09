package com.cakk.infrastructure.persistence.param.shop;

import java.util.Set;

import com.cakk.common.enums.Days;

public record CakeShopDetailParam(
	Long cakeShopId,
	String shopName,
	String thumbnailUrl,
	String shopBio,
	String shopDescription,
	Set<Days> operationDays,
	Set<CakeShopLinkParam> links
) {
}
