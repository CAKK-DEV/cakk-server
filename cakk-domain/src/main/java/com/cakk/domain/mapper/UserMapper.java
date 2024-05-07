package com.cakk.domain.mapper;

import com.cakk.domain.dto.param.user.CertificationParam;
import com.cakk.domain.entity.shop.CakeShop;
import com.cakk.domain.event.user.CertificationEvent;

public class UserMapper {

	public static CertificationEvent supplyCertificationInfoWithCakeShopInfo(CertificationParam param, CakeShop cakeShop) {
		return CertificationEvent.builder()
			.idCardImageUrl(param.idCardImageUrl())
			.businessRegistrationImageUrl(param.businessRegistrationImageUrl())
			.emergencyContact(param.emergencyContact())
			.message(param.message())
			.userEmail(param.user().getEmail())
			.shopName(cakeShop.getShopName())
			.shopLatitude(cakeShop.getLatitude())
			.shopLongitude(cakeShop.getLongitude())
			.build();
	}

	public static CertificationEvent supplyCertificationInfo(CertificationParam param) {
		return CertificationEvent.builder()
			.idCardImageUrl(param.idCardImageUrl())
			.businessRegistrationImageUrl(param.businessRegistrationImageUrl())
			.emergencyContact(param.emergencyContact())
			.message(param.message())
			.userEmail(param.user().getEmail())
			.build();
	}
}
