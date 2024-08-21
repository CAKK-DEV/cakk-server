package com.cakk.admin.service

import com.cakk.admin.dto.request.CreateShopRequest
import com.cakk.admin.dto.request.PromotionRequest
import com.cakk.admin.dto.response.CakeShopCreateResponse
import com.cakk.admin.dto.response.CakeShopOwnerCandidateResponse
import com.cakk.admin.dto.response.CakeShopOwnerCandidatesResponse
import com.cakk.admin.mapper.*
import com.cakk.domain.mysql.bo.user.VerificationPolicy
import com.cakk.domain.mysql.entity.shop.CakeShop
import com.cakk.domain.mysql.entity.shop.CakeShopLink
import com.cakk.domain.mysql.entity.shop.CakeShopOperation
import com.cakk.domain.mysql.entity.user.BusinessInformation
import com.cakk.domain.mysql.entity.user.User
import com.cakk.domain.mysql.repository.reader.BusinessInformationReader
import com.cakk.domain.mysql.repository.reader.CakeShopReader
import com.cakk.domain.mysql.repository.reader.UserReader
import com.cakk.domain.mysql.repository.writer.CakeShopWriter
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class BusinessInformationService(
    private val businessInformationReader: BusinessInformationReader,
    private val userReader: UserReader,
    private val cakeShopWriter: CakeShopWriter,
    private val cakeShopReader: CakeShopReader,
    private val verificationPolicy: VerificationPolicy,
) {

    @Transactional
    fun createCakeShopByCertification(dto: CreateShopRequest): CakeShopCreateResponse {
        val cakeShop: CakeShop = supplyCakeShopBy(dto)
        val businessInformation: BusinessInformation = supplyBusinessInformationBy(dto.businessNumber, cakeShop)
        val cakeShopOperations: List<CakeShopOperation> = supplyCakeShopOperationsBy(cakeShop, dto.operationDays)
        val cakeShopLinks: List<CakeShopLink> = supplyCakeShopLinksBy(cakeShop, dto.links)

        val result: CakeShop = cakeShopWriter.createCakeShop(cakeShop, cakeShopOperations, businessInformation, cakeShopLinks)

        return supplyCakeShopCreateResponseBy(result)
    }

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
