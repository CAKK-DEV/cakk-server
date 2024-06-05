package com.cakk.api.integration.search;

import static org.assertj.core.api.Assertions.*;

import java.util.stream.IntStream;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import com.cakk.api.common.annotation.TestWithDisplayName;
import com.cakk.api.common.base.IntegrationTest;
import com.cakk.api.dto.response.search.TopSearchedListResponse;
import com.cakk.common.enums.ReturnCode;
import com.cakk.common.response.ApiResponse;
import com.cakk.domain.redis.repository.KeywordRedisRepository;

public class KeywordIntegrationTest extends IntegrationTest {

	private static final String API_URL = "/api/v1/search";

	@Autowired
	private KeywordRedisRepository keywordRedisRepository;

	@BeforeEach
	void setUp() {
		IntStream.range(0, 10).forEach(i -> keywordRedisRepository.saveOrIncreaseSearchCount("1위 검색어"));
		IntStream.range(0, 8).forEach(i -> keywordRedisRepository.saveOrIncreaseSearchCount("2위 검색어"));
		IntStream.range(0, 6).forEach(i -> keywordRedisRepository.saveOrIncreaseSearchCount("3위 검색어"));
		IntStream.range(0, 4).forEach(i -> keywordRedisRepository.saveOrIncreaseSearchCount("4위 검색어"));
		IntStream.range(0, 3).forEach(i -> keywordRedisRepository.saveOrIncreaseSearchCount("5위 검색어"));
		IntStream.range(0, 2).forEach(i -> keywordRedisRepository.saveOrIncreaseSearchCount("6위 검색어"));
		IntStream.range(0, 1).forEach(i -> keywordRedisRepository.saveOrIncreaseSearchCount("7위 검색어"));
	}

	@TestWithDisplayName("인기 검색어를 조회한다.")
	void topSearched() {
		// given
		final String url = "%s%d%s/top-searched".formatted(BASE_URL, port, API_URL);
		final int count = 5;
		final UriComponents uriComponents = UriComponentsBuilder
			.fromUriString(url)
			.queryParam("count", count)
			.build();

		// when
		final ResponseEntity<ApiResponse> responseEntity = restTemplate.getForEntity(uriComponents.toUriString(), ApiResponse.class);

		// then
		final ApiResponse response = objectMapper.convertValue(responseEntity.getBody(), ApiResponse.class);
		final TopSearchedListResponse data = objectMapper.convertValue(response.getData(), TopSearchedListResponse.class);

		Assertions.assertEquals(HttpStatusCode.valueOf(200), responseEntity.getStatusCode());
		Assertions.assertEquals(ReturnCode.SUCCESS.getCode(), response.getReturnCode());
		Assertions.assertEquals(ReturnCode.SUCCESS.getMessage(), response.getReturnMessage());

		assertThat(data.keywordList()).hasSizeLessThanOrEqualTo(count);
		assertThat(data.keywordList().get(0)).isEqualTo("1위 검색어");
		assertThat(data.keywordList().get(1)).isEqualTo("2위 검색어");
		assertThat(data.keywordList().get(2)).isEqualTo("3위 검색어");
		assertThat(data.keywordList().get(3)).isEqualTo("4위 검색어");
		assertThat(data.keywordList().get(4)).isEqualTo("5위 검색어");
	}

	@TestWithDisplayName("검색 기록이 없을 경우, 인기 검색어 조회 시 빈 배열을 반환한다.")
	void topSearched2() {
		// given
		keywordRedisRepository.clear();

		final String url = "%s%d%s/top-searched".formatted(BASE_URL, port, API_URL);
		final int count = 10;
		final UriComponents uriComponents = UriComponentsBuilder
			.fromUriString(url)
			.queryParam("count", count)
			.build();

		// when
		final ResponseEntity<ApiResponse> responseEntity = restTemplate.getForEntity(uriComponents.toUriString(), ApiResponse.class);

		// then
		final ApiResponse response = objectMapper.convertValue(responseEntity.getBody(), ApiResponse.class);
		final TopSearchedListResponse data = objectMapper.convertValue(response.getData(), TopSearchedListResponse.class);

		Assertions.assertEquals(HttpStatusCode.valueOf(200), responseEntity.getStatusCode());
		Assertions.assertEquals(ReturnCode.SUCCESS.getCode(), response.getReturnCode());
		Assertions.assertEquals(ReturnCode.SUCCESS.getMessage(), response.getReturnMessage());

		assertThat(data.keywordList()).isEmpty();
	}
}
