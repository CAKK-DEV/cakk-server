package com.cakk.domain.dto.param.user;

import com.cakk.domain.entity.user.User;
import lombok.Builder;

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
