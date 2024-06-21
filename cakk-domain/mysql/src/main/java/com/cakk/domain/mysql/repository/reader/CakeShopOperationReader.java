package com.cakk.domain.mysql.repository.reader;

import java.util.List;

import lombok.RequiredArgsConstructor;

import com.cakk.domain.mysql.annotation.Reader;
import com.cakk.domain.mysql.entity.shop.CakeShopOperation;
import com.cakk.domain.mysql.repository.jpa.CakeShopOperationJpaRepository;

@Reader
@RequiredArgsConstructor
public class CakeShopOperationReader {

	private final CakeShopOperationJpaRepository cakeShopOperationJpaRepository;

	public List<CakeShopOperation> findAllByCakeShopId(Long cakeShopId) {
		return cakeShopOperationJpaRepository.findAllByCakeShopId(cakeShopId);
	}
}
