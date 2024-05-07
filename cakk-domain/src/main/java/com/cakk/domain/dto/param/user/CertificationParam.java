package com.cakk.domain.dto.param.user;

import lombok.Builder;

import com.cakk.domain.entity.user.User;

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
