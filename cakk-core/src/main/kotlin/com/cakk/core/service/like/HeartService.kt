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
import com.cakk.domain.mysql.entity.user.User

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
			userId = param.user.id,
			pageSize = param.pageSize
		)

		return supplyHeartCakeImageListResponseBy(cakeImages)
	}

	@Transactional(readOnly = true)
	fun searchCakeShopByCursorAndHeart(param: HeartCakeShopSearchParam): HeartCakeShopListResponse {
		val cakeShops = cakeShopUserReadFacade.searchAllCakeShopsByCursorAndHeart(
			cakeShopHeartId = param.cakeShopHeartId,
			userId = param.user.id,
			pageSize = param.pageSize
		)

		return supplyHeartCakeShopListResponseBy(cakeShops)
	}

	@Transactional(readOnly = true)
	fun isHeartCake(user: User, cakeId: Long): HeartResponse {
		val cake = cakeReadFacade.findByIdWithHeart(cakeId)
		val isHeart = cake.isHeartedBy(user)

		return supplyHeartResponseBy(isHeart)
	}

	@Transactional(readOnly = true)
	fun isHeartCakeShop(user: User, cakeShopId: Long): HeartResponse {
		val cakeShop = cakeShopReadFacade.findByIdWithHeart(cakeShopId)
		val isHeart = cakeShop.isHeartedBy(user)

		return supplyHeartResponseBy(isHeart)
	}

	@DistributedLock(key = "#cakeId")
	fun heartCake(user: User, cakeId: Long) {
		val cake = cakeReadFacade.findByIdWithHeart(cakeId)

		userHeartFacade.heartCake(user, cake)
	}

	@DistributedLock(key = "#cakeShopId")
	fun heartCakeShop(user: User, cakeShopId: Long) {
		val cakeShop = cakeShopReadFacade.findByIdWithHeart(cakeShopId)

		userHeartFacade.heartCakeShop(user, cakeShop)
	}
}
