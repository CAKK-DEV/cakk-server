package com.cakk.api.dto.request.shop;

import com.cakk.api.mapper.PointMapper;
import com.cakk.domain.mysql.dto.param.shop.CakeShopSearchParam;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

public record CakeShopSearchRequest(
	Long cakeShopId,
	String keyword,
	@Min(-90) @Max(90)
	Double latitude,
	@Min(-180) @Max(180)
	Double longitude,
	Integer pageSize
) {

	public CakeShopSearchParam toParam() {
		return new CakeShopSearchParam(
			cakeShopId == null ? 0 : cakeShopId,
			keyword,
			PointMapper.supplyPointBy(latitude, longitude),
			pageSize
		);
	}
}
