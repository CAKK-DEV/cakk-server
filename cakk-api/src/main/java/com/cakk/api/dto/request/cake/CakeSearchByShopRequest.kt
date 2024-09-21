package com.cakk.api.dto.request.cake

import jakarta.validation.constraints.NotNull

data class CakeSearchByShopRequest(
    val cakeId: Long? = null,
    @field:NotNull
    val cakeShopId: Long?,
    @field:NotNull
    val pageSize: Int? = 10
)
