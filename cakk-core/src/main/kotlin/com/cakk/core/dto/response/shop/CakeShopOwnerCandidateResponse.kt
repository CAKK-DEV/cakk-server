package com.cakk.core.dto.response.shop

data class CakeShopOwnerCandidateResponse(
    val userId: Long,
    val cakeShopId: Long,
    val email: String,
    val businessRegistrationImageUrl: String,
    val idCardImageUrl: String,
    val emergencyContact: String
)
