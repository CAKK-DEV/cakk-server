package com.cakk.core.facade.cake

import com.cakk.core.annotation.DomainFacade
import com.cakk.domain.mysql.dto.param.like.HeartCakeImageResponseParam
import com.cakk.domain.mysql.dto.param.like.HeartCakeShopResponseParam
import com.cakk.domain.mysql.repository.query.CakeHeartQueryRepository
import com.cakk.domain.mysql.repository.query.CakeShopHeartQueryRepository


@DomainFacade
class CakeShopUserReadFacade(
	private val cakeShopHeartQueryRepository: CakeShopHeartQueryRepository,
	private val cakeHeartQueryRepository: CakeHeartQueryRepository
) {
    fun searchAllCakeShopsByCursorAndHeart(
            cakeShopHeartId: Long?,
            userId: Long?,
            pageSize: Int
    ): List<HeartCakeShopResponseParam> {
        val cakeShopHeartIds: List<Long> = cakeShopHeartQueryRepository.searchIdsByCursorAndHeart(cakeShopHeartId, userId, pageSize)
        return cakeShopHeartQueryRepository.searchAllByCursorAndHeart(cakeShopHeartIds)
    }

	fun searchCakeImagesByCursorAndHeart(
		cakeHeartId: Long?,
		userId: Long?,
		pageSize: Int
	): List<HeartCakeImageResponseParam> {
		return cakeHeartQueryRepository.searchCakeImagesByCursorAndHeart(cakeHeartId, userId, pageSize)
	}
}
