package com.cakk.core.mapper

import com.cakk.core.dto.param.shop.CreateShopParam
import com.cakk.core.dto.param.user.OwnerCandidateParam
import com.cakk.core.dto.response.shop.*
import com.cakk.infrastructure.persistence.entity.shop.CakeShop
import com.cakk.infrastructure.persistence.entity.user.BusinessInformation

fun supplyCakeShopOwnerCandidatesResponseBy(businessInformationList: List<com.cakk.infrastructure.persistence.entity.user.BusinessInformation>): CakeShopOwnerCandidatesResponse {
	val candidates = businessInformationList.map {
		OwnerCandidateParam(
			userId = it.user.id,
			nickname = it.user.nickname,
			profileImageUrl = it.user.profileImageUrl,
			email = it.user.email,
			timestamp = it.updatedAt
		)
	}
		.toList()

	return CakeShopOwnerCandidatesResponse(candidates)
}

fun supplyCakeShopOwnerCandidateResponseBy(businessInformation: com.cakk.infrastructure.persistence.entity.user.BusinessInformation): CakeShopOwnerCandidateResponse {
	return CakeShopOwnerCandidateResponse(
		userId = businessInformation.user.id,
		cakeShopId = businessInformation.cakeShop.id,
		email = businessInformation.user.email,
		businessRegistrationImageUrl = businessInformation.businessRegistrationImageUrl,
		idCardImageUrl = businessInformation.idCardImageUrl,
		emergencyContact = businessInformation.emergencyContact
	)
}

fun supplyBusinessInformationBy(param: CreateShopParam, cakeShop: com.cakk.infrastructure.persistence.entity.shop.CakeShop): com.cakk.infrastructure.persistence.entity.user.BusinessInformation {
	return com.cakk.infrastructure.persistence.entity.user.BusinessInformation.builder()
		.businessNumber(param.businessNumber)
		.cakeShop(cakeShop)
		.build()
}
