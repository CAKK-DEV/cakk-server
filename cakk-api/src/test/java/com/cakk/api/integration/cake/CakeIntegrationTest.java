package com.cakk.api.integration.cake;

import static org.assertj.core.api.Assertions.*;
import static org.junit.Assert.*;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.*;

import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import com.cakk.api.common.annotation.TestWithDisplayName;
import com.cakk.api.common.base.IntegrationTest;
import com.cakk.api.dto.request.cake.CakeCreateRequest;
import com.cakk.api.dto.request.cake.CakeUpdateRequest;
import com.cakk.api.dto.response.cake.CakeDetailResponse;
import com.cakk.api.dto.response.cake.CakeImageListResponse;
import com.cakk.api.dto.response.like.HeartResponse;
import com.cakk.common.enums.CakeDesignCategory;
import com.cakk.common.enums.ReturnCode;
import com.cakk.common.response.ApiResponse;
import com.cakk.domain.mysql.dto.param.cake.CakeImageResponseParam;
import com.cakk.domain.mysql.entity.cake.CakeCategory;
import com.cakk.domain.mysql.repository.reader.CakeCategoryReader;
import com.cakk.domain.redis.repository.CakeViewsRedisRepository;

@SqlGroup({
	@Sql(scripts = {
		"/sql/insert-test-user.sql",
		"/sql/insert-cake.sql",
		"/sql/insert-heart.sql"
	}, executionPhase = BEFORE_TEST_METHOD),
	@Sql(scripts = "/sql/delete-all.sql", executionPhase = AFTER_TEST_METHOD)
})
class CakeIntegrationTest extends IntegrationTest {

	private static final String API_URL = "/api/v1/cakes";

	@Autowired
	private CakeCategoryReader cakeCategoryReader;

	@Autowired
	private CakeViewsRedisRepository cakeViewsRedisRepository;

	private void initializeViews() {
		cakeViewsRedisRepository.saveOrIncreaseSearchCount(1L);
		cakeViewsRedisRepository.saveOrIncreaseSearchCount(1L);
		cakeViewsRedisRepository.saveOrIncreaseSearchCount(1L);
		cakeViewsRedisRepository.saveOrIncreaseSearchCount(1L);
		cakeViewsRedisRepository.saveOrIncreaseSearchCount(2L);
		cakeViewsRedisRepository.saveOrIncreaseSearchCount(2L);
		cakeViewsRedisRepository.saveOrIncreaseSearchCount(2L);
		cakeViewsRedisRepository.saveOrIncreaseSearchCount(3L);
		cakeViewsRedisRepository.saveOrIncreaseSearchCount(3L);
		cakeViewsRedisRepository.saveOrIncreaseSearchCount(4L);
		cakeViewsRedisRepository.saveOrIncreaseSearchCount(5L);
		cakeViewsRedisRepository.saveOrIncreaseSearchCount(6L);
		cakeViewsRedisRepository.saveOrIncreaseSearchCount(7L);
		cakeViewsRedisRepository.saveOrIncreaseSearchCount(7L);
		cakeViewsRedisRepository.saveOrIncreaseSearchCount(1L);
		cakeViewsRedisRepository.saveOrIncreaseSearchCount(7L);
		cakeViewsRedisRepository.saveOrIncreaseSearchCount(8L);
		cakeViewsRedisRepository.saveOrIncreaseSearchCount(9L);
	}

	@AfterEach
	void setUp() {
		cakeViewsRedisRepository.clear();
	}

