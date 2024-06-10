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
import com.cakk.api.dto.request.like.LikeCakeSearchRequest;
import com.cakk.api.dto.response.like.LikeCakeImageListResponse;
import com.cakk.common.enums.ReturnCode;
import com.cakk.common.response.ApiResponse;
import com.cakk.domain.mysql.dto.param.like.LikeCakeImageResponseParam;

@SqlGroup({
	@Sql(scripts = {
		"/sql/insert-test-user.sql",
		"/sql/insert-cake.sql",
		"/sql/insert-like.sql"
	}, executionPhase = BEFORE_TEST_METHOD),
	@Sql(scripts = "/sql/delete-all.sql", executionPhase = AFTER_TEST_METHOD)
})
public class LikeIntegrationTest extends IntegrationTest {

	private static final String API_URL = "/api/v1/me";

	@TestWithDisplayName("좋아요 한 케이크 목록을 조회한다.")
	void listByLike1() {
		// given
		final String url = "%s%d%s/liked-cakes".formatted(BASE_URL, port, API_URL);
		final LikeCakeSearchRequest params = new LikeCakeSearchRequest(null, 5);
		final UriComponents uriComponents = UriComponentsBuilder
			.fromUriString(url)
			.queryParam("cakeLikeId", params.cakeLikeId())
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
		final LikeCakeImageListResponse data = objectMapper.convertValue(response.getData(), LikeCakeImageListResponse.class);

		assertEquals(HttpStatusCode.valueOf(200), responseEntity.getStatusCode());
		assertEquals(ReturnCode.SUCCESS.getCode(), response.getReturnCode());
		assertEquals(ReturnCode.SUCCESS.getMessage(), response.getReturnMessage());

		final Long lastCakeLikeId = data.cakeImages().stream()
			.map(LikeCakeImageResponseParam::cakeLikeId)
			.min(Long::compareTo)
			.orElse(null);
		assertEquals(lastCakeLikeId, data.lastCakeLikeId());
		assertEquals(params.pageSize().intValue(), data.size());
	}

	@TestWithDisplayName("좋아요 한 케이크 목록이 없을 시 빈 배열을 조회한다.")
	void listByLike2() {
		// given
		final String url = "%s%d%s/liked-cakes".formatted(BASE_URL, port, API_URL);
		final LikeCakeSearchRequest params = new LikeCakeSearchRequest(1L, 5);
		final UriComponents uriComponents = UriComponentsBuilder
			.fromUriString(url)
			.queryParam("cakeLikeId", params.cakeLikeId())
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
		final LikeCakeImageListResponse data = objectMapper.convertValue(response.getData(), LikeCakeImageListResponse.class);

		assertEquals(HttpStatusCode.valueOf(200), responseEntity.getStatusCode());
		assertEquals(ReturnCode.SUCCESS.getCode(), response.getReturnCode());
		assertEquals(ReturnCode.SUCCESS.getMessage(), response.getReturnMessage());

		final Long lastCakeLikeId = data.cakeImages().stream()
			.map(LikeCakeImageResponseParam::cakeLikeId)
			.min(Long::compareTo)
			.orElse(null);
		assertEquals(lastCakeLikeId, data.lastCakeLikeId());
		assertEquals(0, data.size());
	}
}
