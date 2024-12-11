package com.cakk.core.mapper

import com.cakk.core.dto.param.shop.CreateShopParam
import com.cakk.core.dto.param.user.OwnerCandidateParam
import com.cakk.core.dto.response.shop.*
import com.cakk.infrastructure.persistence.entity.shop.CakeShopEntity
import com.cakk.infrastructure.persistence.entity.user.BusinessInformationEntity

fun supplyCakeShopOwnerCandidatesResponseBy(businessInformationEntityList: List<BusinessInformationEntity>): CakeShopOwnerCandidatesResponse {
	val candidates = businessInformationEntityList.map {
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

fun supplyCakeShopOwnerCandidateResponseBy(businessInformationEntity: BusinessInformationEntity): CakeShopOwnerCandidateResponse {
	return CakeShopOwnerCandidateResponse(
		userId = businessInformationEntity.user.id,
		cakeShopId = businessInformationEntity.cakeShop.id,
		email = businessInformationEntity.user.email,
		businessRegistrationImageUrl = businessInformationEntity.businessRegistrationImageUrl,
		idCardImageUrl = businessInformationEntity.idCardImageUrl,
		emergencyContact = businessInformationEntity.emergencyContact
	)
}

fun supplyBusinessInformationBy(param: CreateShopParam, cakeShop: CakeShopEntity): BusinessInformationEntity {
	return BusinessInformationEntity.builder()
		.businessNumber(param.businessNumber)
		.cakeShop(cakeShop)
		.build()
}
