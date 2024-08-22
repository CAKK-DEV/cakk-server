package com.cakk.admin.mapper

import com.cakk.domain.mysql.entity.cake.Cake

fun supplyCakeBy(cakeImageUrl: String): Cake {
    return Cake.builder()
        .cakeImageUrl(cakeImageUrl)
        .build()
}
