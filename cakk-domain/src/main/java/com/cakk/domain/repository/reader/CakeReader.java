package com.cakk.domain.repository.reader;

import java.util.List;

import lombok.RequiredArgsConstructor;

import com.cakk.common.enums.CakeDesignCategory;
import com.cakk.common.enums.ReturnCode;
import com.cakk.common.exception.CakkException;
import com.cakk.domain.annotation.Reader;
import com.cakk.domain.dto.param.cake.CakeImageResponseParam;
import com.cakk.domain.entity.cake.Cake;
import com.cakk.domain.repository.jpa.CakeJpaRepository;
import com.cakk.domain.repository.query.CakeQueryRepository;

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
}