package com.cakk.core.facade.cake

import com.cakk.common.enums.ReturnCode
import com.cakk.common.enums.VerificationStatus
import com.cakk.common.exception.CakkException
import com.cakk.core.annotation.DomainFacade
import com.cakk.infrastructure.persistence.repository.jpa.BusinessInformationJpaRepository

@DomainFacade
class BusinessInformationReadFacade(
	private val businessInformationJpaRepository: BusinessInformationJpaRepository
) {

    fun isExistBusinessInformation(owner: com.cakk.infrastructure.persistence.entity.user.User, cakeShopId: Long): Boolean {
        return businessInformationJpaRepository.existsBusinessInformationByUserAndCakeShop_Id(owner, cakeShopId)
    }

    fun findAllWithCakeShopByUser(owner: com.cakk.infrastructure.persistence.entity.user.User): List<com.cakk.infrastructure.persistence.entity.user.BusinessInformation> {
        return businessInformationJpaRepository.findAllWithCakeShopByUser(owner)
    }

    fun findAllCakeShopBusinessOwnerCandidates(): List<com.cakk.infrastructure.persistence.entity.user.BusinessInformation> {
        return businessInformationJpaRepository.findAllCakeShopBusinessOwnerCandidates(VerificationStatus.PENDING)
    }

    fun findByUserId(userId: Long): com.cakk.infrastructure.persistence.entity.user.BusinessInformation {
        return businessInformationJpaRepository.findBusinessInformationByUserId(userId) ?: throw CakkException(ReturnCode.NOT_EXIST_CAKE_SHOP)
    }
}

