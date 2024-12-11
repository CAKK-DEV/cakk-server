package com.cakk.domain.aggregate.shop

import com.cakk.domain.base.Domain

class Cake(
	cakeImageUrl: String,
	heartCount: Int = 0,
	val categories: Set<CakeCategory>,
	val tags: Set<CakeTag>
) : Domain<Cake, Long>() {

	var cakeImageUrl: String = cakeImageUrl
		private set

	var heartCount: Int = heartCount
		private set
}
