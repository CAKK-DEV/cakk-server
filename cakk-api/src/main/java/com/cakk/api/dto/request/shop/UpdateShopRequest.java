package com.cakk.api.dto.request.shop;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import com.cakk.domain.mysql.dto.param.shop.CakeShopUpdateParam;
import com.cakk.domain.mysql.entity.user.User;

public record UpdateShopRequest(
	@NotBlank @Size(max = 30)
	String shopName,
	@Size(max = 40)
	String shopBio,
	@Size(max = 500)
	String shopDescription
) {

	public CakeShopUpdateParam toParam(User user, Long cakeShopId) {
		return new CakeShopUpdateParam(
			shopName,
			shopBio,
			shopDescription,
			user,
			cakeShopId
		);
	}
}
