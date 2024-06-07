package com.cakk.domain.mysql.repository.reader;

import java.util.List;

import lombok.RequiredArgsConstructor;

import com.cakk.common.enums.CakeDesignCategory;
import com.cakk.common.enums.ReturnCode;
import com.cakk.common.exception.CakkException;
import com.cakk.domain.mysql.annotation.Reader;
import com.cakk.domain.mysql.dto.param.cake.CakeImageResponseParam;
import com.cakk.domain.mysql.dto.param.cake.CakeSearchParam;
import com.cakk.domain.mysql.entity.cake.Cake;
import com.cakk.domain.mysql.entity.shop.CakeShop;
import com.cakk.domain.mysql.repository.jpa.CakeJpaRepository;
import com.cakk.domain.mysql.repository.query.CakeQueryRepository;

@Reader
@RequiredArgsConstructor
public class CakeReader {

	private final CakeJpaRepository cakeJpaRepository;
	private final CakeQueryRepository cakeQueryRepository;

	public Cake findById(Long cakeId) {
		return cakeJpaRepository.findById(cakeId).orElseThrow(() -> new CakkException(ReturnCode.NOT_EXIST_CAKE));
	}

	public List<CakeImageResponseParam> searchCakeImagesByCursorAndCategory(Long cakeId, CakeDesignCategory category, int pageSize) {
		return cakeQueryRepository.searchCakeImagesByCursorAndCategory(cakeId, category, pageSize);
	}

	public List<CakeImageResponseParam> searchCakeImagesByCursorAndCakeShopId(Long cakeId, Long cakeShopId, int pageSize) {
		return cakeQueryRepository.searchCakeImagesByCursorAndCakeShopId(cakeId, cakeShopId, pageSize);
	}

	public List<CakeImageResponseParam> searchCakeImagesByCakeShops(List<Long> cakeShopIds) {
		return cakeQueryRepository.searchCakeImagesByCakeShopIds(cakeShopIds);
	}

	public List<CakeImageResponseParam> searchCakeImagesByCursorAndSearchKeyword(CakeSearchParam param) {
		return cakeQueryRepository.searchCakeImagesByCursorAndSearchKeyword(
			param.cursorId(),
			param.keyword(),
			param.location(),
			param.pageSize()
		);
	}
}
