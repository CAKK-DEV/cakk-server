package com.cakk.domain.mysql.dto.param.cake;

import java.util.List;

import com.cakk.domain.mysql.entity.cake.CakeCategory;
import com.cakk.domain.mysql.entity.user.User;

public record CakeUpdateParam(
	User owner,
	Long cakeId,
	String cakeImageUrl,
	List<CakeCategory> cakeCategories,
	List<String> tagNames
) {
}
