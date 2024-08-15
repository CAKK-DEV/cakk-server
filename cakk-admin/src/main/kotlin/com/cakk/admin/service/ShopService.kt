package com.cakk.admin.service

import com.cakk.admin.dto.response.CakeShopOwnerCandidateResponse
import com.cakk.admin.dto.response.CakeShopOwnerCandidatesResponse
import com.cakk.admin.mapper.supplyCakeShopOwnerCandidateResponseBy
import com.cakk.admin.mapper.supplyCakeShopOwnerCandidatesResponseBy
import com.cakk.domain.mysql.bo.user.VerificationPolicy
import com.cakk.domain.mysql.repository.reader.BusinessInformationReader
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ShopService(
    private val businessInformationReader: BusinessInformationReader,
    private val verificationPolicy: VerificationPolicy,
) {

    @Transactional(readOnly = true)
    fun getBusinessOwnerCandidates(): CakeShopOwnerCandidatesResponse {
        var businessInformationList = businessInformationReader.findAllCakeShopBusinessOwnerCandidates()

        businessInformationList = businessInformationList
            .filter { bi -> bi.isBusinessOwnerCandidate(verificationPolicy) }
            .toList()

        return supplyCakeShopOwnerCandidatesResponseBy(businessInformationList)
    }

    @Transactional(readOnly = true)
    fun getCandidateInformation(userId: Long?): CakeShopOwnerCandidateResponse {
        val businessInformation = businessInformationReader.findByUserId(userId)

        return supplyCakeShopOwnerCandidateResponseBy(businessInformation)
    }
}
