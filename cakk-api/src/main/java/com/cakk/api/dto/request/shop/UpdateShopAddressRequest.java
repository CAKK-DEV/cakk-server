package com.cakk.api.dto.request.shop;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import com.cakk.api.mapper.PointMapper;
import com.cakk.domain.mysql.dto.param.shop.UpdateShopAddressParam;
import com.cakk.domain.mysql.entity.user.User;

public record UpdateShopAddressRequest(
	@NotBlank
	String shopAddress,
	@NotNull @Min(-90) @Max(90)
	Double latitude,
	@NotNull @Min(-180) @Max(180)
	Double longitude
) {

	public UpdateShopAddressParam toParam(User user, Long cakeShopId) {
		return new UpdateShopAddressParam(
			shopAddress,
			PointMapper.supplyPointBy(latitude, longitude),
			user,
			cakeShopId
		);
	}

}
