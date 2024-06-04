package com.cakk.api.integration.user;

import static org.junit.Assert.*;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import com.cakk.api.common.annotation.TestWithDisplayName;
import com.cakk.api.common.base.IntegrationTest;
import com.cakk.api.dto.response.user.JwtResponse;
import com.cakk.api.provider.jwt.JwtProvider;
import com.cakk.api.vo.JsonWebToken;
import com.cakk.common.enums.ReturnCode;
import com.cakk.common.response.ApiResponse;
import com.cakk.domain.mysql.entity.user.User;
import com.cakk.domain.mysql.repository.reader.UserReader;
import com.cakk.domain.redis.repository.impl.TokenRedisRepository;

@SqlGroup({
	@Sql(scripts = {
		"/sql/insert-test-user.sql"
	}, executionPhase = BEFORE_TEST_METHOD),
	@Sql(scripts = "/sql/delete-all.sql", executionPhase = AFTER_TEST_METHOD)
})
public class SignIntegrationTest extends IntegrationTest {

	private static final String API_URL = "/api/v1";

	@Autowired
	private TokenRedisRepository tokenRedisRepository;

	@TestWithDisplayName("토큰 재발급에 성공한다.")
	void recreate() {
		// given
		final String url = "%s%d%s/recreate-token".formatted(BASE_URL, port, API_URL);
		final UriComponents uriComponents = UriComponentsBuilder
			.fromUriString(url)
			.build();

		JsonWebToken jsonWebToken = getAuthToken();

		HttpHeaders headers = new HttpHeaders();
		headers.set("Refresh", jsonWebToken.refreshToken());

		HttpEntity request = new HttpEntity(headers);

		// when
		final ResponseEntity<ApiResponse> responseEntity = restTemplate.postForEntity(
			uriComponents.toUriString(),
			request,
			ApiResponse.class);

		// then
		final ApiResponse response = objectMapper.convertValue(responseEntity.getBody(), ApiResponse.class);
		final JwtResponse data = objectMapper.convertValue(response.getData(), JwtResponse.class);

		assertEquals(HttpStatusCode.valueOf(200), responseEntity.getStatusCode());
		assertEquals(ReturnCode.SUCCESS.getCode(), response.getReturnCode());
		assertEquals(ReturnCode.SUCCESS.getMessage(), response.getReturnMessage());

		assertNotNull(data.accessToken());
		assertNotNull(data.refreshToken());
		assertNotNull(data.grantType());
	}

	@TestWithDisplayName("블랙리스트인 리프레시 토큰인 경우, 토큰 재발급에 성공한다.")
	void recreate2() {
		// given
		final String url = "%s%d%s/recreate-token".formatted(BASE_URL, port, API_URL);
		final UriComponents uriComponents = UriComponentsBuilder
			.fromUriString(url)
			.build();

		JsonWebToken jsonWebToken = getAuthToken();
		tokenRedisRepository.registerBlackList(jsonWebToken.refreshToken(), getRefreshTokenExpiredSecond());

		HttpHeaders headers = new HttpHeaders();
		headers.set("Refresh", jsonWebToken.refreshToken());

		HttpEntity request = new HttpEntity(headers);

		// when
		final ResponseEntity<ApiResponse> responseEntity = restTemplate.postForEntity(
			uriComponents.toUriString(),
			request,
			ApiResponse.class);

		// then
		final ApiResponse response = objectMapper.convertValue(responseEntity.getBody(), ApiResponse.class);

		assertEquals(HttpStatusCode.valueOf(400), responseEntity.getStatusCode());
		assertEquals(ReturnCode.BLACK_LIST_TOKEN.getCode(), response.getReturnCode());
		assertEquals(ReturnCode.BLACK_LIST_TOKEN.getMessage(), response.getReturnMessage());
	}

	@TestWithDisplayName("잘못된 리프레시 토큰인 경우, 토큰 재발급에 성공한다.")
	void recreate3() {
		// given
		final String url = "%s%d%s/recreate-token".formatted(BASE_URL, port, API_URL);
		final UriComponents uriComponents = UriComponentsBuilder
			.fromUriString(url)
			.build();

		String refreshToken = "false token";

		HttpHeaders headers = new HttpHeaders();
		headers.set("Refresh", refreshToken);

		HttpEntity request = new HttpEntity(headers);

		// when
		final ResponseEntity<ApiResponse> responseEntity = restTemplate.postForEntity(
			uriComponents.toUriString(),
			request,
			ApiResponse.class);

		// then
		final ApiResponse response = objectMapper.convertValue(responseEntity.getBody(), ApiResponse.class);

		assertEquals(HttpStatusCode.valueOf(400), responseEntity.getStatusCode());
		assertEquals(ReturnCode.WRONG_JWT_TOKEN.getCode(), response.getReturnCode());
		assertEquals(ReturnCode.WRONG_JWT_TOKEN.getMessage(), response.getReturnMessage());
	}


}
