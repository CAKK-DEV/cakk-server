package com.cakk.domain.event.user;

import lombok.Builder;

@Builder
public record CertificationEvent(
	String businessRegistrationImageUrl,
	String idCardImageUrl,
	String emergencyContact,
	String message,
	String userEmail,
	String shopName,
	Double shopLatitude,
	Double shopLongitude
) {
}
