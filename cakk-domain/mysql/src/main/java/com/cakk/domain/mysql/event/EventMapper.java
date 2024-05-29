package com.cakk.domain.mysql.event;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import com.cakk.domain.mysql.dto.param.user.CertificationParam;
import com.cakk.domain.mysql.entity.shop.CakeShop;
import com.cakk.domain.mysql.event.shop.CertificationEvent;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EventMapper {
	public static CertificationEvent supplyCertificationInfoWithCakeShopInfo(CertificationParam param, CakeShop cakeShop) {
		return CertificationEvent.builder()
			.idCardImageUrl(param.idCardImageUrl())
			.businessRegistrationImageUrl(param.businessRegistrationImageUrl())
			.emergencyContact(param.emergencyContact())
			.message(param.message())
			.userId(param.user().getId())
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
			.userId(param.user().getId())
			.userEmail(param.user().getEmail())
			.build();
	}
}
