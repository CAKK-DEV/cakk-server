package com.cakk.api.mapper

import java.util.*

import com.cakk.api.dto.request.link.UpdateLinkRequest
import com.cakk.api.dto.request.operation.UpdateShopOperationRequest
import com.cakk.api.dto.request.shop.CreateShopRequest
import com.cakk.api.dto.request.shop.PromotionRequest
import com.cakk.api.dto.request.shop.UpdateShopRequest
import com.cakk.api.dto.request.user.CertificationRequest
import com.cakk.core.dto.param.shop.CreateShopParam
import com.cakk.core.dto.param.shop.PromotionParam
import com.cakk.core.mapper.supplyCakeShopLinkByInstagram
import com.cakk.core.mapper.supplyCakeShopOperationListBy
import com.cakk.domain.mysql.dto.param.link.UpdateLinkParam
import com.cakk.domain.mysql.dto.param.operation.UpdateShopOperationParam
import com.cakk.domain.mysql.dto.param.shop.CakeShopUpdateParam
import com.cakk.domain.mysql.dto.param.user.CertificationParam
import com.cakk.domain.mysql.entity.shop.CakeShopLink
import com.cakk.domain.mysql.entity.user.User

fun supplyCreateShopParamBy(request: CreateShopRequest): CreateShopParam {
    return CreateShopParam(
        businessNumber = request.businessNumber,
        operationDays = request.operationDays,
        shopName = request.shopName,
        shopBio = request.shopBio,
        shopDescription = request.shopDescription,
        shopAddress = request.shopAddress,
        latitude = request.latitude,
        longitude = request.longitude,
        links = request.links
    )
}

fun supplyPromotionParamBy(request: PromotionRequest): PromotionParam {
    return PromotionParam(
        userId = request.userId,
        cakeShopId = request.cakeShopId
    )
}

fun supplyCakeShopUpdateParamBy(
    request: UpdateShopRequest,
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

fun supplyCertificationParamBy(request: CertificationRequest, user: User): CertificationParam {
    return CertificationParam.builder()
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
    user: User,
    cakeShopId: Long
): UpdateLinkParam {
    val cakeShopLinks: MutableList<CakeShopLink> = ArrayList()

    dto.instagram?.let { cakeShopLinks.add(supplyCakeShopLinkByInstagram(dto.instagram)) }
    dto.kakao?.let { cakeShopLinks.add(supplyCakeShopLinkByInstagram(dto.kakao)) }
    dto.web?.let { cakeShopLinks.add(supplyCakeShopLinkByInstagram(dto.web)) }

    return UpdateLinkParam(user, cakeShopId, cakeShopLinks)
}

fun supplyUpdateShopOperationParamBy(
    request: UpdateShopOperationRequest,
    user: User,
    cakeShopId: Long
): UpdateShopOperationParam {
    val cakeShopOperations = supplyCakeShopOperationListBy(request.operationDays!!)

    return UpdateShopOperationParam(cakeShopOperations, user, cakeShopId)
}
