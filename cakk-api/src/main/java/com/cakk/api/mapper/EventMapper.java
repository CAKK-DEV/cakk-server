package com.cakk.api.mapper;

import java.util.Objects;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import com.cakk.api.dto.event.EmailWithVerificationCodeSendEvent;
import com.cakk.api.dto.event.IncreaseSearchCountEvent;
import com.cakk.domain.mysql.event.shop.CertificationEvent;
import com.cakk.external.vo.CertificationMessage;
import com.cakk.external.vo.VerificationMessage;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EventMapper {

	public static EmailWithVerificationCodeSendEvent supplyEmailWithVerificationCodeSendEventBy(final String email, final String code) {
		return new EmailWithVerificationCodeSendEvent(email, code);
	}

	public static IncreaseSearchCountEvent supplyIncreaseSearchCountEventBy(final String keyword) {
		return new IncreaseSearchCountEvent(keyword);
	}

	public static CertificationMessage supplyCertificationMessageBy(final CertificationEvent certificationEvent) {
		Double latitude = null;
		Double longitude = null;

		if (Objects.nonNull(certificationEvent.location())) {
			latitude = certificationEvent.location().getY();
			longitude = certificationEvent.location().getX();
		}

		return new CertificationMessage(
			certificationEvent.businessRegistrationImageUrl(),
			certificationEvent.idCardImageUrl(),
			certificationEvent.emergencyContact(),
			certificationEvent.message(),
			certificationEvent.userId(),
			certificationEvent.userEmail(),
			certificationEvent.shopName(),
			latitude,
			longitude
		);
	}

	public static VerificationMessage supplyVerificationMessageBy(final EmailWithVerificationCodeSendEvent event) {
		return new VerificationMessage(event.email(), event.code());
	}
}
