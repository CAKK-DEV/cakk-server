package com.cakk.core.facade.cake

import com.cakk.common.enums.ReturnCode
import com.cakk.common.exception.CakkException
import com.cakk.core.annotation.DomainFacade
import com.cakk.infrastructure.cache.repository.CakeShopViewsRedisRepository
import com.cakk.infrastructure.persistence.entity.shop.CakeShopEntity
import com.cakk.infrastructure.persistence.entity.shop.CakeShopLinkEntity
import com.cakk.infrastructure.persistence.entity.shop.CakeShopOperationEntity
import com.cakk.infrastructure.persistence.entity.user.BusinessInformationEntity
import com.cakk.infrastructure.persistence.entity.user.UserEntity
import com.cakk.infrastructure.persistence.repository.jpa.BusinessInformationJpaRepository
import com.cakk.infrastructure.persistence.repository.jpa.CakeShopJpaRepository
import com.cakk.infrastructure.persistence.repository.jpa.CakeShopLinkJpaRepository
import com.cakk.infrastructure.persistence.repository.jpa.CakeShopOperationJpaRepository
import com.cakk.infrastructure.persistence.repository.query.CakeShopQueryRepository
import org.locationtech.jts.geom.Point

@DomainFacade
class CakeShopReadFacade(
	private val cakeShopJpaRepository: CakeShopJpaRepository,
	private val cakeShopQueryRepository: CakeShopQueryRepository,
	private val cakeShopOperationJpaRepository: CakeShopOperationJpaRepository,
	private val cakeShopLinkJpaRepository: CakeShopLinkJpaRepository,
	private val businessInformationJpaRepository: BusinessInformationJpaRepository,
	private val cakeShopViewsRedisRepository: CakeShopViewsRedisRepository
) {

	fun findById(cakeShopId: Long): CakeShopEntity {
		return cakeShopJpaRepository.findById(cakeShopId).orElseThrow { CakkException(ReturnCode.NOT_EXIST_CAKE_SHOP) }
	}

	fun findByIdWithHeart(cakeShopId: Long): CakeShopEntity {
		return cakeShopQueryRepository.searchByIdWithHeart(cakeShopId) ?: throw CakkException(ReturnCode.NOT_EXIST_CAKE_SHOP)
	}

	fun findByIdWithLike(cakeShopId: Long): CakeShopEntity {
		return cakeShopQueryRepository.searchByIdWithLike(cakeShopId) ?: throw CakkException(ReturnCode.NOT_EXIST_CAKE_SHOP)
	}

	fun searchSimpleById(cakeShopId: Long): com.cakk.infrastructure.persistence.param.shop.CakeShopSimpleParam {
		return cakeShopQueryRepository.searchSimpleById(cakeShopId) ?: throw CakkException(ReturnCode.NOT_EXIST_CAKE_SHOP)
	}

	fun searchDetailById(cakeShopId: Long): com.cakk.infrastructure.persistence.param.shop.CakeShopDetailParam {
		return cakeShopQueryRepository.searchDetailById(cakeShopId) ?: throw CakkException(ReturnCode.NOT_EXIST_CAKE_SHOP)
	}

	fun searchInfoById(cakeShopId: Long): com.cakk.infrastructure.persistence.param.shop.CakeShopInfoParam {
		return cakeShopQueryRepository.searchInfoById(cakeShopId) ?: throw CakkException(ReturnCode.NOT_EXIST_CAKE_SHOP)
	}

	fun findBusinessInformationWithShop(cakeShopId: Long): BusinessInformationEntity {
		return businessInformationJpaRepository.findBusinessInformationWithCakeShop(cakeShopId)
			?: throw CakkException(ReturnCode.NOT_EXIST_CAKE_SHOP)
	}

	fun findBusinessInformationByCakeShopId(cakeShopId: Long): BusinessInformationEntity {
		return businessInformationJpaRepository.findBusinessInformationWithCakeShop(cakeShopId)
			?: throw CakkException(ReturnCode.NOT_EXIST_CAKE_SHOP)
	}

	fun searchShopByLocationBased(point: Point?, distance: Double?): List<com.cakk.infrastructure.persistence.bo.shop.CakeShopByLocationParam> {
		return cakeShopQueryRepository.findShopsByLocationBased(point, distance)
	}

	fun searchShopBySearch(param: com.cakk.infrastructure.persistence.param.shop.CakeShopSearchParam): List<CakeShopEntity> {
		return cakeShopQueryRepository.searchByKeywordWithLocation(
			param.cakeShopId,
			param.keyword,
			param.location,
			param.pageSize
		)
	}

	fun searchWithShopLinks(owner: UserEntity?, cakeShopId: Long?): CakeShopEntity {
		return cakeShopQueryRepository.searchWithShopLinks(owner, cakeShopId)
			.orElseThrow { CakkException(ReturnCode.NOT_CAKE_SHOP_OWNER) }
	}

	fun searchByIdAndOwner(cakeShopId: Long, owner: UserEntity): CakeShopEntity {
		return cakeShopQueryRepository.searchWithBusinessInformationAndOwnerById(owner, cakeShopId)
			.orElseThrow { CakkException(ReturnCode.NOT_CAKE_SHOP_OWNER) }
	}

	fun searchWithOperations(owner: UserEntity?, cakeShopId: Long?): CakeShopEntity {
		return cakeShopQueryRepository.searchWithOperations(owner, cakeShopId)
			.orElseThrow { CakkException(ReturnCode.NOT_CAKE_SHOP_OWNER) }
	}

	fun searchBestShops(
		offset: Long,
		pageSize: Int
	): List<CakeShopEntity> {
		val cakeShopIds = cakeShopViewsRedisRepository.findTopShopIdsByOffsetAndCount(offset, pageSize.toLong())

		return when {
			cakeShopIds.isEmpty() -> listOf()
			else -> cakeShopQueryRepository.searchByShopIds(cakeShopIds)
		}
	}

	fun findCakeShopOperationsByCakeShopId(cakeShopId: Long): List<CakeShopOperationEntity> {
		return cakeShopOperationJpaRepository.findAllByCakeShopId(cakeShopId)
	}

	fun findCakeShopLinksByCakeShopId(cakeShopId: Long): List<CakeShopLinkEntity> {
		return cakeShopLinkJpaRepository.findAllByCakeShopId(cakeShopId)
	}
}

