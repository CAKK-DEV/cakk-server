package com.cakk.api.service.search;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import com.cakk.api.common.annotation.TestWithDisplayName;
import com.cakk.api.common.base.ServiceTest;
import com.cakk.api.dto.response.search.TopSearchedListResponse;
import com.cakk.core.dto.param.search.TopSearchedListParam;
import com.cakk.domain.redis.repository.KeywordRedisRepository;

@DisplayName("키워드 관련 비즈니스 로직 테스트")
class KeywordServiceTest extends ServiceTest {

	@InjectMocks
	private KeywordService keywordService;

	@Mock
	private KeywordRedisRepository keywordRedisRepository;

	@TestWithDisplayName("인기 검색어 리스트를 조회한다.")
	void findTopSearched1() {
		// given
		final TopSearchedListParam param = getConstructorMonkey().giveMeOne(TopSearchedListParam.class);
		final List<String> keywordList = getConstructorMonkey().giveMe(String.class, 3);

		doReturn(keywordList).when(keywordRedisRepository).findTopSearchedLimitCount(param.getCount());

		// when
		final TopSearchedListResponse response = keywordService.findTopSearched(param);

		// then
		assertEquals(keywordList, response.keywordList());
		assertEquals(keywordList.size(), response.totalCount());

		verify(keywordRedisRepository, times(1)).findTopSearchedLimitCount(param.getCount());
	}

	@TestWithDisplayName("redis에 저장된 검색어가 없을 때, 인기 검색어 리스트를 조회 시 빈 배열을 반환한다.")
	void findTopSearched2() {
		// given
		final TopSearchedListParam param = getConstructorMonkey().giveMeOne(TopSearchedListParam.class);
		final List<String> keywordList = List.of();

		doReturn(keywordList).when(keywordRedisRepository).findTopSearchedLimitCount(param.getCount());

		// when
		final TopSearchedListResponse response = keywordService.findTopSearched(param);

		// then
		assertEquals(keywordList, response.keywordList());
		assertEquals(0, response.totalCount());

		verify(keywordRedisRepository, times(1)).findTopSearchedLimitCount(param.getCount());
	}
}
