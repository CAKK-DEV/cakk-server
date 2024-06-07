package com.cakk.domain.mysql.repository.reader;

import java.util.List;

import org.locationtech.jts.geom.Point;

import lombok.RequiredArgsConstructor;

import com.cakk.domain.mysql.annotation.Reader;
import com.cakk.domain.mysql.dto.param.cake.CakeImageResponseParam;
import com.cakk.domain.mysql.dto.param.cake.CakeSearchParam;
import com.cakk.domain.mysql.repository.query.CakeTagQueryRepository;

@Reader
@RequiredArgsConstructor
public class CakeTagReader {

	private final CakeTagQueryRepository cakeTagQueryRepository;

	public List<CakeImageResponseParam> searchCakeImagesByCursorAndSearchKeyword(CakeSearchParam param) {
		return cakeTagQueryRepository.searchCakeImagesByCursorAndSearchKeyword(
			param.cursorId(),
			param.keyword(),
			param.location(),
			param.pageSize()
		);
	}
}
