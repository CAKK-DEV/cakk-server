package com.cakk.api.vo;

import lombok.Builder;

@Builder
public record JsonWebToken(
	String accessToken,
	String refreshToken,
	String grantType
) {
}
