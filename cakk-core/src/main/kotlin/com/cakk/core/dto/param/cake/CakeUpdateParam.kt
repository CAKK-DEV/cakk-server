package com.cakk.core.dto.param.cake

import com.cakk.infrastructure.persistence.entity.cake.CakeCategory
import com.cakk.infrastructure.persistence.entity.user.User


data class CakeUpdateParam(
    val owner: com.cakk.infrastructure.persistence.entity.user.User,
    val cakeId: Long,
    val cakeImageUrl: String,
    val cakeCategories: List<com.cakk.infrastructure.persistence.entity.cake.CakeCategory>,
    val tagNames: List<String>
)
