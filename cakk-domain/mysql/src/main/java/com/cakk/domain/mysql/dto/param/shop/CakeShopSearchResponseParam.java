package com.cakk.domain.mysql.dto.param.shop;

import java.util.Set;

public record CakeShopSearchResponseParam(
	Long cakeShopId,
	String thumbnailUrl,
	String cakeShopName,
	String cakeShopBio,
	Set<String> cakeImageUrls,
	Set<CakeShopOperationParam> operationDays
) {

}
