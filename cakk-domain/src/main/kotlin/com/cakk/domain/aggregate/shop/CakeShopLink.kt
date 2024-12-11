package com.cakk.domain.aggregate.shop

import com.cakk.common.enums.LinkKind
import com.cakk.domain.base.ValueObject

class CakeShopLink(
	linkKind: LinkKind,
	linkPath: String
) : ValueObject<CakeShopLink>() {

	var linkKind: LinkKind = linkKind
		private set

	var linkPath: String = linkPath
		private set
}
