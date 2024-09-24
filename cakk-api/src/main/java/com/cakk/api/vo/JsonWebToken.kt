package com.cakk.api.vo

data class JsonWebToken(
	val accessToken: String,
	val refreshToken: String,
	val grantType: String
)
