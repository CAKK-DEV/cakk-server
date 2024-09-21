package com.cakk.core.dto.param.user

data class VerifyEmailParam(
	val email: String,
	val code: String
)
