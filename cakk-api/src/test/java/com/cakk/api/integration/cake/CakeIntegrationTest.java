package com.cakk.api.integration.cake;

import static org.junit.Assert.*;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import com.cakk.api.common.base.IntegrationTest;
import com.cakk.api.common.annotation.TestWithDisplayName;
import com.cakk.api.dto.response.cake.CakeImageListResponse;
import com.cakk.common.enums.CakeDesignCategory;
import com.cakk.common.enums.ReturnCode;
import com.cakk.common.response.ApiResponse;
import com.cakk.domain.mysql.dto.param.cake.CakeImageResponseParam;
import com.cakk.domain.mysql.entity.cake.CakeCategory;
import com.cakk.domain.mysql.repository.reader.CakeCategoryReader;

@SqlGroup({
	@Sql(scripts = {
		"/sql/insert-test-user.sql",
		"/sql/insert-cake.sql"
	}, executionPhase = BEFORE_TEST_METHOD),
	@Sql(scripts = "/sql/delete-all.sql", executionPhase = AFTER_TEST_METHOD)
})
class CakeIntegrationTest extends IntegrationTest {

	private static final String API_URL = "/api/v1/cakes";

	@Autowired
	private CakeCategoryReader cakeCategoryReader;

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
			CakeCategory cakeCategory = cakeCategoryReader.findByCakeId(cakeImage.cakeId());
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
			CakeCategory cakeCategory = cakeCategoryReader.findByCakeId(cakeImage.cakeId());
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
		assertEquals(4, data.size());
		data.cakeImages().forEach(cakeImage -> {
			assertEquals(Long.valueOf(1L), cakeImage.cakeShopId());
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
			assertEquals(Long.valueOf(1L), cakeImage.cakeShopId())
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

	@TestWithDisplayName("검색어, 태그명, 케이크 카테고리, 사용자 위치를 포함한 동적 검색, SQL 파일 기준 10개가 조회된다")
	void searchCakeImagesByTextAndLocation1() {
		final String url = "%s%d%s/search/cakes".formatted(BASE_URL, port, API_URL);
		final UriComponents uriComponents = UriComponentsBuilder
			.fromUriString(url)
			.queryParam("cakeId", 0)
			.queryParam("keyword", "케")
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
		assertEquals(10, data.size());
	}

	@TestWithDisplayName("사용자 위치를 포함한 동적 검색, SQL 파일 기준 10개가 조회된다")
	void searchCakeImagesByTextAndLocation2() {
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
		assertEquals(10, data.size());
	}
}
