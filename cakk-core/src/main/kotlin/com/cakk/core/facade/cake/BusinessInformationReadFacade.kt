package com.cakk.core.facade.cake

import com.cakk.common.enums.ReturnCode
import com.cakk.common.enums.VerificationStatus
import com.cakk.common.exception.CakkException
import com.cakk.core.annotation.DomainFacade
import com.cakk.domain.mysql.entity.user.BusinessInformation
import com.cakk.domain.mysql.entity.user.User
import com.cakk.domain.mysql.repository.jpa.BusinessInformationJpaRepository

@DomainFacade
class BusinessInformationReadFacade(
	private val businessInformationJpaRepository: BusinessInformationJpaRepository
) {
    fun isExistBusinessInformation(owner: User, cakeShopId: Long): Boolean {
        return businessInformationJpaRepository.existsBusinessInformationByUserAndCakeShop_Id(owner, cakeShopId)
    }

    fun findAllWithCakeShopByUser(owner: User): List<BusinessInformation> {
        return businessInformationJpaRepository.findAllWithCakeShopByUser(owner)
    }

    fun findAllCakeShopBusinessOwnerCandidates(): List<BusinessInformation> {
        return businessInformationJpaRepository.findAllCakeShopBusinessOwnerCandidates(VerificationStatus.PENDING)
    }

    fun findByUserId(userId: Long): BusinessInformation {
        return businessInformationJpaRepository.findBusinessInformationByUserId(userId) ?: throw CakkException(ReturnCode.NOT_EXIST_CAKE_SHOP)
    }
}
