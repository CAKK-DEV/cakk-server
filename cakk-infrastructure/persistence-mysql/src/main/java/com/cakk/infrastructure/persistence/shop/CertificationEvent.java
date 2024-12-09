package com.cakk.infrastructure.persistence.shop;

import org.locationtech.jts.geom.Point;

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
	Point location
) {
}
