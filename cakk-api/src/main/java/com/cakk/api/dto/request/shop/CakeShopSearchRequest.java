package com.cakk.api.dto.request.shop;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

import com.cakk.api.mapper.PointMapper;
import com.cakk.domain.mysql.dto.param.shop.CakeShopSearchParam;

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
			cakeShopId,
			keyword,
			PointMapper.supplyPointBy(latitude, longitude),
			pageSize
		);
	}
}
