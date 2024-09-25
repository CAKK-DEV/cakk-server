package com.cakk.core.mapper

import com.cakk.common.enums.LinkKind
import com.cakk.core.dto.param.shop.ShopLinkParam
import com.cakk.domain.mysql.entity.shop.CakeShop
import com.cakk.domain.mysql.entity.shop.CakeShopLink

fun supplyCakeShopLinkByInstagram(instagram: String): CakeShopLink {
    return CakeShopLink.builder()
        .linkKind(LinkKind.INSTAGRAM)
        .linkPath(instagram)
        .build()
}

fun supplyCakeShopLinkByKakao(kakao: String): CakeShopLink {
    return CakeShopLink.builder()
        .linkKind(LinkKind.KAKAOTALK)
        .linkPath(kakao)
        .build()
}

fun supplyCakeShopLinkByWeb(web: String): CakeShopLink {
    return CakeShopLink.builder()
        .linkKind(LinkKind.WEB)
        .linkPath(web)
        .build()
}

fun supplyCakeShopLinksBy(cakeShop: CakeShop, links: List<ShopLinkParam>): List<CakeShopLink> {
    return links.map {
        when (it.linkKind) {
            LinkKind.WEB -> supplyCakeShopLinkByWeb(cakeShop, it.linkPath)
            LinkKind.INSTAGRAM -> supplyCakeShopLinkByInstagram(cakeShop, it.linkPath)
            LinkKind.KAKAOTALK -> supplyCakeShopLinkByKakao(cakeShop, it.linkPath)
        }
    }.toList()
}

private fun supplyCakeShopLinkByWeb(cakeShop: CakeShop, web: String): CakeShopLink {
    return CakeShopLink.builder()
        .linkKind(LinkKind.WEB)
        .linkPath(web)
        .cakeShop(cakeShop)
        .build()
}

private fun supplyCakeShopLinkByInstagram(cakeShop: CakeShop, instagram: String): CakeShopLink {
    return CakeShopLink.builder()
        .linkKind(LinkKind.INSTAGRAM)
        .linkPath(instagram)
        .cakeShop(cakeShop)
        .build()
}

private fun supplyCakeShopLinkByKakao(cakeShop: CakeShop, kakao: String): CakeShopLink {
    return CakeShopLink.builder()
        .linkKind(LinkKind.KAKAOTALK)
        .linkPath(kakao)
        .cakeShop(cakeShop)
        .build()
}
