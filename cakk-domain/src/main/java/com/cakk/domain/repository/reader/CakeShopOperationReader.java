package com.cakk.domain.repository.reader;

import java.util.List;

import lombok.RequiredArgsConstructor;

import com.cakk.domain.annotation.Reader;
import com.cakk.domain.entity.shop.CakeShopOperation;
import com.cakk.domain.repository.jpa.CakeShopOperationJpaRepository;

@Reader
@RequiredArgsConstructor
public class CakeShopOperationReader {

	private final CakeShopOperationJpaRepository cakeShopOperationJpaRepository;

	public List<CakeShopOperation> findAllByCakeShopId(Long cakeShopId) {
		return cakeShopOperationJpaRepository.findAllByCakeShopId(cakeShopId);
	}
}
