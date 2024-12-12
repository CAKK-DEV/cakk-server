package com.cakk.domain.aggregate.shop

import com.cakk.common.enums.CakeDesignCategory
import com.cakk.domain.base.ValueObject

class CakeCategory(
	val designCategory: CakeDesignCategory
) : ValueObject<CakeCategory>()
