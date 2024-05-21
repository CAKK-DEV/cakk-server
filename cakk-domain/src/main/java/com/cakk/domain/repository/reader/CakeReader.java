package com.cakk.domain.repository.reader;

import java.util.List;

import lombok.RequiredArgsConstructor;

import com.cakk.common.enums.CakeDesignCategory;
import com.cakk.domain.annotation.Reader;
import com.cakk.domain.dto.param.cake.CakeImageResponseParam;
import com.cakk.domain.repository.query.CakeQueryRepository;

@Reader
@RequiredArgsConstructor
public class CakeReader {

	private final CakeQueryRepository cakeQueryRepository;

	public List<CakeImageResponseParam> findCakeImagesByCursorAndCategory(Long cakeId, CakeDesignCategory category, int pageSize) {
		return cakeQueryRepository.findCakeImagesByCursorAndCategory(cakeId, category, pageSize);
	}
}
