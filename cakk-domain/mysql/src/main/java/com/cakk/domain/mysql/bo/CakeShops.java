package com.cakk.domain.mysql.bo;

import java.util.List;
import java.util.stream.Collectors;

import com.cakk.domain.mysql.dto.param.shop.CakeShopParam;

public class CakeShops<T extends CakeShopParam> {

	private List<T> cakeShops;

	public CakeShops(List<T> cakeShops, int imageMaxCount) {
		validationEmptyCakeImage(cakeShops);
		validationImageCountMaxCount(cakeShops, imageMaxCount);
		this.cakeShops = cakeShops;
	}

	public CakeShops(List<T> cakeShops, int imageMaxCount, int pageSize) {
		validationImageCountMaxCount(cakeShops, imageMaxCount);
		cakeShops = validationPageSize(cakeShops, pageSize);
		this.cakeShops = cakeShops;
	}

	public List<T> getCakeShops() {
		return cakeShops;
	}

	private void validationImageCountMaxCount(List<T> cakeShops, final int maxCount) {
		cakeShops.forEach(cakeShop -> {
			if (cakeShop.getCakeImageUrls().size() > maxCount) {
				cakeShop.setImageCountMaxCount(maxCount);
			}
		});
	}

	private List<T> validationPageSize(List<T> cakeShops, int pageSize) {
		return cakeShops.stream().limit(pageSize).toList();
	}

	private void validationEmptyCakeImage(List<T> cakeShops) {
		cakeShops.forEach(cakeShop -> {
			if (cakeShop.getCakeImageUrls().contains("")) {
				cakeShop.setImagesEmptySet();
			}
		});
	}
}

