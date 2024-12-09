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
import com.cakk.infrastructure.persistence.param.link.UpdateLinkParam
import com.cakk.infrastructure.persistence.param.operation.UpdateShopOperationParam
import com.cakk.infrastructure.persistence.param.shop.CakeShopUpdateParam
import com.cakk.infrastructure.persistence.param.shop.UpdateShopAddressParam
import com.cakk.infrastructure.persistence.param.user.CertificationParam
import com.cakk.infrastructure.persistence.entity.shop.CakeShopLink
import com.cakk.infrastructure.persistence.entity.user.User

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

fun supplyCertificationParamBy(request: CertificationRequest, user: com.cakk.infrastructure.persistence.entity.user.User): com.cakk.infrastructure.persistence.param.user.CertificationParam {
    return com.cakk.infrastructure.persistence.param.user.CertificationParam.builder()
        .businessRegistrationImageUrl(request.businessRegistrationImageUrl)
        .idCardImageUrl(request.idCardImageUrl)
        .cakeShopId(request.cakeShopId)
        .emergencyContact(request.emergencyContact)
        .message(request.message)
        .user(user)
        .build()
}

fun supplyUpdateLinkParamBy(
    dto: UpdateLinkRequest,
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
    request: UpdateShopOperationRequest,
    user: com.cakk.infrastructure.persistence.entity.user.User,
    cakeShopId: Long
): com.cakk.infrastructure.persistence.param.operation.UpdateShopOperationParam {
    val cakeShopOperations = supplyCakeShopOperationListBy(request.operationDays!!)

    return com.cakk.infrastructure.persistence.param.operation.UpdateShopOperationParam(cakeShopOperations, user, cakeShopId)
}

fun supplyUpdateShopAddressParamBy(dto: UpdateShopAddressRequest, user: com.cakk.infrastructure.persistence.entity.user.User, cakeShopId: Long): com.cakk.infrastructure.persistence.param.shop.UpdateShopAddressParam {
	return com.cakk.infrastructure.persistence.param.shop.UpdateShopAddressParam(
        dto.shopAddress!!,
        supplyPointBy(dto.latitude!!, dto.longitude!!),
        user,
        cakeShopId
    )
}
