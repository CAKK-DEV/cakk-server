package com.cakk.core.service.like

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

import com.cakk.core.annotation.DistributedLock
import com.cakk.core.dto.param.search.HeartCakeSearchParam
import com.cakk.core.dto.param.search.HeartCakeShopSearchParam
import com.cakk.core.dto.response.like.HeartCakeImageListResponse
import com.cakk.core.dto.response.like.HeartCakeShopListResponse
import com.cakk.core.dto.response.like.HeartResponse
import com.cakk.core.facade.cake.CakeReadFacade
import com.cakk.core.facade.cake.CakeShopReadFacade
import com.cakk.core.facade.cake.CakeShopUserReadFacade
import com.cakk.core.facade.user.UserHeartFacade
import com.cakk.core.mapper.supplyHeartCakeImageListResponseBy
import com.cakk.core.mapper.supplyHeartCakeShopListResponseBy
import com.cakk.core.mapper.supplyHeartResponseBy
import com.cakk.infrastructure.persistence.entity.user.UserEntity

@Service
class HeartService(
	private val cakeReadFacade: CakeReadFacade,
	private val cakeShopReadFacade: CakeShopReadFacade,
	private val cakeShopUserReadFacade: CakeShopUserReadFacade,
	private val userHeartFacade: UserHeartFacade
) {

	@Transactional(readOnly = true)
	fun searchCakeImagesByCursorAndHeart(param: HeartCakeSearchParam): HeartCakeImageListResponse {
		val cakeImages = cakeShopUserReadFacade.searchCakeImagesByCursorAndHeart(
			cakeHeartId = param.cakeHeartId,
			userId = param.userEntity.id,
			pageSize = param.pageSize
		)

		return supplyHeartCakeImageListResponseBy(cakeImages)
	}

	@Transactional(readOnly = true)
	fun searchCakeShopByCursorAndHeart(param: HeartCakeShopSearchParam): HeartCakeShopListResponse {
		val cakeShops = cakeShopUserReadFacade.searchAllCakeShopsByCursorAndHeart(
			cakeShopHeartId = param.cakeShopHeartId,
			userId = param.userEntity.id,
			pageSize = param.pageSize
		)

		return supplyHeartCakeShopListResponseBy(cakeShops)
	}

	@Transactional(readOnly = true)
	fun isHeartCake(userEntity: UserEntity, cakeId: Long): HeartResponse {
		val cake = cakeReadFacade.findByIdWithHeart(cakeId)
		val isHeart = cake.isHeartedBy(userEntity)

		return supplyHeartResponseBy(isHeart)
	}

	@Transactional(readOnly = true)
	fun isHeartCakeShop(userEntity: UserEntity, cakeShopId: Long): HeartResponse {
		val cakeShop = cakeShopReadFacade.findByIdWithHeart(cakeShopId)
		val isHeart = cakeShop.isHeartedBy(userEntity)

		return supplyHeartResponseBy(isHeart)
	}

	@DistributedLock(key = "#cakeId")
	fun heartCake(userEntity: UserEntity, cakeId: Long) {
		val cake = cakeReadFacade.findByIdWithHeart(cakeId)

		userHeartFacade.heartCake(userEntity, cake)
	}

	@DistributedLock(key = "#cakeShopId")
	fun heartCakeShop(userEntity: UserEntity, cakeShopId: Long) {
		val cakeShop = cakeShopReadFacade.findByIdWithHeart(cakeShopId)

		userHeartFacade.heartCakeShop(userEntity, cakeShop)
	}
}
