package com.cakk.admin.controller

import com.cakk.admin.dto.request.PromotionRequest
import com.cakk.admin.dto.response.CakeShopOwnerCandidateResponse
import com.cakk.admin.dto.response.CakeShopOwnerCandidatesResponse
import com.cakk.admin.service.BusinessInformationService
import com.cakk.common.response.ApiResponse
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/business-information")
class BusinessInformationController(
    private val businessInformationService: BusinessInformationService
) {

    @GetMapping("/candidates")
    fun getBusinessOwnerCandidates(): ApiResponse<CakeShopOwnerCandidatesResponse> {
        val response = businessInformationService.getBusinessOwnerCandidates()
        return ApiResponse.success(response)
    }

    @GetMapping("/candidates/{userId}")
    fun getCandidateSpecificationInformation(
        @PathVariable userId: Long
    ): ApiResponse<CakeShopOwnerCandidateResponse> {
        val response = businessInformationService.getCandidateInformation(userId)
        return ApiResponse.success(response)
    }

    @PutMapping("/promote")
    fun promoteUser(
        @RequestBody @Valid promotionRequest: PromotionRequest
    ): ApiResponse<Unit> {
        businessInformationService.promoteUserToBusinessOwner(promotionRequest)
        return ApiResponse.success()
    }
}
