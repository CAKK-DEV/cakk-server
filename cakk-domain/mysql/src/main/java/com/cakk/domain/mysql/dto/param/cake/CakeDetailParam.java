package com.cakk.domain.mysql.dto.param.cake;

import java.util.Set;

import com.cakk.common.enums.CakeDesignCategory;
import com.cakk.domain.mysql.dto.param.tag.TagParam;

public record CakeDetailParam(
	String cakeImageUrl,
	String cakeShopName,
	String shopBio,
	Long cakeShopId,
	Set<CakeDesignCategory> cakeCategories,
	Set<TagParam> tags
) {
}