	@TestWithDisplayName("카테고리로 첫 페이지 케이크 이미지 조회에 성공한다")
	void searchByCategory1() {
		// given
		final String url = "%s%d%s/search/categories".formatted(BASE_URL, port, API_URL);
		final UriComponents uriComponents = UriComponentsBuilder
			.fromUriString(url)
			.queryParam("category", CakeDesignCategory.FLOWER)
			.queryParam("pageSize", 5)
			.build();

		// when
		final ResponseEntity<ApiResponse> responseEntity = restTemplate.getForEntity(uriComponents.toUriString(), ApiResponse.class);

		// then
		final ApiResponse response = objectMapper.convertValue(responseEntity.getBody(), ApiResponse.class);
		final CakeImageListResponse data = objectMapper.convertValue(response.getData(), CakeImageListResponse.class);

		assertEquals(HttpStatusCode.valueOf(200), responseEntity.getStatusCode());
		assertEquals(ReturnCode.SUCCESS.getCode(), response.getReturnCode());
		assertEquals(ReturnCode.SUCCESS.getMessage(), response.getReturnMessage());

		Long lastCakeId = data.cakeImages().stream().map(CakeImageResponseParam::cakeId).min(Long::compareTo).orElse(null);
		assertEquals(lastCakeId, data.lastCakeId());
		assertEquals(5, data.size());
		data.cakeImages().forEach(cakeImage -> {
			CakeCategory cakeCategory = cakeCategoryReader.findByCakeId(cakeImage.cakeId);
			assertEquals(CakeDesignCategory.FLOWER, cakeCategory.getCakeDesignCategory());
		});
	}

	@TestWithDisplayName("카테고리로 첫 페이지가 아닌 케이크 이미지 조회에 성공한다")
	void searchByCategory2() {
		// given
		final String url = "%s%d%s/search/categories".formatted(BASE_URL, port, API_URL);
		final UriComponents uriComponents = UriComponentsBuilder
			.fromUriString(url)
			.queryParam("cakeId", 6)
			.queryParam("category", CakeDesignCategory.FLOWER)
			.queryParam("pageSize", 5)
			.build();

		// when
		final ResponseEntity<ApiResponse> responseEntity = restTemplate.getForEntity(uriComponents.toUriString(), ApiResponse.class);

		// then
		final ApiResponse response = objectMapper.convertValue(responseEntity.getBody(), ApiResponse.class);
		final CakeImageListResponse data = objectMapper.convertValue(response.getData(), CakeImageListResponse.class);

		assertEquals(HttpStatusCode.valueOf(200), responseEntity.getStatusCode());
		assertEquals(ReturnCode.SUCCESS.getCode(), response.getReturnCode());
		assertEquals(ReturnCode.SUCCESS.getMessage(), response.getReturnMessage());

		Long lastCakeId = data.cakeImages().stream().map(CakeImageResponseParam::cakeId).min(Long::compareTo).orElse(null);
		assertEquals(lastCakeId, data.lastCakeId());
		assertEquals(5, data.size());
		data.cakeImages().forEach(cakeImage -> {
			CakeCategory cakeCategory = cakeCategoryReader.findByCakeId(cakeImage.cakeId);
			assertEquals(CakeDesignCategory.FLOWER, cakeCategory.getCakeDesignCategory());
		});
	}

	@TestWithDisplayName("카테고리로 케이크 이미지 조회 시 데이터가 없으면 빈 배열을 반환한다")
	void searchByCategory3() {
		// given
		final String url = "%s%d%s/search/categories".formatted(BASE_URL, port, API_URL);
		final UriComponents uriComponents = UriComponentsBuilder
			.fromUriString(url)
			.queryParam("cakeId", 1)
			.queryParam("category", CakeDesignCategory.FLOWER)
			.queryParam("pageSize", 5)
			.build();

		// when
		final ResponseEntity<ApiResponse> responseEntity = restTemplate.getForEntity(uriComponents.toUriString(), ApiResponse.class);

		// then
		final ApiResponse response = objectMapper.convertValue(responseEntity.getBody(), ApiResponse.class);
		final CakeImageListResponse data = objectMapper.convertValue(response.getData(), CakeImageListResponse.class);

		assertEquals(HttpStatusCode.valueOf(200), responseEntity.getStatusCode());
		assertEquals(ReturnCode.SUCCESS.getCode(), response.getReturnCode());
		assertEquals(ReturnCode.SUCCESS.getMessage(), response.getReturnMessage());

		assertEquals(0, data.cakeImages().size());
		assertNull(data.lastCakeId());
		assertEquals(0, data.size());
	}

