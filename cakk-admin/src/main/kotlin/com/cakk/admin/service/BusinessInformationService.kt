package com.cakk.admin.service

import com.cakk.admin.dto.request.PromotionRequest
import com.cakk.admin.dto.response.CakeShopOwnerCandidateResponse
import com.cakk.admin.dto.response.CakeShopOwnerCandidatesResponse
import com.cakk.admin.mapper.*
import com.cakk.core.facade.cake.BusinessInformationReadFacade
import com.cakk.core.facade.cake.CakeShopReadFacade
import com.cakk.core.facade.user.UserReadFacade
import com.cakk.domain.mysql.bo.user.VerificationPolicy
import com.cakk.domain.mysql.entity.user.BusinessInformation
import com.cakk.domain.mysql.entity.user.User
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class BusinessInformationService(
    private val businessInformationReadFacade: BusinessInformationReadFacade,
    private val userReadFacade: UserReadFacade,
    private val cakeShopReadFacade: CakeShopReadFacade,
    private val verificationPolicy: VerificationPolicy,
) {

    @Transactional
    fun promoteUserToBusinessOwner(dto: PromotionRequest) {
        val user: User = userReadFacade.findByUserId(dto.userId)
        val businessInformation: BusinessInformation = cakeShopReadFacade.findBusinessInformationWithShop(dto.cakeShopId)

        businessInformation.updateBusinessOwner(verificationPolicy, user)
    }

    @Transactional(readOnly = true)
    fun getBusinessOwnerCandidates(): CakeShopOwnerCandidatesResponse {
        var businessInformationList = businessInformationReadFacade.findAllCakeShopBusinessOwnerCandidates()

        businessInformationList = businessInformationList
            .filter { it.isBusinessOwnerCandidate(verificationPolicy) }
            .toList()

        return supplyCakeShopOwnerCandidatesResponseBy(businessInformationList)
    }

    @Transactional(readOnly = true)
    fun getCandidateInformation(userId: Long): CakeShopOwnerCandidateResponse {
        val businessInformation = businessInformationReadFacade.findByUserId(userId)

        return supplyCakeShopOwnerCandidateResponseBy(businessInformation)
    }
}
