package com.cakk.domain.mysql.facade.cake;

import java.util.List;

import com.cakk.domain.mysql.annotation.DomainFacade;
import com.cakk.domain.mysql.entity.cake.Cake;
import com.cakk.domain.mysql.entity.cake.CakeCategory;
import com.cakk.domain.mysql.entity.cake.Tag;
import com.cakk.domain.mysql.entity.shop.CakeShop;

@DomainFacade
public class CakeManagerFacade {

	public void createCake(CakeShop cakeShop, Cake cake, List<Tag> tags, List<CakeCategory> cakeCategories) {
		cake.registerTags(tags);
		cake.registerCategories(cakeCategories);
		cakeShop.registerCake(cake);
	}

	public void updateCake(Cake cake, String cakeImageUrl, List<Tag> tags, List<CakeCategory> cakeCategories) {
		cake.updateCakeImageUrl(cakeImageUrl);
		cake.updateCakeCategories(cakeCategories);
		cake.updateCakeTags(tags);
	}
}
