package com.cakk.core.facade.cake

import java.util.*

import com.cakk.common.enums.CakeDesignCategory
import com.cakk.common.enums.ReturnCode
import com.cakk.common.exception.CakkException
import com.cakk.core.annotation.DomainFacade
import com.cakk.domain.mysql.dto.param.cake.CakeDetailParam
import com.cakk.domain.mysql.dto.param.cake.CakeImageResponseParam
import com.cakk.domain.mysql.dto.param.cake.CakeImageWithShopInfoResponseParam
import com.cakk.core.dto.param.cake.CakeSearchParam
import com.cakk.domain.mysql.entity.cake.Cake
import com.cakk.domain.mysql.entity.cake.CakeCategory
import com.cakk.domain.mysql.entity.user.User
import com.cakk.domain.mysql.repository.jpa.CakeCategoryJpaRepository
import com.cakk.domain.mysql.repository.jpa.CakeJpaRepository
import com.cakk.domain.mysql.repository.query.CakeQueryRepository
import com.cakk.domain.redis.repository.CakeViewsRedisRepository

@DomainFacade
class CakeReadFacade(
	private val cakeJpaRepository: CakeJpaRepository,
	private val cakeCategoryJpaRepository: CakeCategoryJpaRepository,
	private val cakeQueryRepository: CakeQueryRepository,
	private val cakeViewsRedisRepository: CakeViewsRedisRepository,
) {

	fun findById(cakeId: Long): Cake {
		return cakeJpaRepository.findById(cakeId).orElseThrow { CakkException(ReturnCode.NOT_EXIST_CAKE) }
	}

	fun findByIdWithHeart(cakeId: Long): Cake {
		val cake: Cake = cakeQueryRepository.searchByIdWithHeart(cakeId) ?: throw CakkException(ReturnCode.NOT_EXIST_CAKE)

		return cake
	}

	fun searchCakeImagesByCursorAndCategory(
		cakeId: Long?,
		category: CakeDesignCategory,
		pageSize: Int
	): List<CakeImageWithShopInfoResponseParam> {
		return cakeQueryRepository.searchCakeImagesByCursorAndCategory(cakeId, category, pageSize)
	}

	fun searchCakeImagesByCursorAndCakeShopId(
		cakeId: Long?,
		cakeShopId: Long?,
		pageSize: Int
	): List<CakeImageResponseParam> {
		return cakeQueryRepository.searchCakeImagesByCursorAndCakeShopId(cakeId, cakeShopId, pageSize)
	}

	fun searchCakeImagesByCursorAndSearchKeyword(param: CakeSearchParam): List<CakeImageResponseParam> {
		return cakeQueryRepository.searchCakeImagesByCursorAndSearchKeyword(
			param.cakeId,
			param.keyword,
			param.location,
			param.pageSize
		)
	}

	fun searchBestCakeImages(
		offset: Long,
		pageSize: Int
	): Pair<List<Long>, List<CakeImageResponseParam>> {
		val cakeIds: List<Long> = cakeViewsRedisRepository.findTopCakeIdsByOffsetAndCount(offset, pageSize.toLong())

		return when {
			cakeIds.isEmpty() -> Pair(listOf(), listOf())
			else -> Pair(cakeIds, cakeQueryRepository.searchCakeImagesByCakeIds(cakeIds))
		}
	}

	fun findWithCakeTagsAndCakeCategories(cakeId: Long, owner: User): Cake {
		return cakeQueryRepository.searchWithCakeTagsAndCakeCategories(cakeId, owner)
			.orElseThrow { CakkException(ReturnCode.NOT_CAKE_SHOP_OWNER) }
	}

	fun searchCakeDetailById(cakeId: Long?): CakeDetailParam {
		val param: CakeDetailParam = cakeQueryRepository.searchCakeDetailById(cakeId)
		if (Objects.isNull(param)) {
			throw CakkException(ReturnCode.NOT_EXIST_CAKE)
		}
		return param
	}

	fun findCakeCategoryByCakeId(cakeId: Long?): CakeCategory {
		return cakeCategoryJpaRepository.findByCakeId(cakeId) ?: throw CakkException(ReturnCode.NOT_EXIST_CAKE_CATEGORY)
	}
}

