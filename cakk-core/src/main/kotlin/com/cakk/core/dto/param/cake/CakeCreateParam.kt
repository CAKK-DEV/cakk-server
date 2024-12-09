package com.cakk.core.dto.param.cake

import com.cakk.infrastructure.persistence.entity.cake.Cake
import com.cakk.infrastructure.persistence.entity.cake.CakeCategory
import com.cakk.infrastructure.persistence.entity.user.User


data class CakeCreateParam(
    val cake: com.cakk.infrastructure.persistence.entity.cake.Cake,
    val cakeCategories: List<com.cakk.infrastructure.persistence.entity.cake.CakeCategory>,
    val tagNames: List<String>,
    val owner: com.cakk.infrastructure.persistence.entity.user.User,
    val cakeShopId: Long
)
