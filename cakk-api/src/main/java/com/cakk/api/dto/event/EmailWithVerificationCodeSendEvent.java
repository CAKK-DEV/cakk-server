package com.cakk.api.dto.event;

public record EmailWithVerificationCodeSendEvent(
	String email,
	String code
) {
}
