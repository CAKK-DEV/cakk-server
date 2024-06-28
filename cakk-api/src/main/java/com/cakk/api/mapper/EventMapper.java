package com.cakk.api.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import com.cakk.api.dto.event.EmailWithVerificationCodeSendEvent;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EventMapper {

	public static EmailWithVerificationCodeSendEvent supplyEmailWithVerificationCodeSendEventBy(String email, String code) {
		return new EmailWithVerificationCodeSendEvent(email, code);
	}
}
