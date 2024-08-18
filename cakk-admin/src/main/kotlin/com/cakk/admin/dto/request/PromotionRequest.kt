package com.cakk.admin.dto.request

import jakarta.validation.constraints.NotNull

data class PromotionRequest(
    @field:NotNull
    val userId: Long,
    @field:NotNull
    val cakeShopId: Long
)
