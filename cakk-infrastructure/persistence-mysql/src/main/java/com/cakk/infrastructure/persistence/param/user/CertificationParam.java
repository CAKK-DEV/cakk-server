package com.cakk.infrastructure.persistence.param.user;

import lombok.Builder;

import com.cakk.infrastructure.persistence.entity.user.User;

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
