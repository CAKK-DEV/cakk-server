package com.cakk.core.facade.cake

import com.cakk.core.annotation.DomainFacade
import com.cakk.infrastructure.persistence.param.like.HeartCakeImageResponseParam
import com.cakk.infrastructure.persistence.param.like.HeartCakeShopResponseParam
import com.cakk.infrastructure.persistence.repository.query.CakeHeartQueryRepository
import com.cakk.infrastructure.persistence.repository.query.CakeShopHeartQueryRepository

@DomainFacade
class CakeShopUserReadFacade(
	private val cakeShopHeartQueryRepository: com.cakk.infrastructure.persistence.repository.query.CakeShopHeartQueryRepository,
	private val cakeHeartQueryRepository: com.cakk.infrastructure.persistence.repository.query.CakeHeartQueryRepository
) {

    fun searchAllCakeShopsByCursorAndHeart(
            cakeShopHeartId: Long?,
            userId: Long?,
            pageSize: Int
    ): List<com.cakk.infrastructure.persistence.param.like.HeartCakeShopResponseParam> {
        val cakeShopHeartIds: List<Long> = cakeShopHeartQueryRepository.searchIdsByCursorAndHeart(cakeShopHeartId, userId, pageSize)
        return cakeShopHeartQueryRepository.searchAllByCursorAndHeart(cakeShopHeartIds)
    }

	fun searchCakeImagesByCursorAndHeart(
		cakeHeartId: Long?,
		userId: Long?,
		pageSize: Int
	): List<com.cakk.infrastructure.persistence.param.like.HeartCakeImageResponseParam> {
		return cakeHeartQueryRepository.searchCakeImagesByCursorAndHeart(cakeHeartId, userId, pageSize)
	}
}

