package com.cakk.domain.aggregate.shop

import java.time.LocalDateTime
import com.cakk.domain.base.AggregateRoot
import com.cakk.domain.generic.Location

class CakeShop(
	private val id: Long?,
	thumbnailUrl: String?,
	shopName: String,
	shopAddress: String?,
	shopBio: String?,
	shopDescription: String?,
	val location: Location,
	likeCount: Int = 0,
	heartCount: Int = 0,
	deletedAt: LocalDateTime?,
	val links: Set<CakeShopLink> = emptySet(),
	val operations: Set<CakeShopOperation> = emptySet(),
	val likes: Set<CakeShopLike> = emptySet(),
	val cakes: Set<Cake> = emptySet(),
) : AggregateRoot<CakeShop, Long>() {

	var thumbnailUrl: String? = thumbnailUrl
		private set

	var shopName: String = shopName
		private set

	var shopAddress: String? = shopAddress
		private set

	var shopBio: String? = shopBio
		private set

	var shopDescription: String? = shopDescription
		private set

	var likeCount: Int = likeCount
		private set

	var heartCount: Int = heartCount
		private set

	var deletedAt: LocalDateTime? = deletedAt
		private set
}
