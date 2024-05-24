package com.cakk.domain.repository.reader;

import java.util.List;

import lombok.RequiredArgsConstructor;

import com.cakk.domain.annotation.Reader;
import com.cakk.domain.entity.shop.CakeShopLink;
import com.cakk.domain.repository.jpa.CakeShopLinkJpaRepository;

@Reader
@RequiredArgsConstructor
public class CakeShopLinkReader {

	private final CakeShopLinkJpaRepository cakeShopLinkJpaRepository;

	public List<CakeShopLink> findAllByCakeShopId(Long cakeShopId) {
		return cakeShopLinkJpaRepository.findAllByCakeShopId(cakeShopId);
	}
}
