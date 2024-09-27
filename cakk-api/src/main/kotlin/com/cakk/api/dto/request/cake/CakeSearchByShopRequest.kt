package com.cakk.api.dto.request.cake

import jakarta.validation.constraints.NotNull

import com.cakk.common.utils.DEFAULT_PAGE_SIZE

data class CakeSearchByShopRequest(
    val cakeId: Long? = null,
    @field:NotNull
    val cakeShopId: Long?,
    val pageSize: Int? = DEFAULT_PAGE_SIZE
)
