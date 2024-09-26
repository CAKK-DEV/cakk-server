package com.cakk.core.vo

data class JsonWebToken(
	val accessToken: String,
	val refreshToken: String,
	val grantType: String
)
