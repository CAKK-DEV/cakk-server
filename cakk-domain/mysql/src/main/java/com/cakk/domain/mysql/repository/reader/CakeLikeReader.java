package com.cakk.domain.mysql.repository.reader;

import java.util.List;

import lombok.RequiredArgsConstructor;

import com.cakk.domain.mysql.annotation.Reader;
import com.cakk.domain.mysql.dto.param.like.LikeCakeImageResponseParam;
import com.cakk.domain.mysql.repository.query.CakeLikeQueryRepository;

@RequiredArgsConstructor
@Reader
public class CakeLikeReader {

	private final CakeLikeQueryRepository cakeLikeQueryRepository;

	public List<LikeCakeImageResponseParam> searchCakeImagesByCursorAndLike(Long cakeLikeId, Long userId, int pageSize) {
		return cakeLikeQueryRepository.searchCakeImagesByCursorAndLike(cakeLikeId, userId, pageSize);
	}
}
