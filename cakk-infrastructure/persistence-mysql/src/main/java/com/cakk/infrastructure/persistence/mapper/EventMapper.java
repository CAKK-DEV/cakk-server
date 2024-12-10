package com.cakk.infrastructure.persistence.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import com.cakk.infrastructure.persistence.entity.shop.CakeShopEntity;
import com.cakk.infrastructure.persistence.param.user.CertificationParam;
import com.cakk.infrastructure.persistence.shop.CertificationEvent;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EventMapper {
	public static CertificationEvent supplyCertificationInfoWithCakeShopInfo(CertificationParam param, CakeShopEntity cakeShop) {
		return CertificationEvent.builder()
			.idCardImageUrl(param.idCardImageUrl())
			.businessRegistrationImageUrl(param.businessRegistrationImageUrl())
			.emergencyContact(param.emergencyContact())
			.message(param.message())
			.userId(param.user().getId())
			.userEmail(param.user().getEmail())
			.shopName(cakeShop.getShopName())
			.location(cakeShop.getLocation())
			.build();
	}
}
