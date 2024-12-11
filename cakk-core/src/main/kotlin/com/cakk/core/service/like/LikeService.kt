package com.cakk.core.service.like

import org.springframework.stereotype.Service

import com.cakk.core.annotation.DistributedLock
import com.cakk.core.facade.cake.CakeShopReadFacade
import com.cakk.core.facade.user.UserLikeFacade
import com.cakk.infrastructure.persistence.entity.user.UserEntity

@Service
class LikeService(
	private val cakeShopReadFacade: CakeShopReadFacade,
	private val userLikeFacade: UserLikeFacade
) {

	@DistributedLock(key = "#cakeShopId")
	fun likeCakeShop(userEntity: UserEntity, cakeShopId: Long) {
		val cakeShop = cakeShopReadFacade.findByIdWithLike(cakeShopId)

		userLikeFacade.likeCakeShop(userEntity, cakeShop)
	}
}
