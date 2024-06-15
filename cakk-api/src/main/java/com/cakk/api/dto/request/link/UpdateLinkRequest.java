package com.cakk.api.dto.request.link;

import static java.util.Objects.*;

import java.util.ArrayList;
import java.util.List;

import com.cakk.api.mapper.LinkMapper;
import com.cakk.domain.mysql.dto.param.link.UpdateLinkParam;
import com.cakk.domain.mysql.entity.shop.CakeShopLink;
import com.cakk.domain.mysql.entity.user.User;


import jakarta.validation.constraints.Size;

public record UpdateLinkRequest(
	@Size(min = 1, max = 200)
	String instagram,
	@Size(min = 1, max = 200)
	String kakao,
	@Size(min = 1, max = 200)
	String web
) {

	public UpdateLinkParam toParam(User user, Long cakeShopId) {
		List<CakeShopLink> cakeShopLinks = new ArrayList<>();
		CakeShopLink instagramLink = LinkMapper.supplyCakeShopLinkByInstagram(instagram);
		CakeShopLink kakaoLink = LinkMapper.supplyCakeShopLinkByKakao(kakao);
		CakeShopLink webLink = LinkMapper.supplyCakeShopLinkByWeb(web);

		addLink(cakeShopLinks, instagramLink);
		addLink(cakeShopLinks, kakaoLink);
		addLink(cakeShopLinks, webLink);

		return new UpdateLinkParam(
			user,
			cakeShopId,
			cakeShopLinks
		);
	}

	private void addLink(List<CakeShopLink> cakeShopLinks, CakeShopLink cakeShopLink) {
		if (nonNull(cakeShopLink)) {
			cakeShopLinks.add(cakeShopLink);
		}
	}
}
