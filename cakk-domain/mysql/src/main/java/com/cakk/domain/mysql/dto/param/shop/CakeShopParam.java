package com.cakk.domain.mysql.dto.param.shop;

import java.util.List;
import java.util.Set;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class CakeShopParam {

	private Long cakeShopId;
	private String thumbnailUrl;
	private String cakeShopName;
	private String cakeShopBio;
	private Set<String> cakeImageUrls;
	private List<CakeShopOperationParam> operationDays;

	public void addCakeImageUrl(String cakeImageUrl) {
		cakeImageUrls.add(cakeImageUrl);
	}

	public void setOperationDay(ShopOperationParam operationDay) {
		operationDays.add(
			new CakeShopOperationParam(
				operationDay.operationDay(),
				operationDay.operationStartTime(),
				operationDay.operationEndTime()
			)
		);
	}
}

