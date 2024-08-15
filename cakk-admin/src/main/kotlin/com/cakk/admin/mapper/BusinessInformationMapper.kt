package com.cakk.admin.mapper

import com.cakk.admin.dto.param.OwnerCandidateParam
import com.cakk.admin.dto.response.CakeShopOwnerCandidateResponse
import com.cakk.admin.dto.response.CakeShopOwnerCandidatesResponse
import com.cakk.domain.mysql.entity.user.BusinessInformation

fun supplyCakeShopOwnerCandidatesResponseBy(businessInformationList: List<BusinessInformation>): CakeShopOwnerCandidatesResponse {
    val candidates: List<OwnerCandidateParam> = businessInformationList
        .map { businessInformation: BusinessInformation ->
            OwnerCandidateParam(
                userId = businessInformation.user.id,
                nickname = businessInformation.user.nickname,
                profileImageUrl = businessInformation.user.profileImageUrl,
                email = businessInformation.user.email,
                timestamp = businessInformation.updatedAt
            )
        }.toList()

    return CakeShopOwnerCandidatesResponse(candidates)
}

fun supplyCakeShopOwnerCandidateResponseBy(businessInformation: BusinessInformation): CakeShopOwnerCandidateResponse {
    return CakeShopOwnerCandidateResponse(
        userId = businessInformation.user.id,
        cakeShopId = businessInformation.cakeShop.id,
        email = businessInformation.user.email,
        businessRegistrationImageUrl = businessInformation.businessRegistrationImageUrl,
        idCardImageUrl = businessInformation.idCardImageUrl,
        emergencyContact = businessInformation.emergencyContact
    )
}
