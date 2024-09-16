package com.cakk.domain.mysql.dto.param.cake;

import java.util.List;

import com.cakk.domain.mysql.entity.cake.Cake;
import com.cakk.domain.mysql.entity.cake.CakeCategory;
import com.cakk.domain.mysql.entity.user.User;

public record CakeCreateParam(
	Cake cake,
	List<CakeCategory> cakeCategories,
	List<String> tagNames,
	User owner,
	Long cakeShopId
) {
}
