package com.cakk.api.dto.request.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import com.cakk.domain.mysql.dto.param.user.CertificationParam;
import com.cakk.domain.mysql.entity.user.User;

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
