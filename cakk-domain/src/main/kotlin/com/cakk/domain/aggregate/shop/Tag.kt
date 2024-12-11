package com.cakk.domain.aggregate.shop

import com.cakk.domain.base.AggregateRoot

class Tag(
	val tagName: String
) : AggregateRoot<Tag, Long>()
