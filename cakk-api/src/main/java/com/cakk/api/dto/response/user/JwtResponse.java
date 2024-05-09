package com.cakk.api.dto.response.user;

import com.cakk.api.vo.JsonWebToken;

public record JwtResponse(
	String accessToken,
	String refreshToken,
	String grantType
) {

	public static JwtResponse from(JsonWebToken jwt) {
		return new JwtResponse(jwt.accessToken(), jwt.refreshToken(), jwt.grantType());
	}
}
