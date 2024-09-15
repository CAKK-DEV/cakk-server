package com.cakk.core.facade.cake

import com.cakk.domain.mysql.annotation.Reader
import com.cakk.domain.mysql.entity.user.User
import java.util.function.Supplier

class BusinessInformationReader {
    private val businessInformationJpaRepository: BusinessInformationJpaRepository? = null
    fun isExistBusinessInformation(owner: User?, cakeShopId: Long?): Boolean {
        return businessInformationJpaRepository.existsBusinessInformationByUserAndCakeShop_Id(owner, cakeShopId)
    }

    fun findAllWithCakeShopByUser(owner: User?): List<BusinessInformation> {
        return businessInformationJpaRepository.findAllWithCakeShopByUser(owner)
    }

    fun findAllCakeShopBusinessOwnerCandidates(): List<BusinessInformation> {
        return businessInformationJpaRepository.findAllCakeShopBusinessOwnerCandidates(VerificationStatus.PENDING)
    }

    fun findByUserId(userId: Long?): BusinessInformation {
        return businessInformationJpaRepository.findBusinessInformationByUserId(userId)
                .orElseThrow<CakkException>(Supplier<CakkException> { CakkException(ReturnCode.NOT_EXIST_CAKE_SHOP) })
    }
}