	@TestWithDisplayName("케이크 샵으로 첫 페이지 케이크 이미지 조회에 성공한다")
	void searchByShopId1() {
		// given
		final String url = "%s%d%s/search/shops".formatted(BASE_URL, port, API_URL);
		final UriComponents uriComponents = UriComponentsBuilder
			.fromUriString(url)
			.queryParam("cakeShopId", 1L)
			.queryParam("pageSize", 4)
			.build();

		// when
		final ResponseEntity<ApiResponse> responseEntity = restTemplate.getForEntity(uriComponents.toUriString(), ApiResponse.class);

		// then
		final ApiResponse response = objectMapper.convertValue(responseEntity.getBody(), ApiResponse.class);
		final CakeImageListResponse data = objectMapper.convertValue(response.getData(), CakeImageListResponse.class);

		assertEquals(HttpStatusCode.valueOf(200), responseEntity.getStatusCode());
		assertEquals(ReturnCode.SUCCESS.getCode(), response.getReturnCode());
		assertEquals(ReturnCode.SUCCESS.getMessage(), response.getReturnMessage());

		Long lastCakeId = data.cakeImages().stream().map(CakeImageResponseParam::cakeId).min(Long::compareTo).orElse(null);
		assertEquals(lastCakeId, data.lastCakeId());
		assertEquals(3, data.size());
		data.cakeImages().forEach(cakeImage -> {
			assertEquals(Long.valueOf(1L), cakeImage.cakeShopId);
		});
	}

	@TestWithDisplayName("케이크 샵으로 첫 페이지가 아닌 케이크 이미지 조회에 성공한다")
	void searchByShopId2() {
		// given
		final String url = "%s%d%s/search/shops".formatted(BASE_URL, port, API_URL);
		final UriComponents uriComponents = UriComponentsBuilder
			.fromUriString(url)
			.queryParam("cakeId", 5)
			.queryParam("cakeShopId", 1L)
			.queryParam("pageSize", 4)
			.build();

		// when
		final ResponseEntity<ApiResponse> responseEntity = restTemplate.getForEntity(uriComponents.toUriString(), ApiResponse.class);

		// then
		final ApiResponse response = objectMapper.convertValue(responseEntity.getBody(), ApiResponse.class);
		final CakeImageListResponse data = objectMapper.convertValue(response.getData(), CakeImageListResponse.class);

		assertEquals(HttpStatusCode.valueOf(200), responseEntity.getStatusCode());
		assertEquals(ReturnCode.SUCCESS.getCode(), response.getReturnCode());
		assertEquals(ReturnCode.SUCCESS.getMessage(), response.getReturnMessage());

		Long lastCakeId = data.cakeImages().stream().map(CakeImageResponseParam::cakeId).min(Long::compareTo).orElse(null);
		assertEquals(lastCakeId, data.lastCakeId());
		assertEquals(3, data.size());
		data.cakeImages().forEach(cakeImage ->
			assertEquals(Long.valueOf(1L), cakeImage.cakeShopId)
		);
	}

	@TestWithDisplayName("케이크 샵으로 케이크 이미지 조회 시 데이터가 없으면 빈 배열을 반환한다")
	void searchByShopId3() {
		// given
		final String url = "%s%d%s/search/shops".formatted(BASE_URL, port, API_URL);
		final UriComponents uriComponents = UriComponentsBuilder
			.fromUriString(url)
			.queryParam("cakeId", 1)
			.queryParam("cakeShopId", 1L)
			.queryParam("pageSize", 4)
			.build();

		// when
		final ResponseEntity<ApiResponse> responseEntity = restTemplate.getForEntity(uriComponents.toUriString(), ApiResponse.class);

		// then
		final ApiResponse response = objectMapper.convertValue(responseEntity.getBody(), ApiResponse.class);
		final CakeImageListResponse data = objectMapper.convertValue(response.getData(), CakeImageListResponse.class);

		assertEquals(HttpStatusCode.valueOf(200), responseEntity.getStatusCode());
		assertEquals(ReturnCode.SUCCESS.getCode(), response.getReturnCode());
		assertEquals(ReturnCode.SUCCESS.getMessage(), response.getReturnMessage());

		assertEquals(0, data.cakeImages().size());
		assertNull(data.lastCakeId());
		assertEquals(0, data.size());
	}

