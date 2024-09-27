package com.cakk.core.dto.param.user

import com.cakk.common.enums.Provider

data class UserSignInParam(
	val provider: Provider,
	val idToken: String
)

