package com.cakk.api.dto.request.cake;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import com.cakk.api.mapper.PointMapper;
import com.cakk.domain.mysql.dto.param.cake.CakeSearchParam;

public record CakeSearchByLocationRequest(
	Long cakeId,
	String keyword,
	@NotNull @Min(-90) @Max(90)
	Double latitude,
	@NotNull @Min(-180) @Max(180)
	Double longitude,
	Integer pageSize
) {

	public CakeSearchParam toParam() {
		return new CakeSearchParam(
			cakeId,
			keyword,
			PointMapper.supplyPointBy(latitude, longitude),
			pageSize == null ? 10 : pageSize
		);
	}
}