	@TestWithDisplayName("검색어, 태그명, 케이크 카테고리, 사용자 위치를 포함한 동적 검색에 성공한다")
	void searchCakeImagesByKeywordAndLocation1() {
		final String url = "%s%d%s/search/cakes".formatted(BASE_URL, port, API_URL);
		final UriComponents uriComponents = UriComponentsBuilder
			.fromUriString(url)
			.queryParam("keyword", "tag")
			.queryParam("latitude", 37.2096575)
			.queryParam("longitude", 127.0998228)
			.queryParam("pageSize", 10)
			.build();

		// when
		final ResponseEntity<ApiResponse> responseEntity = restTemplate.getForEntity(uriComponents.toUriString(), ApiResponse.class);

		// then
		final ApiResponse response = objectMapper.convertValue(responseEntity.getBody(), ApiResponse.class);
		final CakeImageListResponse data = objectMapper.convertValue(response.getData(), CakeImageListResponse.class);

		assertEquals(HttpStatusCode.valueOf(200), responseEntity.getStatusCode());
		assertEquals(ReturnCode.SUCCESS.getCode(), response.getReturnCode());
		assertEquals(ReturnCode.SUCCESS.getMessage(), response.getReturnMessage());
		assertThat(data.cakeImages().size()).isNotNegative();
	}

	@TestWithDisplayName("검색어, 태그명, 케이크 카테고리, 사용자 위치를 포함한 동적 검색에 성공한다")
	void searchCakeImagesByKeywordAndLocation2() {
		final String url = "%s%d%s/search/cakes".formatted(BASE_URL, port, API_URL);
		final UriComponents uriComponents = UriComponentsBuilder
			.fromUriString(url)
			.queryParam("keyword", "tag")
			.queryParam("latitude", 37.543343)
			.queryParam("longitude", 127.052609)
			.queryParam("pageSize", 10)
			.build();

		// when
		final ResponseEntity<ApiResponse> responseEntity = restTemplate.getForEntity(uriComponents.toUriString(), ApiResponse.class);

		// then
		final ApiResponse response = objectMapper.convertValue(responseEntity.getBody(), ApiResponse.class);
		final CakeImageListResponse data = objectMapper.convertValue(response.getData(), CakeImageListResponse.class);

		assertEquals(HttpStatusCode.valueOf(200), responseEntity.getStatusCode());
		assertEquals(ReturnCode.SUCCESS.getCode(), response.getReturnCode());
		assertEquals(ReturnCode.SUCCESS.getMessage(), response.getReturnMessage());
		assertThat(data.cakeImages().size()).isNotNegative();
	}

	@TestWithDisplayName("사용자 위치를 포함한 동적 검색에 성공한다")
	void searchCakeImagesByKeywordAndLocation3() {
		final String url = "%s%d%s/search/cakes".formatted(BASE_URL, port, API_URL);
		final UriComponents uriComponents = UriComponentsBuilder
			.fromUriString(url)
			.queryParam("latitude", 37.2096575)
			.queryParam("longitude", 127.0998228)
			.queryParam("pageSize", 10)
			.build();

		// when
		final ResponseEntity<ApiResponse> responseEntity = restTemplate.getForEntity(uriComponents.toUriString(), ApiResponse.class);

		// then
		final ApiResponse response = objectMapper.convertValue(responseEntity.getBody(), ApiResponse.class);
		final CakeImageListResponse data = objectMapper.convertValue(response.getData(), CakeImageListResponse.class);

		assertEquals(HttpStatusCode.valueOf(200), responseEntity.getStatusCode());
		assertEquals(ReturnCode.SUCCESS.getCode(), response.getReturnCode());
		assertEquals(ReturnCode.SUCCESS.getMessage(), response.getReturnMessage());
		assertThat(data.cakeImages().size()).isNotNegative();
	}

