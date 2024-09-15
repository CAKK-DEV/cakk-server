package com.cakk.domain.mysql.bo.shop;

import java.util.List;

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
		for (T cakeShop : cakeShops) {
			if (cakeShop.getCakeImageUrls().size() > maxCount) {
				cakeShop.setImageCountMaxCount(maxCount);
			}
		}
	}

	private List<T> validationPageSize(List<T> cakeShops, int pageSize) {
		return cakeShops.stream().limit(pageSize).toList();
	}

	private void validationEmptyCakeImage(List<T> cakeShops) {
		for (T cakeShop : cakeShops) {
			if (cakeShop.getCakeImageUrls().contains("")) {
				cakeShop.setImagesEmptySet();
			}
		}
	}
}

