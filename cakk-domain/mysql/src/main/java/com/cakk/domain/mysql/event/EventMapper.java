package com.cakk.domain.mysql.event;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import com.cakk.domain.mysql.dto.param.user.CertificationParam;
import com.cakk.domain.mysql.entity.shop.CakeShop;
import com.cakk.domain.mysql.event.shop.CertificationEvent;
import com.cakk.domain.mysql.event.views.CakeIncreaseViewsEvent;

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
			.location(cakeShop.getLocation())
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

	public static CakeIncreaseViewsEvent supplyCakeIncreaseViewsEvent(Long cakeId) {
		return new CakeIncreaseViewsEvent(cakeId);
	}
}
