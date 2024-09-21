package com.cakk.core.dto.param.cake

import com.cakk.domain.mysql.entity.cake.Cake
import com.cakk.domain.mysql.entity.cake.CakeCategory
import com.cakk.domain.mysql.entity.user.User


data class CakeCreateParam(
    val cake: Cake,
    val cakeCategories: List<CakeCategory>,
    val tagNames: List<String>,
    val owner: User,
    val cakeShopId: Long
)
