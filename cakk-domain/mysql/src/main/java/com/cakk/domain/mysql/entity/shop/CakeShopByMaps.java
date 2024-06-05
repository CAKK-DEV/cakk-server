package com.cakk.domain.mysql.entity.shop;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.cakk.common.enums.ReturnCode;
import com.cakk.common.exception.CakkException;
import com.cakk.domain.mysql.dto.param.cake.CakeImageResponseParam;
import com.cakk.domain.mysql.dto.param.shop.CakeShopMapParam;

public class CakeShopByMaps {

	private List<CakeShopMapParam> cakeShopByMaps;

	public CakeShopByMaps(List<CakeShop> cakeShops, List<CakeImageResponseParam> cakes) {
		cakeShopByMaps = new ArrayList<>();
		initCakeShopByMaps(cakeShops);
		matchRepresentCakeImagesByMaxFour(cakes);
	}

	private void initCakeShopByMaps(List<CakeShop> cakeShops) {
		cakeShops.forEach(cakeShop -> cakeShopByMaps.add(
			CakeShopMapParam.builder()
				.cakeShopId(cakeShop.getId())
				.cakeShopName(cakeShop.getShopName())
				.cakeShopBio(cakeShop.getShopBio())
				.thumbnailUrl(cakeShop.getThumbnailUrl())
				.cakeImageUrls(new HashSet<>())
				.build()
		));
	}

	private void matchRepresentCakeImagesByMaxFour(List<CakeImageResponseParam> cakes) {
		Map<Long, CakeShopMapParam> map = new HashMap<>();

		for (CakeImageResponseParam cake : cakes) {
			CakeShopMapParam param;

			if (map.containsKey(cake.cakeShopId())) {
				param = map.get(cake.cakeShopId());
			} else {
				param = findCakeShop(cake.cakeShopId());
				map.put(cake.cakeShopId(), param);
			}

			if (isCakeImageUrlsCountLessThanFour(param)) {
				param.addCakeImageUrl(cake.cakeImageUrl());
			}
		}
	}

	private boolean isCakeImageUrlsCountLessThanFour(CakeShopMapParam param) {
		return param.getCakeImageUrls().size() < 4;
	}

	private CakeShopMapParam findCakeShop(Long cakeShopId) {
		return cakeShopByMaps.stream()
			.filter(cakeShopMapParam -> Objects.equals(cakeShopMapParam.getCakeShopId(), cakeShopId))
			.findFirst()
			.orElseThrow(() -> new CakkException(ReturnCode.NOT_EXIST_CAKE_SHOP));
	}

	public List<CakeShopMapParam> getCakeShopByMaps() {
		return cakeShopByMaps;
	}
}

