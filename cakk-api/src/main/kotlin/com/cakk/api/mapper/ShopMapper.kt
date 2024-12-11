package com.cakk.api.mapper

import java.util.*

import com.cakk.api.dto.request.link.UpdateLinkRequest
import com.cakk.api.dto.request.operation.UpdateShopOperationRequest
import com.cakk.api.dto.request.shop.CreateShopRequest
import com.cakk.api.dto.request.shop.PromotionRequest
import com.cakk.api.dto.request.shop.UpdateShopAddressRequest
import com.cakk.api.dto.request.shop.UpdateShopRequest
import com.cakk.api.dto.request.user.CertificationRequest
import com.cakk.core.dto.param.shop.CreateShopParam
import com.cakk.core.dto.param.shop.PromotionParam
import com.cakk.core.mapper.*
import com.cakk.infrastructure.persistence.entity.shop.CakeShopLinkEntity
import com.cakk.infrastructure.persistence.entity.user.UserEntity
import com.cakk.infrastructure.persistence.param.link.UpdateLinkParam
import com.cakk.infrastructure.persistence.param.operation.UpdateShopOperationParam
import com.cakk.infrastructure.persistence.param.shop.CakeShopUpdateParam
import com.cakk.infrastructure.persistence.param.shop.UpdateShopAddressParam
import com.cakk.infrastructure.persistence.param.user.CertificationParam

fun supplyCreateShopParamBy(request: CreateShopRequest): CreateShopParam {
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

fun supplyPromotionParamBy(request: PromotionRequest): PromotionParam {
    return PromotionParam(
        userId = request.userId!!,
        cakeShopId = request.cakeShopId!!
    )
}

fun supplyCakeShopUpdateParamBy(
	request: UpdateShopRequest,
	userEntity: UserEntity,
	cakeShopId: Long
): CakeShopUpdateParam {
    return CakeShopUpdateParam.builder()
        .user(userEntity)
        .cakeShopId(cakeShopId)
        .thumbnailUrl(request.thumbnailUrl)
        .shopName(request.shopName)
        .shopBio(request.shopBio)
        .shopDescription(request.shopDescription)
        .build()
}

fun supplyCertificationParamBy(request: CertificationRequest, userEntity: UserEntity): CertificationParam {
    return CertificationParam.builder()
        .businessRegistrationImageUrl(request.businessRegistrationImageUrl)
        .idCardImageUrl(request.idCardImageUrl)
        .cakeShopId(request.cakeShopId)
        .emergencyContact(request.emergencyContact)
        .message(request.message)
        .user(userEntity)
        .build()
}

fun supplyUpdateLinkParamBy(
	dto: UpdateLinkRequest,
	userEntity: UserEntity,
	cakeShopId: Long
): UpdateLinkParam {
    val cakeShopLinks: MutableList<CakeShopLinkEntity> = ArrayList()

    dto.instagram?.let { cakeShopLinks.add(supplyCakeShopLinkByInstagram(dto.instagram)) }
    dto.kakao?.let { cakeShopLinks.add(supplyCakeShopLinkByKakao(dto.kakao)) }
    dto.web?.let { cakeShopLinks.add(supplyCakeShopLinkByWeb(dto.web)) }

    return UpdateLinkParam(userEntity, cakeShopId, cakeShopLinks)
}

fun supplyUpdateShopOperationParamBy(
	request: UpdateShopOperationRequest,
	userEntity: UserEntity,
	cakeShopId: Long
): UpdateShopOperationParam {
    val cakeShopOperations = supplyCakeShopOperationListBy(request.operationDays!!)

    return UpdateShopOperationParam(cakeShopOperations, userEntity, cakeShopId)
}

fun supplyUpdateShopAddressParamBy(dto: UpdateShopAddressRequest, userEntity: UserEntity, cakeShopId: Long): UpdateShopAddressParam {
	return UpdateShopAddressParam(
        dto.shopAddress!!,
        supplyPointBy(dto.latitude!!, dto.longitude!!),
        userEntity,
        cakeShopId
    )
}
