package com.cakk.core.dto.param.user

import com.cakk.common.enums.Gender
import com.cakk.common.enums.Provider
import java.time.LocalDate

data class UserSignUpParam(
	val provider: Provider,
	val idToken: String,
	val deviceOs: String?,
	val deviceToken: String?,
	val nickname: String,
	val email: String,
	val birthday: LocalDate,
	val gender: Gender
)

