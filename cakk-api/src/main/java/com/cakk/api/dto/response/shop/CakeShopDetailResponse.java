package com.cakk.api.dto.response.shop;

import java.util.Set;

import lombok.Builder;

import com.cakk.common.enums.Days;
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
}
