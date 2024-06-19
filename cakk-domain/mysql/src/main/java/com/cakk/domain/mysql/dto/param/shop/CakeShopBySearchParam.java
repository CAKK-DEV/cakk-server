package com.cakk.domain.mysql.dto.param.shop;

import java.util.Set;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@SuperBuilder
public class CakeShopBySearchParam extends CakeShopParam {

	private Set<CakeShopOperationParam> operationDays;

	public CakeShopBySearchParam(Long cakeShopId, String thumbnailUrl, String cakeShopName, String cakeShopBio,
		Set<String> cakeImageUrls, Set<CakeShopOperationParam> operationDays) {
		super(cakeShopId, thumbnailUrl, cakeShopName, cakeShopBio, cakeImageUrls);
		this.operationDays = operationDays;
	}
}
