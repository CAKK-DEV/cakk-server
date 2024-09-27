package com.cakk.api.dto.request.cake

import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min

import com.cakk.common.utils.DEFAULT_PAGE_SIZE

data class CakeSearchByLocationRequest(
    val cakeId: Long?,
    val keyword: String?,
    @field:Min(-90) @field:Max(90)
    val latitude: Double?,
    @field:Min(-180) @field:Max(180)
    val longitude: Double?,
    val pageSize: Int? = DEFAULT_PAGE_SIZE
)
