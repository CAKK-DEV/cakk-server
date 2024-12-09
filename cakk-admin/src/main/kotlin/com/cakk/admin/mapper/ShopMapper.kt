package com.cakk.admin.mapper

import java.util.ArrayList

import com.cakk.admin.dto.request.*
import com.cakk.core.dto.param.shop.CreateShopParam
import com.cakk.core.dto.param.shop.PromotionParam
import com.cakk.core.mapper.supplyPointBy
import com.cakk.infrastructure.persistence.param.link.UpdateLinkParam
import com.cakk.infrastructure.persistence.param.operation.UpdateShopOperationParam
import com.cakk.infrastructure.persistence.param.shop.CakeShopUpdateParam
import com.cakk.infrastructure.persistence.param.shop.UpdateShopAddressParam
import com.cakk.infrastructure.persistence.entity.shop.CakeShopLink
import com.cakk.infrastructure.persistence.entity.user.User

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
	user: com.cakk.infrastructure.persistence.entity.user.User,
	cakeShopId: Long
): com.cakk.infrastructure.persistence.param.shop.CakeShopUpdateParam {
	return com.cakk.infrastructure.persistence.param.shop.CakeShopUpdateParam.builder()
		.user(user)
		.cakeShopId(cakeShopId)
		.thumbnailUrl(request.thumbnailUrl)
		.shopName(request.shopName)
		.shopBio(request.shopBio)
		.shopDescription(request.shopDescription)
		.build()
}

fun supplyUpdateLinkParamBy(
	dto: LinkUpdateByAdminRequest,
	user: com.cakk.infrastructure.persistence.entity.user.User,
	cakeShopId: Long
): com.cakk.infrastructure.persistence.param.link.UpdateLinkParam {
	val cakeShopLinks: MutableList<com.cakk.infrastructure.persistence.entity.shop.CakeShopLink> = ArrayList()

	dto.instagram?.let { cakeShopLinks.add(supplyCakeShopLinkByInstagram(dto.instagram)) }
	dto.kakao?.let { cakeShopLinks.add(supplyCakeShopLinkByKakao(dto.kakao)) }
	dto.web?.let { cakeShopLinks.add(supplyCakeShopLinkByWeb(dto.web)) }

	return com.cakk.infrastructure.persistence.param.link.UpdateLinkParam(user, cakeShopId, cakeShopLinks)
}

fun supplyUpdateShopOperationParamBy(
	request: ShopOperationUpdateByAdminRequest,
	user: com.cakk.infrastructure.persistence.entity.user.User,
	cakeShopId: Long
): com.cakk.infrastructure.persistence.param.operation.UpdateShopOperationParam {
	val cakeShopOperations = supplyCakeShopOperationListBy(request.operationDays!!)

	return com.cakk.infrastructure.persistence.param.operation.UpdateShopOperationParam(cakeShopOperations, user, cakeShopId)
}

fun supplyUpdateShopAddressParamBy(
	dto: AddressUpdateByAdminRequest,
	user: com.cakk.infrastructure.persistence.entity.user.User,
	cakeShopId: Long
): com.cakk.infrastructure.persistence.param.shop.UpdateShopAddressParam {
	return com.cakk.infrastructure.persistence.param.shop.UpdateShopAddressParam(
		dto.shopAddress!!,
		supplyPointBy(dto.latitude!!, dto.longitude!!),
		user,
		cakeShopId
	)
}

fun supplyPromotionParamBy(request: PromotionRequest): PromotionParam {
	return PromotionParam(
		userId = request.userId!!,
		cakeShopId = request.cakeShopId!!
	)
}
