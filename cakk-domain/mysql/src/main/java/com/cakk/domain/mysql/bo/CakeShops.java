package com.cakk.domain.mysql.bo;

import java.util.List;
import java.util.stream.Collectors;

import com.cakk.domain.mysql.dto.param.shop.CakeShopParam;

public class CakeShops<T extends CakeShopParam> {

	private List<T> cakeShops;

	public CakeShops(List<T> cakeShops) {
		validationImageCountMaxFour(cakeShops);
		this.cakeShops = cakeShops;
	}

	public CakeShops(List<T> cakeShops, int pageSize) {
		validationImageCountMaxFour(cakeShops);
		cakeShops = validationPageSize(cakeShops, pageSize);
		this.cakeShops = cakeShops;
	}

	public List<T> getCakeShops() {
		return cakeShops;
	}

	private void validationImageCountMaxFour(List<T> cakeShops) {
		cakeShops.forEach(cakeShop -> {
			if (cakeShop.getCakeImageUrls().size() > 4) {
				cakeShop.setImageCountMaxFour();
			}
		});
	}

	private List<T> validationPageSize(List<T> cakeShops, int pageSize) {
		return cakeShops.stream().limit(pageSize).collect(Collectors.toList());
	}
}