	@TestWithDisplayName("검색어와 커서 아이디로 동적 검색, 위치 정보 없이 4개가 조회된다")
	void searchCakeImagesByKeyword() {
		final String url = "%s%d%s/search/cakes".formatted(BASE_URL, port, API_URL);
		final UriComponents uriComponents = UriComponentsBuilder
			.fromUriString(url)
			.queryParam("cakeId", 5)
			.queryParam("keyword", "tag")
			.queryParam("pageSize", 10)
			.build();

		// when
		final ResponseEntity<ApiResponse> responseEntity = restTemplate.getForEntity(uriComponents.toUriString(), ApiResponse.class);

		// then
		final ApiResponse response = objectMapper.convertValue(responseEntity.getBody(), ApiResponse.class);
		final CakeImageListResponse data = objectMapper.convertValue(response.getData(), CakeImageListResponse.class);

		assertEquals(HttpStatusCode.valueOf(200), responseEntity.getStatusCode());
		assertEquals(ReturnCode.SUCCESS.getCode(), response.getReturnCode());
		assertEquals(ReturnCode.SUCCESS.getMessage(), response.getReturnMessage());

		assertEquals(4, data.cakeImages().size());
	}

	@TestWithDisplayName("조회수로 케이크 이미지 조회에 성공한다")
	void searchByViews1() {
		// given
		final String url = "%s%d%s/search/views".formatted(BASE_URL, port, API_URL);
		final UriComponents uriComponents = UriComponentsBuilder
			.fromUriString(url)
			.queryParam("offset", 0L)
			.queryParam("pageSize", 4)
			.build();

		initializeViews();

		// when
		final ResponseEntity<ApiResponse> responseEntity = restTemplate.getForEntity(uriComponents.toUriString(), ApiResponse.class);

		// then
		final ApiResponse response = objectMapper.convertValue(responseEntity.getBody(), ApiResponse.class);
		final CakeImageListResponse data = objectMapper.convertValue(response.getData(), CakeImageListResponse.class);

		assertEquals(HttpStatusCode.valueOf(200), responseEntity.getStatusCode());
		assertEquals(ReturnCode.SUCCESS.getCode(), response.getReturnCode());
		assertEquals(ReturnCode.SUCCESS.getMessage(), response.getReturnMessage());

		assertEquals(4, data.size());
	}

	@TestWithDisplayName("조회한 케이크가 없을 시, 인기 케이크 이미지 조회에 빈 배열을 리턴한다")
	void searchByViews2() {
		// given
		final String url = "%s%d%s/search/views".formatted(BASE_URL, port, API_URL);
		final UriComponents uriComponents = UriComponentsBuilder
			.fromUriString(url)
			.queryParam("pageSize", 4)
			.build();

		// when
		final ResponseEntity<ApiResponse> responseEntity = restTemplate.getForEntity(uriComponents.toUriString(), ApiResponse.class);

		// then
		final ApiResponse response = objectMapper.convertValue(responseEntity.getBody(), ApiResponse.class);
		final CakeImageListResponse data = objectMapper.convertValue(response.getData(), CakeImageListResponse.class);

		assertEquals(HttpStatusCode.valueOf(200), responseEntity.getStatusCode());
		assertEquals(ReturnCode.SUCCESS.getCode(), response.getReturnCode());
		assertEquals(ReturnCode.SUCCESS.getMessage(), response.getReturnMessage());

		assertEquals(0, data.size());
	}

