package com.cakk.core.facade.cake

import com.cakk.common.enums.ReturnCode
import com.cakk.common.exception.CakkException
import com.cakk.core.annotation.DomainFacade
import com.cakk.domain.mysql.bo.shop.CakeShopByLocationParam
import com.cakk.domain.mysql.dto.param.shop.CakeShopDetailParam
import com.cakk.domain.mysql.dto.param.shop.CakeShopInfoParam
import com.cakk.domain.mysql.dto.param.shop.CakeShopSearchParam
import com.cakk.domain.mysql.dto.param.shop.CakeShopSimpleParam
import com.cakk.domain.mysql.entity.shop.CakeShop
import com.cakk.domain.mysql.entity.shop.CakeShopLink
import com.cakk.domain.mysql.entity.shop.CakeShopOperation
import com.cakk.domain.mysql.entity.user.BusinessInformation
import com.cakk.domain.mysql.entity.user.User
import com.cakk.domain.mysql.repository.jpa.BusinessInformationJpaRepository
import com.cakk.domain.mysql.repository.jpa.CakeShopJpaRepository
import com.cakk.domain.mysql.repository.jpa.CakeShopLinkJpaRepository
import com.cakk.domain.mysql.repository.jpa.CakeShopOperationJpaRepository
import com.cakk.domain.mysql.repository.query.CakeShopQueryRepository
import org.locationtech.jts.geom.Point

@DomainFacade
class CakeShopReadFacade(
	private val cakeShopJpaRepository: CakeShopJpaRepository,
	private val cakeShopQueryRepository: CakeShopQueryRepository,
	private val cakeShopOperationJpaRepository: CakeShopOperationJpaRepository,
	private val cakeShopLinkJpaRepository: CakeShopLinkJpaRepository,
	private val businessInformationJpaRepository: BusinessInformationJpaRepository
) {
    fun findById(cakeShopId: Long): CakeShop {
        return cakeShopJpaRepository.findById(cakeShopId).orElseThrow { CakkException(ReturnCode.NOT_EXIST_CAKE_SHOP) }
	}

    fun findByIdWithHeart(cakeShopId: Long): CakeShop {
        return cakeShopQueryRepository.searchByIdWithHeart(cakeShopId) ?: throw CakkException(ReturnCode.NOT_EXIST_CAKE_SHOP)
    }

    fun findByIdWithLike(cakeShopId: Long?): CakeShop {
		return cakeShopQueryRepository.searchByIdWithLike(cakeShopId) ?: throw CakkException(ReturnCode.NOT_EXIST_CAKE_SHOP)
    }

    fun searchSimpleById(cakeShopId: Long?): CakeShopSimpleParam {
		return cakeShopQueryRepository.searchSimpleById(cakeShopId) ?: throw CakkException(ReturnCode.NOT_EXIST_CAKE_SHOP)
    }

	fun searchDetailById(cakeShopId: Long?): CakeShopDetailParam {
		return cakeShopQueryRepository.searchDetailById(cakeShopId) ?: throw CakkException(ReturnCode.NOT_EXIST_CAKE_SHOP)
	}

	fun searchInfoById(cakeShopId: Long?): CakeShopInfoParam {
		return cakeShopQueryRepository.searchInfoById(cakeShopId) ?: throw CakkException(ReturnCode.NOT_EXIST_CAKE_SHOP)
	}

    fun findBusinessInformationWithShop(cakeShopId: Long): BusinessInformation {
        return businessInformationJpaRepository.findBusinessInformationWithCakeShop(cakeShopId) ?: throw CakkException(ReturnCode.NOT_EXIST_CAKE_SHOP)
    }

    fun findBusinessInformationByCakeShopId(cakeShopId: Long): BusinessInformation {
        return businessInformationJpaRepository.findBusinessInformationWithCakeShop(cakeShopId) ?: throw CakkException(ReturnCode.NOT_EXIST_CAKE_SHOP)
    }

    fun searchShopByLocationBased(point: Point?, distance: Double?): List<CakeShopByLocationParam> {
        return cakeShopQueryRepository.findShopsByLocationBased(point, distance)
    }

    fun searchShopBySearch(param: CakeShopSearchParam): List<CakeShop> {
        return cakeShopQueryRepository.searchByKeywordWithLocation(
                param.cakeShopId,
                param.keyword,
                param.location,
                param.pageSize
        )
    }

    fun searchWithShopLinks(owner: User?, cakeShopId: Long?): CakeShop {
        return cakeShopQueryRepository.searchWithShopLinks(owner, cakeShopId)
                .orElseThrow { CakkException(ReturnCode.NOT_CAKE_SHOP_OWNER) }
	}

    fun searchByIdAndOwner(cakeShopId: Long?, owner: User?): CakeShop {
        return cakeShopQueryRepository.searchWithBusinessInformationAndOwnerById(owner, cakeShopId)
                .orElseThrow { CakkException(ReturnCode.NOT_CAKE_SHOP_OWNER) }
	}

    fun searchWithOperations(owner: User?, cakeShopId: Long?): CakeShop {
        return cakeShopQueryRepository.searchWithOperations(owner, cakeShopId)
                .orElseThrow { CakkException(ReturnCode.NOT_CAKE_SHOP_OWNER) }
	}

    fun searchShopsByShopIds(shopIds: List<Long>?): List<CakeShop> {
        return cakeShopQueryRepository.searchByShopIds(shopIds)
    }

	fun findCakeShopOperationsByCakeShopId(cakeShopId: Long): List<CakeShopOperation> {
		return cakeShopOperationJpaRepository.findAllByCakeShopId(cakeShopId)
	}

	fun findCakeShopLinksByCakeShopId(cakeShopId: Long): List<CakeShopLink> {
		return cakeShopLinkJpaRepository.findAllByCakeShopId(cakeShopId)
	}
}
