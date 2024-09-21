package com.cakk.api.mapper;

import jakarta.servlet.http.HttpServletRequest;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import com.cakk.core.dto.event.CakeIncreaseViewsEvent;
import com.cakk.core.dto.event.CakeShopIncreaseViewsEvent;
import com.cakk.core.dto.event.EmailWithVerificationCodeSendEvent;
import com.cakk.core.dto.event.ErrorAlertEvent;
import com.cakk.core.dto.event.IncreaseSearchCountEvent;

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

	public static CakeIncreaseViewsEvent supplyCakeIncreaseViewsEventBy(Long cakeId) {
		return new CakeIncreaseViewsEvent(cakeId);
	}

	public static CakeShopIncreaseViewsEvent supplyCakeShopIncreaseViewsEventBy(Long cakeShopId) {
		return new CakeShopIncreaseViewsEvent(cakeShopId);
	}
}
