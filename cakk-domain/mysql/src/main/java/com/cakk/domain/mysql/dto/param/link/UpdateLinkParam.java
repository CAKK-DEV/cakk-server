package com.cakk.domain.mysql.dto.param.link;

import java.util.List;

import com.cakk.domain.mysql.entity.shop.CakeShopLink;
import com.cakk.domain.mysql.entity.user.User;

public record UpdateLinkParam(
	User user,
	Long cakeShopId,
	List<CakeShopLink> cakeShopLinks
) {
}
