package com.cakk.core.dto.param.shop

import com.cakk.common.enums.LinkKind

data class ShopLinkParam(
	val linkKind: LinkKind,
	val linkPath: String
)

