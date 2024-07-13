package com.cakk.api.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import com.cakk.api.dto.event.EmailWithVerificationCodeSendEvent;
import com.cakk.api.dto.event.IncreaseSearchCountEvent;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EventMapper {

	public static EmailWithVerificationCodeSendEvent supplyEmailWithVerificationCodeSendEventBy(final String email, final String code) {
		return new EmailWithVerificationCodeSendEvent(email, code);
	}

	public static IncreaseSearchCountEvent supplyIncreaseSearchCountEventBy(final String keyword) {
		return new IncreaseSearchCountEvent(keyword);
	}
}
