package com.cakk.api.mapper;

import static java.util.Objects.*;

import java.util.List;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import com.cakk.common.enums.LinkKind;
import com.cakk.core.dto.param.shop.ShopLinkParam;
import com.cakk.domain.mysql.entity.shop.CakeShop;
import com.cakk.domain.mysql.entity.shop.CakeShopLink;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LinkMapper {

	public static CakeShopLink supplyCakeShopLinkByInstagram(final String instagram) {
		if (isNull(instagram)) {
			return null;
		}
		return CakeShopLink.builder()
			.linkKind(LinkKind.INSTAGRAM)
			.linkPath(instagram)
			.build();
	}

	public static CakeShopLink supplyCakeShopLinkByKakao(final String kakao) {
		if (isNull(kakao)) {
			return null;
		}
		return CakeShopLink.builder()
			.linkKind(LinkKind.KAKAOTALK)
			.linkPath(kakao)
			.build();
	}

	public static CakeShopLink supplyCakeShopLinkByWeb(final String web) {
		if (isNull(web)) {
			return null;
		}
		return CakeShopLink.builder()
			.linkKind(LinkKind.WEB)
			.linkPath(web)
			.build();
	}

	public static List<CakeShopLink> supplyCakeShopLinksBy(final CakeShop cakeShop, final List<ShopLinkParam> links) {
		return links.stream().map(link -> {
			if (link.getLinkKind() == LinkKind.WEB) {
				return supplyCakeShopLinkByWeb(cakeShop, link.getLinkPath());
			} else if (link.getLinkKind() == LinkKind.INSTAGRAM) {
				return supplyCakeShopLinkByInstagram(cakeShop, link.getLinkPath());
			} else {
				return supplyCakeShopLinkByKakao(cakeShop, link.getLinkPath());
			}
		}).toList();
	}

	private static CakeShopLink supplyCakeShopLinkByWeb(final CakeShop cakeShop, final String web) {
		return CakeShopLink.builder()
			.linkKind(LinkKind.WEB)
			.linkPath(web)
			.cakeShop(cakeShop)
			.build();
	}

	private static CakeShopLink supplyCakeShopLinkByInstagram(final CakeShop cakeShop, final String instagram) {
		return CakeShopLink.builder()
			.linkKind(LinkKind.INSTAGRAM)
			.linkPath(instagram)
			.cakeShop(cakeShop)
			.build();
	}

	private static CakeShopLink supplyCakeShopLinkByKakao(final CakeShop cakeShop, final String kakao) {
		return CakeShopLink.builder()
			.linkKind(LinkKind.KAKAOTALK)
			.linkPath(kakao)
			.cakeShop(cakeShop)
			.build();
	}
}
