package com.cakk.domain.aggregate.shop

import com.cakk.domain.aggregate.user.User
import com.cakk.domain.base.Domain

class CakeShopLike(
	val user: User,
) : Domain<CakeShopLike, Long>()
