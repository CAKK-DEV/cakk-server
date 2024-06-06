package com.cakk.api.dto.request.cake;

import com.cakk.api.mapper.PointMapper;
import com.cakk.domain.mysql.dto.param.cake.CakeSearchParam;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

public record CakeImageSearchRequest(
	Long cursorId,
	String searchText,
	@Min(-90) @Max(90)
	Double latitude,
	@Min(-180) @Max(180)
	Double longitude,
	Integer pageSize
) {

	public CakeSearchParam ToParam() {
		return new CakeSearchParam(
			cursorId == null ? 0 : cursorId,
			searchText,
			PointMapper.supplyPointBy(latitude, longitude),
			pageSize
		);
	}
}
