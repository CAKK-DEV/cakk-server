package com.cakk.domain.mysql.bo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.cakk.common.enums.ReturnCode;
import com.cakk.common.exception.CakkException;
import com.cakk.domain.mysql.dto.param.cake.CakeImageResponseParam;
import com.cakk.domain.mysql.dto.param.shop.CakeShopByKeywordParam;
import com.cakk.domain.mysql.dto.param.shop.CakeShopParam;
import com.cakk.domain.mysql.dto.param.shop.ShopOperationParam;
import com.cakk.domain.mysql.entity.shop.CakeShop;

public class CakeShops {

	private List<CakeShopParam> cakeShops;

	public CakeShops(List<CakeShop> cakeShops, List<CakeImageResponseParam> cakes) {
		Map<Long, CakeShopParam> map = new HashMap<>();
		this.cakeShops = new ArrayList<>();
		initCakeShopByLocationBased(cakeShops);
		matchRepresentCakeImagesByMaxFour(map, cakes);
	}

	public CakeShops(
		List<CakeShopByKeywordParam> cakeShops,
		List<ShopOperationParam> cakeShopOperations,
		List<CakeImageResponseParam> cakeImages
		) {
		Map<Long, CakeShopParam> map = new HashMap<>();
		this.cakeShops = new ArrayList<>();
		initCakeShopBySearchKeyword(cakeShops);
		matchRepresentCakeImagesByMaxFour(map, cakeImages);
		matchCakeShopOperations(map, cakeShopOperations);
	}

	public List<CakeShopParam> getCakeShops() {
		return cakeShops;
	}

	private void initCakeShopBySearchKeyword(List<CakeShopByKeywordParam> cakeShops) {
		cakeShops.forEach(cakeShop -> this.cakeShops.add(
			CakeShopParam.builder()
				.cakeShopId(cakeShop.cakeShopId())
				.cakeShopBio(cakeShop.cakeShopBio())
				.cakeShopName(cakeShop.cakeShopName())
				.thumbnailUrl(cakeShop.thumbnailUrl())
				.cakeImageUrls(new HashSet<>())
				.operationDays(new ArrayList<>())
				.build()
		));
	}

	private void initCakeShopByLocationBased(List<CakeShop> cakeShops) {
		cakeShops.forEach(cakeShop -> this.cakeShops.add(
			CakeShopParam.builder()
				.cakeShopId(cakeShop.getId())
				.cakeShopName(cakeShop.getShopName())
				.cakeShopBio(cakeShop.getShopBio())
				.thumbnailUrl(cakeShop.getThumbnailUrl())
				.cakeImageUrls(new HashSet<>())
				.operationDays(null)
				.build()
		));
	}

	private void matchRepresentCakeImagesByMaxFour(Map<Long, CakeShopParam> map, List<CakeImageResponseParam> cakes) {
		for (CakeImageResponseParam cake : cakes) {
			CakeShopParam param;

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

	private void matchCakeShopOperations(Map<Long, CakeShopParam> map, List<ShopOperationParam> cakeOperations) {
		for (ShopOperationParam cakeOperation : cakeOperations) {
			CakeShopParam param;

			if (map.containsKey(cakeOperation.cakeShopId())) {
				param = map.get(cakeOperation.cakeShopId());
			} else {
				param = findCakeShop(cakeOperation.cakeShopId());
				map.put(cakeOperation.cakeShopId(), param);
			}

			param.setOperationDay(cakeOperation);
		}
	}

	private boolean isCakeImageUrlsCountLessThanFour(CakeShopParam param) {
		return param.getCakeImageUrls().size() < 4;
	}

	private CakeShopParam findCakeShop(Long cakeShopId) {
		return cakeShops.stream()
			.filter(cakeShopParam -> Objects.equals(cakeShopParam.getCakeShopId(), cakeShopId))
			.findFirst()
			.orElseThrow(() -> new CakkException(ReturnCode.NOT_EXIST_CAKE_SHOP));
	}
}

