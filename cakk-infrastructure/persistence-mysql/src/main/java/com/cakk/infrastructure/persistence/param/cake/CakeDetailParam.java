package com.cakk.infrastructure.persistence.param.cake;

import java.util.Set;

import com.cakk.common.enums.CakeDesignCategory;
import com.cakk.infrastructure.persistence.param.tag.TagParam;

public record CakeDetailParam(
	String cakeImageUrl,
	String cakeShopName,
	String shopBio,
	Long cakeShopId,
	Set<CakeDesignCategory> cakeCategories,
	Set<TagParam> tags
) {
}
