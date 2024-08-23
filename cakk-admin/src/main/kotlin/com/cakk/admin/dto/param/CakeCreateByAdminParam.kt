package com.cakk.admin.dto.param

import com.cakk.domain.mysql.entity.cake.Cake
import com.cakk.domain.mysql.entity.cake.CakeCategory

data class CakeCreateByAdminParam(
    val cake: Cake,
    val cakeCategories: List<CakeCategory>,
    val tagNames: List<String>,
    val cakeShopId: Long
)
