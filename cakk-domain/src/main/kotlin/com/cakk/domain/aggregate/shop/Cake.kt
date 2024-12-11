package com.cakk.domain.aggregate.shop

import com.cakk.domain.base.AggregateRoot

class Cake(
	val shopId: Long,
	cakeImageUrl: String,
	heartCount: Int = 0,
	val categories: Set<CakeCategory>,
	val tags: Set<CakeTag>
) : AggregateRoot<Cake, Long>() {

	var cakeImageUrl: String = cakeImageUrl
		private set

	var heartCount: Int = heartCount
		private set
}
