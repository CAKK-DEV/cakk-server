package com.cakk.admin.dto.param

import com.cakk.domain.mysql.entity.cake.CakeCategory

data class CakeUpdateByAdminParam(
    val cakeId: Long,
    val cakeImageUrl: String,
    val cakeCategories: List<CakeCategory>,
    val tagNames: List<String>
)
