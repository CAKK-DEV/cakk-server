package com.cakk.domain.mysql.dto.param.user;

import lombok.Builder;

import com.cakk.domain.mysql.entity.user.User;

@Builder
public record CertificationParam(
	String businessRegistrationImageUrl,
	String idCardImageUrl,
	Long cakeShopId,
	String emergencyContact,
	String message,
	User user
) {
}
