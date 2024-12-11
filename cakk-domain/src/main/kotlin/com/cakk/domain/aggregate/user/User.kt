package com.cakk.domain.aggregate.user

import java.time.LocalDate
import com.cakk.common.enums.Gender
import com.cakk.common.enums.Role
import com.cakk.domain.aggregate.heart.CakeHeart
import com.cakk.domain.aggregate.heart.CakeShopHeart
import com.cakk.domain.base.AggregateRoot
import com.cakk.domain.generic.Device
import com.cakk.domain.generic.Provider

class User(
	id: Long?,
	val provider: Provider,
	nickname: String,
	profileImageUrl: String,
	email: String,
	gender: Gender,
	birthday: LocalDate,
	device: Device,
	val role: Role,
	val cakeShopHearts: Set<CakeShopHeart> = emptySet(),
	val cakeHearts: Set<CakeHeart> = emptySet()
) : AggregateRoot<User, Long>() {

	var nickname: String = nickname
		private set

	var profileImageUrl: String = profileImageUrl
		private set

	var email: String = email
		private set

	var gender: Gender = gender
		private set

	var birthday: LocalDate = birthday
		private set

	var device: Device = device
		private set
}
