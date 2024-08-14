package com.cakk.api.dto.response.shop;

public record CakeShopOwnerCandidateResponse(
	Long userId,
	Long cakeShopId,
	String email,
	String businessRegistrationImageUrl,
	String idCardImageUrl,
	String emergencyContact
) {
}
