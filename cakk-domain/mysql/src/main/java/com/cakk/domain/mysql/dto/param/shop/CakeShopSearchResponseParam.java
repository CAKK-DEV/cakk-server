package com.cakk.domain.mysql.dto.param.shop;

import java.util.List;
import java.util.Set;

public class CakeShopSearchResponseParam {

	private Long cakeShopId;
	private String thumbnailUrl;
	private String cakeShopName;
	private String cakeShopBio;
	private List<CakeShopOperationParam> operationDays;
	private Set<String> cakeImageUrls;
}
