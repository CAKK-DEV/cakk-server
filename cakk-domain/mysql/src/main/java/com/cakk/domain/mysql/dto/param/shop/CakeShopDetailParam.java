package com.cakk.domain.mysql.dto.param.shop;

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
