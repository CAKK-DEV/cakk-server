package com.cakk.domain.aggregate.heart

import com.cakk.domain.aggregate.shop.CakeShop
import com.cakk.domain.aggregate.user.User
import com.cakk.domain.base.AggregateRoot

class CakeShopHeart(
	val user: User,
	val shop: CakeShop,
) : AggregateRoot<CakeShopHeart, Long>()
