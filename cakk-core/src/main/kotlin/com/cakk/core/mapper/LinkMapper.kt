package com.cakk.core.mapper

import com.cakk.common.enums.LinkKind
import com.cakk.core.dto.param.shop.ShopLinkParam
import com.cakk.infrastructure.persistence.entity.shop.CakeShop
import com.cakk.infrastructure.persistence.entity.shop.CakeShopLink

fun supplyCakeShopLinkByInstagram(instagram: String): com.cakk.infrastructure.persistence.entity.shop.CakeShopLink {
    return com.cakk.infrastructure.persistence.entity.shop.CakeShopLink.builder()
        .linkKind(LinkKind.INSTAGRAM)
        .linkPath(instagram)
        .build()
}

fun supplyCakeShopLinkByKakao(kakao: String): com.cakk.infrastructure.persistence.entity.shop.CakeShopLink {
    return com.cakk.infrastructure.persistence.entity.shop.CakeShopLink.builder()
        .linkKind(LinkKind.KAKAOTALK)
        .linkPath(kakao)
        .build()
}

fun supplyCakeShopLinkByWeb(web: String): com.cakk.infrastructure.persistence.entity.shop.CakeShopLink {
    return com.cakk.infrastructure.persistence.entity.shop.CakeShopLink.builder()
        .linkKind(LinkKind.WEB)
        .linkPath(web)
        .build()
}

fun supplyCakeShopLinksBy(cakeShop: com.cakk.infrastructure.persistence.entity.shop.CakeShop, links: List<ShopLinkParam>): List<com.cakk.infrastructure.persistence.entity.shop.CakeShopLink> {
    return links.map {
        when (it.linkKind) {
            LinkKind.WEB -> supplyCakeShopLinkByWeb(cakeShop, it.linkPath)
            LinkKind.INSTAGRAM -> supplyCakeShopLinkByInstagram(cakeShop, it.linkPath)
            LinkKind.KAKAOTALK -> supplyCakeShopLinkByKakao(cakeShop, it.linkPath)
        }
    }.toList()
}

private fun supplyCakeShopLinkByWeb(cakeShop: com.cakk.infrastructure.persistence.entity.shop.CakeShop, web: String): com.cakk.infrastructure.persistence.entity.shop.CakeShopLink {
    return com.cakk.infrastructure.persistence.entity.shop.CakeShopLink.builder()
        .linkKind(LinkKind.WEB)
        .linkPath(web)
        .cakeShop(cakeShop)
        .build()
}

private fun supplyCakeShopLinkByInstagram(cakeShop: com.cakk.infrastructure.persistence.entity.shop.CakeShop, instagram: String): com.cakk.infrastructure.persistence.entity.shop.CakeShopLink {
    return com.cakk.infrastructure.persistence.entity.shop.CakeShopLink.builder()
        .linkKind(LinkKind.INSTAGRAM)
        .linkPath(instagram)
        .cakeShop(cakeShop)
        .build()
}

private fun supplyCakeShopLinkByKakao(cakeShop: com.cakk.infrastructure.persistence.entity.shop.CakeShop, kakao: String): com.cakk.infrastructure.persistence.entity.shop.CakeShopLink {
    return com.cakk.infrastructure.persistence.entity.shop.CakeShopLink.builder()
        .linkKind(LinkKind.KAKAOTALK)
        .linkPath(kakao)
        .cakeShop(cakeShop)
        .build()
}
