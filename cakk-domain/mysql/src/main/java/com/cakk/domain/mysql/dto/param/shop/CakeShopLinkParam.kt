package com.cakk.domain.mysql.dto.param.shop

import com.cakk.common.enums.LinkKind

data class CakeShopLinkParam(
	val linkKind: LinkKind,
	val linkPath: String
)
