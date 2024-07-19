package com.cakk.external.vo;

public record CertificationMessage(
	String businessRegistrationImageUrl,
	String idCardImageUrl,
	String emergencyContact,
	String message,
	Long userId,
	String userEmail,
	String shopName,
	Double latitude,
	Double longitude
) {
}
