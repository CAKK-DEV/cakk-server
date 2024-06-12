package com.cakk.domain.mysql.repository.reader;

import java.util.List;

import lombok.RequiredArgsConstructor;

import com.cakk.domain.mysql.annotation.Reader;
import com.cakk.domain.mysql.dto.param.shop.ShopOperationParam;
import com.cakk.domain.mysql.entity.shop.CakeShopOperation;
import com.cakk.domain.mysql.repository.jpa.CakeShopOperationJpaRepository;
import com.cakk.domain.mysql.repository.query.CakeShopOperationQueryRepository;

@Reader
@RequiredArgsConstructor
public class CakeShopOperationReader {

	private final CakeShopOperationJpaRepository cakeShopOperationJpaRepository;
	private final CakeShopOperationQueryRepository cakeShopOperationQueryRepository;

	public List<CakeShopOperation> findAllByCakeShopId(Long cakeShopId) {
		return cakeShopOperationJpaRepository.findAllByCakeShopId(cakeShopId);
	}

	public List<ShopOperationParam> searchShopOperationsByCakeShops(List<Long> cakeShopIds) {
		return cakeShopOperationQueryRepository.findByCakeShopIds(cakeShopIds);
	}
}