	@TestWithDisplayName("해당 id의 이전에 하트 누른 케이크에 대하여 하트 상태인지 조회에 성공한다.")
	void isHeartCake1() {
		// given
		final Long cakeId = 3L;
		final String url = "%s%d%s/{cakeId}/heart".formatted(BASE_URL, port, API_URL);
		final UriComponents uriComponents = UriComponentsBuilder
			.fromUriString(url)
			.buildAndExpand(cakeId);

		// when
		final ResponseEntity<ApiResponse> responseEntity = restTemplate.exchange(
			uriComponents.toUriString(),
			HttpMethod.GET,
			new HttpEntity<>(getAuthHeader()),
			ApiResponse.class);

		// then
		final ApiResponse response = objectMapper.convertValue(responseEntity.getBody(), ApiResponse.class);
		final HeartResponse data = objectMapper.convertValue(response.getData(), HeartResponse.class);

		assertEquals(HttpStatusCode.valueOf(200), responseEntity.getStatusCode());
		assertEquals(ReturnCode.SUCCESS.getCode(), response.getReturnCode());
		assertEquals(ReturnCode.SUCCESS.getMessage(), response.getReturnMessage());

		assertEquals(true, data.isHeart());
	}

	@TestWithDisplayName("해당 id의 이전에 하트 누르지 않은 케이크에 대하여 하트 상태인지 조회에 성공한다.")
	void isHeartCake2() {
		// given
		final Long cakeId = 1L;
		final String url = "%s%d%s/{cakeId}/heart".formatted(BASE_URL, port, API_URL);
		final UriComponents uriComponents = UriComponentsBuilder
			.fromUriString(url)
			.buildAndExpand(cakeId);

		// when
		final ResponseEntity<ApiResponse> responseEntity = restTemplate.exchange(
			uriComponents.toUriString(),
			HttpMethod.GET,
			new HttpEntity<>(getAuthHeader()),
			ApiResponse.class);

		// then
		final ApiResponse response = objectMapper.convertValue(responseEntity.getBody(), ApiResponse.class);
		final HeartResponse data = objectMapper.convertValue(response.getData(), HeartResponse.class);

		assertEquals(HttpStatusCode.valueOf(200), responseEntity.getStatusCode());
		assertEquals(ReturnCode.SUCCESS.getCode(), response.getReturnCode());
		assertEquals(ReturnCode.SUCCESS.getMessage(), response.getReturnMessage());

		assertEquals(false, data.isHeart());
	}

	@TestWithDisplayName("해당 id의 케이크 하트에 성공한다.")
	void heartCake() {
		// given
		final Long cakeId = 1L;
		final String url = "%s%d%s/{cakeId}/heart".formatted(BASE_URL, port, API_URL);
		final UriComponents uriComponents = UriComponentsBuilder
			.fromUriString(url)
			.buildAndExpand(cakeId);

		// when
		final ResponseEntity<ApiResponse> responseEntity = restTemplate.exchange(
			uriComponents.toUriString(),
			HttpMethod.PUT,
			new HttpEntity<>(getAuthHeader()),
			ApiResponse.class);

		// then
		final ApiResponse response = objectMapper.convertValue(responseEntity.getBody(), ApiResponse.class);

		assertEquals(HttpStatusCode.valueOf(200), responseEntity.getStatusCode());
		assertEquals(ReturnCode.SUCCESS.getCode(), response.getReturnCode());
		assertEquals(ReturnCode.SUCCESS.getMessage(), response.getReturnMessage());
		assertNull(response.getData());
	}

