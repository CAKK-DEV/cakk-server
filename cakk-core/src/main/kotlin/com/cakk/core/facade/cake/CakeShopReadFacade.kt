package com.cakk.core.facade.cake

import com.cakk.common.enums.ReturnCode
import com.cakk.common.exception.CakkException
import com.cakk.core.annotation.DomainFacade
import com.cakk.infrastructure.cache.repository.CakeShopViewsRedisRepository
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

	fun findById(cakeShopId: Long): com.cakk.infrastructure.persistence.entity.shop.CakeShop {
		return cakeShopJpaRepository.findById(cakeShopId).orElseThrow { CakkException(ReturnCode.NOT_EXIST_CAKE_SHOP) }
	}

	fun findByIdWithHeart(cakeShopId: Long): com.cakk.infrastructure.persistence.entity.shop.CakeShop {
		return cakeShopQueryRepository.searchByIdWithHeart(cakeShopId) ?: throw CakkException(ReturnCode.NOT_EXIST_CAKE_SHOP)
	}

	fun findByIdWithLike(cakeShopId: Long): com.cakk.infrastructure.persistence.entity.shop.CakeShop {
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

	fun findBusinessInformationWithShop(cakeShopId: Long): com.cakk.infrastructure.persistence.entity.user.BusinessInformation {
		return businessInformationJpaRepository.findBusinessInformationWithCakeShop(cakeShopId)
			?: throw CakkException(ReturnCode.NOT_EXIST_CAKE_SHOP)
	}

	fun findBusinessInformationByCakeShopId(cakeShopId: Long): com.cakk.infrastructure.persistence.entity.user.BusinessInformation {
		return businessInformationJpaRepository.findBusinessInformationWithCakeShop(cakeShopId)
			?: throw CakkException(ReturnCode.NOT_EXIST_CAKE_SHOP)
	}

	fun searchShopByLocationBased(point: Point?, distance: Double?): List<com.cakk.infrastructure.persistence.bo.shop.CakeShopByLocationParam> {
		return cakeShopQueryRepository.findShopsByLocationBased(point, distance)
	}

	fun searchShopBySearch(param: com.cakk.infrastructure.persistence.param.shop.CakeShopSearchParam): List<com.cakk.infrastructure.persistence.entity.shop.CakeShop> {
		return cakeShopQueryRepository.searchByKeywordWithLocation(
			param.cakeShopId,
			param.keyword,
			param.location,
			param.pageSize
		)
	}

	fun searchWithShopLinks(owner: com.cakk.infrastructure.persistence.entity.user.User?, cakeShopId: Long?): com.cakk.infrastructure.persistence.entity.shop.CakeShop {
		return cakeShopQueryRepository.searchWithShopLinks(owner, cakeShopId)
			.orElseThrow { CakkException(ReturnCode.NOT_CAKE_SHOP_OWNER) }
	}

	fun searchByIdAndOwner(cakeShopId: Long, owner: com.cakk.infrastructure.persistence.entity.user.User): com.cakk.infrastructure.persistence.entity.shop.CakeShop {
		return cakeShopQueryRepository.searchWithBusinessInformationAndOwnerById(owner, cakeShopId)
			.orElseThrow { CakkException(ReturnCode.NOT_CAKE_SHOP_OWNER) }
	}

	fun searchWithOperations(owner: com.cakk.infrastructure.persistence.entity.user.User?, cakeShopId: Long?): com.cakk.infrastructure.persistence.entity.shop.CakeShop {
		return cakeShopQueryRepository.searchWithOperations(owner, cakeShopId)
			.orElseThrow { CakkException(ReturnCode.NOT_CAKE_SHOP_OWNER) }
	}

	fun searchBestShops(
		offset: Long,
		pageSize: Int
	): List<com.cakk.infrastructure.persistence.entity.shop.CakeShop> {
		val cakeShopIds = cakeShopViewsRedisRepository.findTopShopIdsByOffsetAndCount(offset, pageSize.toLong())

		return when {
			cakeShopIds.isEmpty() -> listOf()
			else -> cakeShopQueryRepository.searchByShopIds(cakeShopIds)
		}
	}

	fun findCakeShopOperationsByCakeShopId(cakeShopId: Long): List<com.cakk.infrastructure.persistence.entity.shop.CakeShopOperation> {
		return cakeShopOperationJpaRepository.findAllByCakeShopId(cakeShopId)
	}

	fun findCakeShopLinksByCakeShopId(cakeShopId: Long): List<com.cakk.infrastructure.persistence.entity.shop.CakeShopLink> {
		return cakeShopLinkJpaRepository.findAllByCakeShopId(cakeShopId)
	}
}

