package com.cakk.infrastructure.persistence.param.link;

import java.util.List;

import lombok.Builder;

import com.cakk.infrastructure.persistence.entity.shop.CakeShopLink;
import com.cakk.infrastructure.persistence.entity.user.User;

@Builder
public record UpdateLinkParam(
	User user,
	Long cakeShopId,
	List<CakeShopLink> cakeShopLinks
) {
}
