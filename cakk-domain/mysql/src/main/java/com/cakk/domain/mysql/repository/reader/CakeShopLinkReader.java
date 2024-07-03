package com.cakk.domain.mysql.repository.reader;

import java.util.List;

import lombok.RequiredArgsConstructor;

import com.cakk.domain.mysql.annotation.Reader;
import com.cakk.domain.mysql.entity.shop.CakeShopLink;
import com.cakk.domain.mysql.repository.jpa.CakeShopLinkJpaRepository;

@Reader
@RequiredArgsConstructor
public class CakeShopLinkReader {

	private final CakeShopLinkJpaRepository cakeShopLinkJpaRepository;

	public List<CakeShopLink> findAllByCakeShopId(final Long cakeShopId) {
		return cakeShopLinkJpaRepository.findAllByCakeShopId(cakeShopId);
	}
}
