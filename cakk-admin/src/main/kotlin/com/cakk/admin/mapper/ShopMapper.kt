package com.cakk.admin.mapper

import java.util.ArrayList

import com.cakk.admin.dto.request.*
import com.cakk.core.dto.param.shop.CreateShopParam
import com.cakk.core.dto.param.shop.PromotionParam
import com.cakk.core.mapper.supplyPointBy
import com.cakk.domain.mysql.dto.param.link.UpdateLinkParam
import com.cakk.domain.mysql.dto.param.operation.UpdateShopOperationParam
import com.cakk.domain.mysql.dto.param.shop.CakeShopUpdateParam
import com.cakk.domain.mysql.dto.param.shop.UpdateShopAddressParam
import com.cakk.domain.mysql.entity.shop.CakeShopLink
import com.cakk.domain.mysql.entity.user.User

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
	user: User,
	cakeShopId: Long
): CakeShopUpdateParam {
	return CakeShopUpdateParam.builder()
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
	user: User,
	cakeShopId: Long
): UpdateLinkParam {
	val cakeShopLinks: MutableList<CakeShopLink> = ArrayList()

	dto.instagram?.let { cakeShopLinks.add(supplyCakeShopLinkByInstagram(dto.instagram)) }
	dto.kakao?.let { cakeShopLinks.add(supplyCakeShopLinkByKakao(dto.kakao)) }
	dto.web?.let { cakeShopLinks.add(supplyCakeShopLinkByWeb(dto.web)) }

	return UpdateLinkParam(user, cakeShopId, cakeShopLinks)
}

fun supplyUpdateShopOperationParamBy(
	request: ShopOperationUpdateByAdminRequest,
	user: User,
	cakeShopId: Long
): UpdateShopOperationParam {
	val cakeShopOperations = supplyCakeShopOperationListBy(request.operationDays!!)

	return UpdateShopOperationParam(cakeShopOperations, user, cakeShopId)
}

fun supplyUpdateShopAddressParamBy(
	dto: AddressUpdateByAdminRequest,
	user: User,
	cakeShopId: Long
): UpdateShopAddressParam {
	return UpdateShopAddressParam(
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
