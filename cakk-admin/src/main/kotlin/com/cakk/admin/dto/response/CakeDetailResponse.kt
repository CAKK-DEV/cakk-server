package com.cakk.admin.dto.response

import com.cakk.common.enums.CakeDesignCategory

data class CakeDetailResponse(
    val cakeShopId: Long,
    val cakeImageUrl: String,
    val cakeShopName: String,
    val shopBio: String,
    val cakeCategories: MutableSet<CakeDesignCategory>,
    val tags: MutableSet<TagResponse>
)
