package com.cakk.api.dto.param.operation;


public record OwnerCandidateParam(
	Long userId,
	Long cakeShopId,
	String email,
	String businessRegistrationImageUrl,
	String idCardImageUrl,
	String emergencyContact
) {
}