	@TestWithDisplayName("해당 id의 케이크 하트 취소에 성공한다.")
	void heartCancelCake() {
		// given
		final Long cakeId = 3L;
		final String url = "%s%d%s/{cakeId}/heart".formatted(BASE_URL, port, API_URL);
		final UriComponents uriComponents = UriComponentsBuilder
			.fromUriString(url)
			.buildAndExpand(cakeId);

		// when
		final ResponseEntity<ApiResponse> responseEntity = restTemplate.exchange(
			uriComponents.toUriString(),
			HttpMethod.PUT,
			new HttpEntity<>(getAuthHeader()),
			ApiResponse.class);

		// then
		final ApiResponse response = objectMapper.convertValue(responseEntity.getBody(), ApiResponse.class);

		assertEquals(HttpStatusCode.valueOf(200), responseEntity.getStatusCode());
		assertEquals(ReturnCode.SUCCESS.getCode(), response.getReturnCode());
		assertEquals(ReturnCode.SUCCESS.getMessage(), response.getReturnMessage());
		assertNull(response.getData());
	}

	@TestWithDisplayName("케이크 정보 업데이트에 성공한다")
	void updateCake() {
		// given
		final Long cakeId = 1L;
		final String url = "%s%d%s/{cakeId}".formatted(BASE_URL, port, API_URL);
		final UriComponents uriComponents = UriComponentsBuilder
			.fromUriString(url)
			.buildAndExpand(cakeId);
		final CakeUpdateRequest request = getConstructorMonkey().giveMeBuilder(CakeUpdateRequest.class)
			.set("tagNames", List.of("tag_name1", "new_tag"))
			.sample();


		// when
		final ResponseEntity<ApiResponse> responseEntity = restTemplate.exchange(
			uriComponents.toUriString(),
			HttpMethod.PUT,
			new HttpEntity<>(request, getAuthHeader()),
			ApiResponse.class);

		// then
		final ApiResponse response = objectMapper.convertValue(responseEntity.getBody(), ApiResponse.class);

		assertEquals(HttpStatusCode.valueOf(200), responseEntity.getStatusCode());
		assertEquals(ReturnCode.SUCCESS.getCode(), response.getReturnCode());
		assertEquals(ReturnCode.SUCCESS.getMessage(), response.getReturnMessage());
		assertNull(response.getData());
	}

	@TestWithDisplayName("어드민에 의해 케이크 정보 업데이트에 성공한다")
	void updateCakeByAdmin() {
		// given
		final Long cakeId = 1L;
		final String url = "%s%d%s/{cakeId}".formatted(BASE_URL, port, API_URL);
		final UriComponents uriComponents = UriComponentsBuilder
			.fromUriString(url)
			.buildAndExpand(cakeId);
		final CakeUpdateRequest request = getConstructorMonkey().giveMeBuilder(CakeUpdateRequest.class)
			.set("tagNames", List.of("tag_name1", "new_tag"))
			.sample();


		// when
		final ResponseEntity<ApiResponse> responseEntity = restTemplate.exchange(
			uriComponents.toUriString(),
			HttpMethod.PUT,
			new HttpEntity<>(request, getAuthHeaderById(10L)),
			ApiResponse.class);

		// then
		final ApiResponse response = objectMapper.convertValue(responseEntity.getBody(), ApiResponse.class);

		assertEquals(HttpStatusCode.valueOf(200), responseEntity.getStatusCode());
		assertEquals(ReturnCode.SUCCESS.getCode(), response.getReturnCode());
		assertEquals(ReturnCode.SUCCESS.getMessage(), response.getReturnMessage());
		assertNull(response.getData());
	}

	@TestWithDisplayName("케이크 삭제에 성공한다")
	void deleteCake() {
		// given
		final Long cakeId = 1L;
		final String url = "%s%d%s/{cakeId}".formatted(BASE_URL, port, API_URL);
		final UriComponents uriComponents = UriComponentsBuilder
			.fromUriString(url)
			.buildAndExpand(cakeId);


		// when
		final ResponseEntity<ApiResponse> responseEntity = restTemplate.exchange(
			uriComponents.toUriString(),
			HttpMethod.DELETE,
			new HttpEntity<>(getAuthHeader()),
			ApiResponse.class);

		// then
		final ApiResponse response = objectMapper.convertValue(responseEntity.getBody(), ApiResponse.class);

		assertEquals(HttpStatusCode.valueOf(200), responseEntity.getStatusCode());
		assertEquals(ReturnCode.SUCCESS.getCode(), response.getReturnCode());
		assertEquals(ReturnCode.SUCCESS.getMessage(), response.getReturnMessage());
		assertNull(response.getData());
	}

