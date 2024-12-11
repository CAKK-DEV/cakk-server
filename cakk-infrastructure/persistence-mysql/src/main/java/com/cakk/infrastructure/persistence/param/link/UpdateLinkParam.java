package com.cakk.infrastructure.persistence.param.link;

import java.util.List;

import lombok.Builder;

import com.cakk.infrastructure.persistence.entity.shop.CakeShopLinkEntity;
import com.cakk.infrastructure.persistence.entity.user.UserEntity;

@Builder
public record UpdateLinkParam(
	UserEntity userEntity,
	Long cakeShopId,
	List<CakeShopLinkEntity> cakeShopLinks
) {
}
