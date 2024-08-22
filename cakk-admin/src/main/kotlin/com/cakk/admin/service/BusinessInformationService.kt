package com.cakk.admin.service

import com.cakk.admin.dto.request.PromotionRequest
import com.cakk.admin.dto.response.CakeShopOwnerCandidateResponse
import com.cakk.admin.dto.response.CakeShopOwnerCandidatesResponse
import com.cakk.admin.mapper.*
import com.cakk.domain.mysql.bo.user.VerificationPolicy
import com.cakk.domain.mysql.entity.user.BusinessInformation
import com.cakk.domain.mysql.entity.user.User
import com.cakk.domain.mysql.repository.reader.BusinessInformationReader
import com.cakk.domain.mysql.repository.reader.CakeShopReader
import com.cakk.domain.mysql.repository.reader.UserReader
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class BusinessInformationService(
    private val businessInformationReader: BusinessInformationReader,
    private val userReader: UserReader,
    private val cakeShopReader: CakeShopReader,
    private val verificationPolicy: VerificationPolicy,
) {

    @Transactional
    fun promoteUserToBusinessOwner(dto: PromotionRequest) {
        val user: User = userReader.findByUserId(dto.userId)
        val businessInformation: BusinessInformation = cakeShopReader.findBusinessInformationWithShop(dto.cakeShopId)

        businessInformation.updateBusinessOwner(verificationPolicy, user)
    }

    @Transactional(readOnly = true)
    fun getBusinessOwnerCandidates(): CakeShopOwnerCandidatesResponse {
        var businessInformationList = businessInformationReader.findAllCakeShopBusinessOwnerCandidates()

        businessInformationList = businessInformationList
            .filter { it.isBusinessOwnerCandidate(verificationPolicy) }
            .toList()

        return supplyCakeShopOwnerCandidatesResponseBy(businessInformationList)
    }

    @Transactional(readOnly = true)
    fun getCandidateInformation(userId: Long): CakeShopOwnerCandidateResponse {
        val businessInformation = businessInformationReader.findByUserId(userId)

        return supplyCakeShopOwnerCandidateResponseBy(businessInformation)
    }
}