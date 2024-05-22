package com.cakk.api.integration.cake;

import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.*;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import com.cakk.api.common.base.IntegrationTest;
import com.cakk.api.dto.response.cake.CakeImageListResponse;
import com.cakk.common.enums.CakeDesignCategory;
import com.cakk.common.enums.ReturnCode;
import com.cakk.common.response.ApiResponse;

@SqlGroup({
	@Sql(scripts = {
		"/sql/insert-test-user.sql",
		"/sql/insert-cake.sql"
	}, executionPhase = BEFORE_TEST_METHOD),
	@Sql(scripts = "/sql/delete-all.sql", executionPhase = AFTER_TEST_METHOD)
})
class CakeIntegrationTest extends IntegrationTest {

	private static final String API_URL = "/api/v1/cakes";

	@Test
	void 카테고리로_케이크_이미지_조회에_성공한다() {
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

		Assertions.assertEquals(HttpStatusCode.valueOf(200), responseEntity.getStatusCode());
		Assertions.assertEquals(ReturnCode.SUCCESS.getCode(), response.getReturnCode());
		Assertions.assertEquals(ReturnCode.SUCCESS.getMessage(), response.getReturnMessage());
		Assertions.assertEquals(5, data.cakeImages().size());
		Assertions.assertEquals(6L, data.lastCakeId());
		Assertions.assertEquals(5, data.size());
	}

	@Test
	void 카테고리로_다음_페이지_케이크_이미지_조회에_성공한다() {
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

		Assertions.assertEquals(HttpStatusCode.valueOf(200), responseEntity.getStatusCode());
		Assertions.assertEquals(ReturnCode.SUCCESS.getCode(), response.getReturnCode());
		Assertions.assertEquals(ReturnCode.SUCCESS.getMessage(), response.getReturnMessage());
		Assertions.assertEquals(5, data.cakeImages().size());
		Assertions.assertEquals(1L, data.lastCakeId());
		Assertions.assertEquals(5, data.size());
	}

	@Test
	void 카테고리로_케이크_이미지_조회_시_데이터가_없으면_빈_배열을_반환한다() {
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

		Assertions.assertEquals(HttpStatusCode.valueOf(200), responseEntity.getStatusCode());
		Assertions.assertEquals(ReturnCode.SUCCESS.getCode(), response.getReturnCode());
		Assertions.assertEquals(ReturnCode.SUCCESS.getMessage(), response.getReturnMessage());
		Assertions.assertEquals(0, data.cakeImages().size());
		Assertions.assertNull(data.lastCakeId());
		Assertions.assertEquals(0, data.size());
	}
}
