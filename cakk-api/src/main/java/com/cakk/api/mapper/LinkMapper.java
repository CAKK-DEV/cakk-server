package com.cakk.api.mapper;


import java.util.List;
import java.util.stream.Collectors;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import com.cakk.api.dto.request.link.ShopLinkParam;
import com.cakk.common.enums.LinkKind;
import com.cakk.domain.mysql.entity.shop.CakeShop;
import com.cakk.domain.mysql.entity.shop.CakeShopLink;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LinkMapper {

	public static CakeShopLink supplyCakeShopLinkByInstagram(String instagram) {
		if (instagram == null) {
			return null;
		}
		return CakeShopLink.builder()
			.linkKind(LinkKind.INSTAGRAM)
			.linkPath(instagram)
			.build();
	}

	public static CakeShopLink supplyCakeShopLinkByKakao(String kakao) {
		if (kakao == null) {
			return null;
		}
		return CakeShopLink.builder()
			.linkKind(LinkKind.KAKAOTALK)
			.linkPath(kakao)
			.build();
	}

	public static CakeShopLink supplyCakeShopLinkByWeb(String web) {
		if (web == null) {
			return null;
		}
		return CakeShopLink.builder()
			.linkKind(LinkKind.WEB)
			.linkPath(web)
			.build();
	}

	public static CakeShopLink supplyCakeShopLinkByWeb(CakeShop cakeShop, String web) {
		return CakeShopLink.builder()
			.linkKind(LinkKind.WEB)
			.linkPath(web)
			.cakeShop(cakeShop)
			.build();
	}

	public static CakeShopLink supplyCakeShopLinkByInstagram(CakeShop cakeShop, String instagram) {
		return CakeShopLink.builder()
			.linkKind(LinkKind.INSTAGRAM)
			.linkPath(instagram)
			.cakeShop(cakeShop)
			.build();
	}

	public static CakeShopLink supplyCakeShopLinkByKakao(CakeShop cakeShop, String kakao) {
		return CakeShopLink.builder()
			.linkKind(LinkKind.KAKAOTALK)
			.linkPath(kakao)
			.cakeShop(cakeShop)
			.build();
	}

	public static List<CakeShopLink> supplyCakeShopLinksBy(CakeShop cakeShop, List<ShopLinkParam> links) {
		return links.stream().map(link -> {
			if (link.linkKind() == LinkKind.WEB) {
				return supplyCakeShopLinkByWeb(cakeShop, link.linkPath());
			} else if (link.linkKind() == LinkKind.INSTAGRAM) {
				return supplyCakeShopLinkByInstagram(cakeShop, link.linkPath());
			} else {
				return supplyCakeShopLinkByKakao(cakeShop, link.linkPath());
			}
		}).collect(Collectors.toList());
	}
}
