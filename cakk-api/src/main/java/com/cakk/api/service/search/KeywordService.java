package com.cakk.api.service.search;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

import com.cakk.api.dto.request.search.TopSearchedListRequest;
import com.cakk.api.dto.response.search.TopSearchedListResponse;
import com.cakk.api.mapper.SearchMapper;
import com.cakk.core.dto.param.search.TopSearchedListParam;
import com.cakk.domain.redis.repository.KeywordRedisRepository;

@RequiredArgsConstructor
@Service
public class KeywordService {

	private final KeywordRedisRepository keywordRedisRepository;

	public TopSearchedListResponse findTopSearched(final TopSearchedListParam param) {
		final List<String> keywordList = keywordRedisRepository.findTopSearchedLimitCount(param.getCount());

		return SearchMapper.supplyTopSearchedListResponseBy(keywordList);
	}
}
