package com.cakk.core.mapper

import com.cakk.core.dto.param.shop.CreateShopParam
import com.cakk.core.dto.param.user.OwnerCandidateParam
import com.cakk.core.dto.response.shop.*
import com.cakk.domain.mysql.entity.shop.CakeShop
import com.cakk.domain.mysql.entity.user.BusinessInformation

fun supplyCakeShopOwnerCandidatesResponseBy(businessInformationList: List<BusinessInformation>): CakeShopOwnerCandidatesResponse {
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

fun supplyBusinessInformationBy(param: CreateShopParam, cakeShop: CakeShop): BusinessInformation {
	return BusinessInformation.builder()
		.businessNumber(param.businessNumber)
		.cakeShop(cakeShop)
		.build()
}
