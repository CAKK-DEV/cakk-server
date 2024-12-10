package com.cakk.admin.mapper

import java.util.ArrayList

import com.cakk.admin.dto.request.*
import com.cakk.core.dto.param.shop.CreateShopParam
import com.cakk.core.dto.param.shop.PromotionParam
import com.cakk.core.mapper.supplyPointBy
import com.cakk.infrastructure.persistence.entity.shop.CakeShopLinkEntity
import com.cakk.infrastructure.persistence.entity.user.UserEntity

fun supplyCreateShopParamBy(request: CakeShopCreateByAdminRequest): CreateShopParam {
	return CreateShopParam(
		businessNumber = request.businessNumber,
		operationDays = request.operationDays!!,
		shopName = request.shopName!!,
		shopBio = request.shopBio,
		shopDescription = request.shopDescription,
		shopAddress = request.shopAddress!!,
		latitude = request.latitude!!,
		longitude = request.longitude!!,
		links = request.links!!
	)
}

fun supplyCakeShopUpdateParamBy(
	request: CakeShopUpdateByAdminRequest,
	userEntity: UserEntity,
	cakeShopId: Long
): com.cakk.infrastructure.persistence.param.shop.CakeShopUpdateParam {
	return com.cakk.infrastructure.persistence.param.shop.CakeShopUpdateParam.builder()
		.user(userEntity)
		.cakeShopId(cakeShopId)
		.thumbnailUrl(request.thumbnailUrl)
		.shopName(request.shopName)
		.shopBio(request.shopBio)
		.shopDescription(request.shopDescription)
		.build()
}

fun supplyUpdateLinkParamBy(
	dto: LinkUpdateByAdminRequest,
	userEntity: UserEntity,
	cakeShopId: Long
): com.cakk.infrastructure.persistence.param.link.UpdateLinkParam {
	val cakeShopLinks: MutableList<CakeShopLinkEntity> = ArrayList()

	dto.instagram?.let { cakeShopLinks.add(supplyCakeShopLinkByInstagram(dto.instagram)) }
	dto.kakao?.let { cakeShopLinks.add(supplyCakeShopLinkByKakao(dto.kakao)) }
	dto.web?.let { cakeShopLinks.add(supplyCakeShopLinkByWeb(dto.web)) }

	return com.cakk.infrastructure.persistence.param.link.UpdateLinkParam(userEntity, cakeShopId, cakeShopLinks)
}

fun supplyUpdateShopOperationParamBy(
	request: ShopOperationUpdateByAdminRequest,
	userEntity: UserEntity,
	cakeShopId: Long
): com.cakk.infrastructure.persistence.param.operation.UpdateShopOperationParam {
	val cakeShopOperations = supplyCakeShopOperationListBy(request.operationDays!!)

	return com.cakk.infrastructure.persistence.param.operation.UpdateShopOperationParam(cakeShopOperations, userEntity, cakeShopId)
}

fun supplyUpdateShopAddressParamBy(
	dto: AddressUpdateByAdminRequest,
	userEntity: UserEntity,
	cakeShopId: Long
): com.cakk.infrastructure.persistence.param.shop.UpdateShopAddressParam {
	return com.cakk.infrastructure.persistence.param.shop.UpdateShopAddressParam(
		dto.shopAddress!!,
		supplyPointBy(dto.latitude!!, dto.longitude!!),
		userEntity,
		cakeShopId
	)
}

fun supplyPromotionParamBy(request: PromotionRequest): PromotionParam {
	return PromotionParam(
		userId = request.userId!!,
		cakeShopId = request.cakeShopId!!
	)
}
