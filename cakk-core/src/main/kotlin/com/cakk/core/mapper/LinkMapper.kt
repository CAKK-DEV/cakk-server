package com.cakk.core.mapper

import com.cakk.common.enums.LinkKind
import com.cakk.core.dto.param.shop.ShopLinkParam
import com.cakk.infrastructure.persistence.entity.shop.CakeShopEntity
import com.cakk.infrastructure.persistence.entity.shop.CakeShopLinkEntity

fun supplyCakeShopLinkByInstagram(instagram: String): CakeShopLinkEntity {
    return CakeShopLinkEntity.builder()
        .linkKind(LinkKind.INSTAGRAM)
        .linkPath(instagram)
        .build()
}

fun supplyCakeShopLinkByKakao(kakao: String): CakeShopLinkEntity {
    return CakeShopLinkEntity.builder()
        .linkKind(LinkKind.KAKAOTALK)
        .linkPath(kakao)
        .build()
}

fun supplyCakeShopLinkByWeb(web: String): CakeShopLinkEntity {
    return CakeShopLinkEntity.builder()
        .linkKind(LinkKind.WEB)
        .linkPath(web)
        .build()
}

fun supplyCakeShopLinksBy(cakeShop: CakeShopEntity, links: List<ShopLinkParam>): List<CakeShopLinkEntity> {
    return links.map {
        when (it.linkKind) {
            LinkKind.WEB -> supplyCakeShopLinkByWeb(cakeShop, it.linkPath)
            LinkKind.INSTAGRAM -> supplyCakeShopLinkByInstagram(cakeShop, it.linkPath)
            LinkKind.KAKAOTALK -> supplyCakeShopLinkByKakao(cakeShop, it.linkPath)
        }
    }.toList()
}

private fun supplyCakeShopLinkByWeb(cakeShop: CakeShopEntity, web: String): CakeShopLinkEntity {
    return CakeShopLinkEntity.builder()
        .linkKind(LinkKind.WEB)
        .linkPath(web)
        .cakeShop(cakeShop)
        .build()
}

private fun supplyCakeShopLinkByInstagram(cakeShop: CakeShopEntity, instagram: String): CakeShopLinkEntity {
    return CakeShopLinkEntity.builder()
        .linkKind(LinkKind.INSTAGRAM)
        .linkPath(instagram)
        .cakeShop(cakeShop)
        .build()
}

private fun supplyCakeShopLinkByKakao(cakeShop: CakeShopEntity, kakao: String): CakeShopLinkEntity {
    return CakeShopLinkEntity.builder()
        .linkKind(LinkKind.KAKAOTALK)
        .linkPath(kakao)
        .cakeShop(cakeShop)
        .build()
}
