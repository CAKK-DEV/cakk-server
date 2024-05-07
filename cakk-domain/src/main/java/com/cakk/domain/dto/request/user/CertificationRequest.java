package com.cakk.domain.dto.request.user;

import com.cakk.domain.dto.param.user.CertificationParam;
import com.cakk.domain.entity.user.User;
import jakarta.validation.constraints.NotBlank;

public record CertificationRequest(
	@NotBlank
	String businessRegistrationImageUrl,
	@NotBlank
	String idCardImageUrl,
	Long cakeShopId,
	@NotBlank
	String emergencyContact,
	String message
) {

	public CertificationParam from(User user) {
		return CertificationParam.builder()
			.businessRegistrationImageUrl(businessRegistrationImageUrl)
			.idCardImageUrl(idCardImageUrl)
			.cakeShopId(cakeShopId)
			.emergencyContact(emergencyContact)
			.message(message)
			.user(user)
			.build();
	}
}
