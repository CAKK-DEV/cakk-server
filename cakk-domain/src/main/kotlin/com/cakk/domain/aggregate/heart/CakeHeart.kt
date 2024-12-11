package com.cakk.domain.aggregate.heart

import com.cakk.domain.aggregate.shop.Cake
import com.cakk.domain.aggregate.user.User
import com.cakk.domain.base.AggregateRoot

class CakeHeart(
	val user: User,
	val cake: Cake
) : AggregateRoot<CakeHeart, Long>()
