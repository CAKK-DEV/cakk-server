package com.cakk.domain.aggregate.user

import java.time.LocalDate
import java.time.LocalDateTime
import com.cakk.common.enums.Gender
import com.cakk.common.enums.Role
import com.cakk.domain.base.AggregateRoot

data class UserWithdrawal(
	private val id: Long?,
	val email: String,
	val gender: Gender,
	val birthday: LocalDate,
	val role: Role,
	val withdrawalDate: LocalDateTime
) : AggregateRoot<UserWithdrawal, Long>()
