package com.cakk.api.integration.user;

import static org.junit.Assert.*;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.*;

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
import com.cakk.api.dto.request.like.HeartCakeSearchRequest;
import com.cakk.api.dto.response.like.HeartCakeImageListResponse;
import com.cakk.common.enums.ReturnCode;
import com.cakk.common.response.ApiResponse;
import com.cakk.domain.mysql.dto.param.like.HeartCakeImageResponseParam;

@SqlGroup({
	@Sql(scripts = {
		"/sql/insert-test-user.sql",
		"/sql/insert-cake.sql",
		"/sql/insert-heart.sql"
	}, executionPhase = BEFORE_TEST_METHOD),
	@Sql(scripts = "/sql/delete-all.sql", executionPhase = AFTER_TEST_METHOD)
})
public class HeartIntegrationTest extends IntegrationTest {

	private static final String API_URL = "/api/v1/me";

	@TestWithDisplayName("하트 한 케이크 목록을 조회한다.")
	void listByHeart1() {
		// given
		final String url = "%s%d%s/heart-cakes".formatted(BASE_URL, port, API_URL);
		final HeartCakeSearchRequest params = new HeartCakeSearchRequest(null, 5);
		final UriComponents uriComponents = UriComponentsBuilder
			.fromUriString(url)
			.queryParam("cakeHeartId", params.cakeHeartId())
			.queryParam("pageSize", params.pageSize())
			.build();

		// when
		final ResponseEntity<ApiResponse> responseEntity = restTemplate.exchange(
			uriComponents.toUriString(),
			HttpMethod.GET,
			new HttpEntity<>(getAuthHeader()),
			ApiResponse.class);

		// then
		final ApiResponse response = objectMapper.convertValue(responseEntity.getBody(), ApiResponse.class);
		final HeartCakeImageListResponse data = objectMapper.convertValue(response.getData(), HeartCakeImageListResponse.class);

		assertEquals(HttpStatusCode.valueOf(200), responseEntity.getStatusCode());
		assertEquals(ReturnCode.SUCCESS.getCode(), response.getReturnCode());
		assertEquals(ReturnCode.SUCCESS.getMessage(), response.getReturnMessage());

		final Long lastCakeLikeId = data.cakeImages().stream()
			.map(HeartCakeImageResponseParam::cakeHeartId)
			.min(Long::compareTo)
			.orElse(null);
		assertEquals(lastCakeLikeId, data.lastCakeHeartId());
		assertEquals(params.pageSize().intValue(), data.size());
	}

	@TestWithDisplayName("하트 한 케이크 목록이 없을 시 빈 배열을 조회한다.")
	void listByHeart2() {
		// given
		final String url = "%s%d%s/heart-cakes".formatted(BASE_URL, port, API_URL);
		final HeartCakeSearchRequest params = new HeartCakeSearchRequest(1L, 5);
		final UriComponents uriComponents = UriComponentsBuilder
			.fromUriString(url)
			.queryParam("cakeHeartId", params.cakeHeartId())
			.queryParam("pageSize", params.pageSize())
			.build();

		// when
		final ResponseEntity<ApiResponse> responseEntity = restTemplate.exchange(
			uriComponents.toUriString(),
			HttpMethod.GET,
			new HttpEntity<>(getAuthHeader()),
			ApiResponse.class);

		// then
		final ApiResponse response = objectMapper.convertValue(responseEntity.getBody(), ApiResponse.class);
		final HeartCakeImageListResponse data = objectMapper.convertValue(response.getData(), HeartCakeImageListResponse.class);

		assertEquals(HttpStatusCode.valueOf(200), responseEntity.getStatusCode());
		assertEquals(ReturnCode.SUCCESS.getCode(), response.getReturnCode());
		assertEquals(ReturnCode.SUCCESS.getMessage(), response.getReturnMessage());

		final Long lastCakeLikeId = data.cakeImages().stream()
			.map(HeartCakeImageResponseParam::cakeHeartId)
			.min(Long::compareTo)
			.orElse(null);
		assertEquals(lastCakeLikeId, data.lastCakeHeartId());
		assertEquals(0, data.size());
	}
}
