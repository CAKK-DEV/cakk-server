package com.cakk.domain.mysql.dto.param.link

import com.cakk.domain.mysql.entity.shop.CakeShopLink
import com.cakk.domain.mysql.entity.user.User

data class UpdateLinkParam(
	val user: User,
	val cakeShopId: Long,
	val cakeShopLinks: List<CakeShopLink>
)
