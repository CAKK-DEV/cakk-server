package com.cakk.core.facade.cake

import java.util.*

import com.cakk.common.enums.CakeDesignCategory
import com.cakk.common.enums.ReturnCode
import com.cakk.common.exception.CakkException
import com.cakk.core.annotation.DomainFacade
import com.cakk.core.dto.param.cake.CakeSearchParam
import com.cakk.infrastructure.cache.repository.CakeViewsRedisRepository
import com.cakk.infrastructure.persistence.entity.cake.CakeCategoryEntity
import com.cakk.infrastructure.persistence.entity.cake.CakeEntity
import com.cakk.infrastructure.persistence.entity.user.UserEntity
import com.cakk.infrastructure.persistence.repository.jpa.CakeCategoryJpaRepository
import com.cakk.infrastructure.persistence.repository.jpa.CakeJpaRepository
import com.cakk.infrastructure.persistence.repository.query.CakeQueryRepository

@DomainFacade
class CakeReadFacade(
	private val cakeJpaRepository: CakeJpaRepository,
	private val cakeCategoryJpaRepository: CakeCategoryJpaRepository,
	private val cakeQueryRepository: CakeQueryRepository,
	private val cakeViewsRedisRepository: CakeViewsRedisRepository,
) {

	fun findById(cakeId: Long): CakeEntity {
		return cakeJpaRepository.findById(cakeId).orElseThrow { CakkException(ReturnCode.NOT_EXIST_CAKE) }
	}

	fun findByIdWithHeart(cakeId: Long): CakeEntity {
		val cake: CakeEntity = cakeQueryRepository.searchByIdWithHeart(cakeId) ?: throw CakkException(ReturnCode.NOT_EXIST_CAKE)

		return cake
	}

	fun searchCakeImagesByCursorAndCategory(
		cakeId: Long?,
		category: CakeDesignCategory,
		pageSize: Int
	): List<com.cakk.infrastructure.persistence.param.cake.CakeImageWithShopInfoResponseParam> {
		return cakeQueryRepository.searchCakeImagesByCursorAndCategory(cakeId, category, pageSize)
	}

	fun searchCakeImagesByCursorAndCakeShopId(
		cakeId: Long?,
		cakeShopId: Long?,
		pageSize: Int
	): List<com.cakk.infrastructure.persistence.param.cake.CakeImageWithShopInfoResponseParam> {
		return cakeQueryRepository.searchCakeImagesByCursorAndCakeShopId(cakeId, cakeShopId, pageSize)
	}

	fun searchCakeImagesByCursorAndSearchKeyword(param: CakeSearchParam): List<com.cakk.infrastructure.persistence.param.cake.CakeImageWithShopInfoResponseParam> {
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
	): Pair<List<Long>, List<com.cakk.infrastructure.persistence.param.cake.CakeImageWithShopInfoResponseParam>> {
		val cakeIds: List<Long> = cakeViewsRedisRepository.findTopCakeIdsByOffsetAndCount(offset, pageSize.toLong())

		return when {
			cakeIds.isEmpty() -> Pair(listOf(), listOf())
			else -> Pair(cakeIds, cakeQueryRepository.searchCakeImagesByCakeIds(cakeIds))
		}
	}

	fun findWithCakeTagsAndCakeCategories(cakeId: Long, owner: UserEntity): CakeEntity {
		return cakeQueryRepository.searchWithCakeTagsAndCakeCategories(cakeId, owner)
			.orElseThrow { CakkException(ReturnCode.NOT_CAKE_SHOP_OWNER) }
	}

	fun searchCakeDetailById(cakeId: Long?): com.cakk.infrastructure.persistence.param.cake.CakeDetailParam {
		val param: com.cakk.infrastructure.persistence.param.cake.CakeDetailParam = cakeQueryRepository.searchCakeDetailById(cakeId)
		if (Objects.isNull(param)) {
			throw CakkException(ReturnCode.NOT_EXIST_CAKE)
		}
		return param
	}

	fun findCakeCategoryByCakeId(cakeId: Long?): CakeCategoryEntity {
		return cakeCategoryJpaRepository.findByCakeId(cakeId) ?: throw CakkException(ReturnCode.NOT_EXIST_CAKE_CATEGORY)
	}
}

