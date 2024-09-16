package com.cakk.domain.mysql.dto.param.like;

import java.util.Set;

import com.cakk.common.enums.Days;

public record HeartCakeShopResponseParam(
	Long cakeShopHeartId,
	Long cakeShopId,
	String thumbnailUrl,
	String cakeShopName,
	String cakeShopBio,
	Set<String> cakeImageUrls,
	Set<Days> operationDays
) {
}
