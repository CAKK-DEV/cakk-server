package com.cakk.api.dto.request.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;


public record CertificationRequest(
	@NotBlank
	String businessRegistrationImageUrl,
	@NotBlank
	String idCardImageUrl,
	@NotNull
	Long cakeShopId,
	@NotBlank
	String emergencyContact,
	String message
) {
}
