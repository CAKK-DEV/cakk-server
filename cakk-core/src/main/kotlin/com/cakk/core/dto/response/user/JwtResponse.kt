package com.cakk.core.dto.response.user

data class JwtResponse(
	val accessToken: String,
	val refreshToken: String,
	val grantType: String
)