	@TestWithDisplayName("케이크 상세 조회에 성공한다")
	void getDetailCake() {
		// given
		final Long cakeId = 1L;
		final String url = "%s%d%s/{cakeId}".formatted(BASE_URL, port, API_URL);
		final UriComponents uriComponents = UriComponentsBuilder
			.fromUriString(url)
			.buildAndExpand(cakeId);

		// when
		final ResponseEntity<ApiResponse> responseEntity = restTemplate.getForEntity(uriComponents.toUriString(), ApiResponse.class);

		// then
		final ApiResponse response = objectMapper.convertValue(responseEntity.getBody(), ApiResponse.class);
		final CakeDetailResponse data = objectMapper.convertValue(response.getData(), CakeDetailResponse.class);

		assertEquals("cake_image_url1", data.cakeImageUrl());
		assertEquals("케이크 맛집1", data.cakeShopName());
		assertEquals("케이크 맛집입니다.", data.shopBio());
		assertEquals(1L, data.cakeShopId().longValue());
		assertEquals(1, data.cakeCategories().size());
		assertEquals(5, data.tags().size());
	}

	@TestWithDisplayName("케이크 상세 조회에서 카테고리가 없는 경우, 태그가 없는 경우에 성공한다")
	void getDetailCakeWithNoCategoriesAndNoTags() {
		// given
		final Long cakeId = 19L;
		final String url = "%s%d%s/{cakeId}".formatted(BASE_URL, port, API_URL);
		final UriComponents uriComponents = UriComponentsBuilder
			.fromUriString(url)
			.buildAndExpand(cakeId);

		// when
		final ResponseEntity<ApiResponse> responseEntity = restTemplate.getForEntity(uriComponents.toUriString(), ApiResponse.class);

		// then
		final ApiResponse response = objectMapper.convertValue(responseEntity.getBody(), ApiResponse.class);
		final CakeDetailResponse data = objectMapper.convertValue(response.getData(), CakeDetailResponse.class);

		assertEquals("cake_image_url19", data.cakeImageUrl());
		assertEquals("케이크 맛집10", data.cakeShopName());
		assertEquals("케이크 맛집입니다.", data.shopBio());
		assertEquals(10L, data.cakeShopId().longValue());
		assertEquals(0, data.cakeCategories().size());
		assertEquals(0, data.tags().size());
	}

	@TestWithDisplayName("케이크 추가에 성공한다")
	void createCake() {
		// given
		final Long cakeShopId = 1L;
		final String url = "%s%d%s/{cakeShopId}".formatted(BASE_URL, port, API_URL);
		final UriComponents uriComponents = UriComponentsBuilder
			.fromUriString(url)
			.buildAndExpand(cakeShopId);
		final CakeCreateRequest request = getConstructorMonkey().giveMeBuilder(CakeCreateRequest.class)
			.set("tagNames", List.of("tag_name1", "new_tag1", "new_tag2"))
			.sample();


		// when
		final ResponseEntity<ApiResponse> responseEntity = restTemplate.exchange(
			uriComponents.toUriString(),
			HttpMethod.POST,
			new HttpEntity<>(request, getAuthHeader()),
			ApiResponse.class);

		// then
		final ApiResponse response = objectMapper.convertValue(responseEntity.getBody(), ApiResponse.class);

		assertEquals(HttpStatusCode.valueOf(200), responseEntity.getStatusCode());
		assertEquals(ReturnCode.SUCCESS.getCode(), response.getReturnCode());
		assertEquals(ReturnCode.SUCCESS.getMessage(), response.getReturnMessage());
		assertNull(response.getData());
	}
}
