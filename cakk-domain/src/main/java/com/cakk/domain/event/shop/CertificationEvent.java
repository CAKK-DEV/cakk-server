package com.cakk.domain.event.shop;

import lombok.Builder;

@Builder
public record CertificationEvent(
	String businessRegistrationImageUrl,
	String idCardImageUrl,
	String emergencyContact,
	String message,
	Long userId,
	String userEmail,
	String shopName,
	Double shopLatitude,
	Double shopLongitude
) {
}
