package com.cakk.api.mapper;

import java.util.Arrays;
import java.util.Objects;

import jakarta.servlet.http.HttpServletRequest;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import com.cakk.api.dto.event.EmailWithVerificationCodeSendEvent;
import com.cakk.api.dto.event.ErrorAlertEvent;
import com.cakk.api.dto.event.IncreaseSearchCountEvent;
import com.cakk.domain.mysql.event.shop.CertificationEvent;
import com.cakk.external.vo.message.CertificationMessage;
import com.cakk.external.vo.message.ErrorAlertMessage;
import com.cakk.external.vo.message.VerificationMessage;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EventMapper {

	public static EmailWithVerificationCodeSendEvent supplyEmailWithVerificationCodeSendEventBy(final String email, final String code) {
		return new EmailWithVerificationCodeSendEvent(email, code);
	}

	public static IncreaseSearchCountEvent supplyIncreaseSearchCountEventBy(final String keyword) {
		return new IncreaseSearchCountEvent(keyword);
	}

	public static ErrorAlertEvent supplyErrorAlertEventBy(
		final Exception exception,
		final HttpServletRequest request,
		final String profile
	) {
		return new ErrorAlertEvent(exception, request, profile);
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
		return new VerificationMessage(event.getEmail(), event.getCode());
	}

	public static ErrorAlertMessage supplyErrorAlertMessageBy(final ErrorAlertEvent event) {
		final String profile = event.getProfile();
		final String stackTrace = Arrays.toString(event.getException().getStackTrace());
		final HttpServletRequest request = event.getRequest();

		return new ErrorAlertMessage(
			profile,
			stackTrace,
			request.getContextPath(),
			request.getRequestURL().toString(),
			request.getMethod(),
			request.getParameterMap(),
			request.getRemoteAddr(),
			request.getHeader("User-Agent")
		);
	}
}
