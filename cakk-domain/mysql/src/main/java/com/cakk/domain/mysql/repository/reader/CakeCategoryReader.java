package com.cakk.domain.mysql.repository.reader;

import lombok.RequiredArgsConstructor;

import com.cakk.common.enums.ReturnCode;
import com.cakk.common.exception.CakkException;
import com.cakk.domain.mysql.annotation.Reader;
import com.cakk.domain.mysql.entity.cake.CakeCategory;
import com.cakk.domain.mysql.repository.jpa.CakeCategoryJpaRepository;

@Reader
@RequiredArgsConstructor
public class CakeCategoryReader {

	private final CakeCategoryJpaRepository cakeCategoryJpaRepository;

	public CakeCategory findByCakeId(final Long cakeId) {
		return cakeCategoryJpaRepository.findByCakeId(cakeId).orElseThrow(() -> new CakkException(ReturnCode.NOT_EXIST_CAKE_CATEGORY));
	}
}
