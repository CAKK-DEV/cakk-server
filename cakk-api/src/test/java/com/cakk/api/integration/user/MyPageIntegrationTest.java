package com.cakk.api.integration.user;

import static org.junit.Assert.*;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.*;

import java.time.LocalDate;
import java.util.Map;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import net.jqwik.api.Arbitraries;

import com.cakk.api.common.annotation.TestWithDisplayName;
import com.cakk.api.common.base.IntegrationTest;
import com.cakk.api.dto.request.user.ProfileUpdateRequest;
import com.cakk.api.dto.response.user.ProfileInformationResponse;
import com.cakk.api.vo.JsonWebToken;
import com.cakk.common.enums.Gender;
import com.cakk.common.enums.ReturnCode;
import com.cakk.common.exception.CakkException;
import com.cakk.common.response.ApiResponse;
import com.cakk.domain.mysql.entity.user.User;
import com.cakk.domain.mysql.repository.reader.UserReader;

@SqlGroup({
	@Sql(scripts = {
		"/sql/insert-test-user.sql"
	}, executionPhase = BEFORE_TEST_METHOD),
	@Sql(scripts = "/sql/delete-all.sql", executionPhase = AFTER_TEST_METHOD)
})
class MyPageIntegrationTest extends IntegrationTest {

	private static final String API_URL = "/api/v1/me";

	@TestWithDisplayName("프로필 조회에 성공한다.")
	void profile() {
		// given
		final String url = "%s%d%s".formatted(BASE_URL, port, API_URL);
		final UriComponents uriComponents = UriComponentsBuilder
			.fromUriString(url)
			.build();

		// when
		final ResponseEntity<ApiResponse> responseEntity = restTemplate.exchange(
			uriComponents.toUriString(),
			HttpMethod.GET,
			new HttpEntity<>(getAuthHeader()),
			ApiResponse.class);

		// then
		final ApiResponse response = objectMapper.convertValue(responseEntity.getBody(), ApiResponse.class);
		final ProfileInformationResponse body = objectMapper.convertValue(response.getData(), ProfileInformationResponse.class);

		assertEquals(HttpStatusCode.valueOf(200), responseEntity.getStatusCode());
		assertEquals(ReturnCode.SUCCESS.getCode(), response.getReturnCode());
		assertEquals(ReturnCode.SUCCESS.getMessage(), response.getReturnMessage());

		final User user = userReader.findByUserId(getUserId());
		assertEquals(body.nickname(), user.getNickname());
		assertEquals(body.profileImageUrl(), user.getProfileImageUrl());
		assertEquals(body.email(), user.getEmail());
		assertEquals(body.gender(), user.getGender());
		assertEquals(body.role(), user.getRole());
	}

	@TestWithDisplayName("토큰에 Bearer가 붙어있지 않으면 Filter에서 에러를 반환한다.")
	void profile2() {
		// given
		final String url = "%s%d%s".formatted(BASE_URL, port, API_URL);
		final UriComponents uriComponents = UriComponentsBuilder
			.fromUriString(url)
			.build();

		final HttpHeaders headers = new HttpHeaders();
		final JsonWebToken jsonWebToken = getAuthToken();
		headers.set(HttpHeaders.AUTHORIZATION, jsonWebToken.accessToken());
		headers.set("Refresh", jsonWebToken.refreshToken());

		// when
		final ResponseEntity<ApiResponse> responseEntity = restTemplate.exchange(
			uriComponents.toUriString(),
			HttpMethod.GET,
			new HttpEntity<>(headers),
			ApiResponse.class);

		// then
		final ApiResponse response = objectMapper.convertValue(responseEntity.getBody(), ApiResponse.class);

		assertEquals(HttpStatusCode.valueOf(401), responseEntity.getStatusCode());
		assertEquals(ReturnCode.NOT_EXIST_BEARER_SUFFIX.getCode(), response.getReturnCode());
		assertEquals(ReturnCode.NOT_EXIST_BEARER_SUFFIX.getMessage(), response.getReturnMessage());
	}

	@TestWithDisplayName("프로필 수정에 성공한다.")
	void modify() {
		// given
		final String url = "%s%d%s".formatted(BASE_URL, port, API_URL);
		final UriComponents uriComponents = UriComponentsBuilder
			.fromUriString(url)
			.build();
		final ProfileUpdateRequest body = getConstructorMonkey().giveMeBuilder(ProfileUpdateRequest.class)
			.set("nickname", Arbitraries.strings().withCharRange('a', 'z').ofMinLength(1).ofMaxLength(20))
			.set("profileImageUrl", Arbitraries.strings().withCharRange('a', 'z').ofMinLength(1).ofMaxLength(200))
			.set("email", Arbitraries.strings().withCharRange('a', 'z').ofMinLength(1).ofMaxLength(50))
			.set("gender", Arbitraries.of(Gender.class))
			.set("birthday", LocalDate.now())
			.sample();

		// when
		final ResponseEntity<ApiResponse> responseEntity = restTemplate.exchange(
			uriComponents.toUriString(),
			HttpMethod.PUT,
			new HttpEntity<>(body, getAuthHeader()),
			ApiResponse.class);

		// then
		final ApiResponse response = objectMapper.convertValue(responseEntity.getBody(), ApiResponse.class);

		assertEquals(HttpStatusCode.valueOf(200), responseEntity.getStatusCode());
		assertEquals(ReturnCode.SUCCESS.getCode(), response.getReturnCode());
		assertEquals(ReturnCode.SUCCESS.getMessage(), response.getReturnMessage());

		final User user = userReader.findByUserId(1L);
		assertEquals(body.nickname(), user.getNickname());
		assertEquals(body.profileImageUrl(), user.getProfileImageUrl());
		assertEquals(body.email(), user.getEmail());
		assertEquals(body.gender(), user.getGender());
	}

	@TestWithDisplayName("회원 탈퇴에 성공한다.")
	void withdraw() {
		// given
		final String url = "%s%d%s".formatted(BASE_URL, port, API_URL);
		final UriComponents uriComponents = UriComponentsBuilder
			.fromUriString(url)
			.build();

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

		assertThrows(CakkException.class, () -> userReader.findByUserId(1L));
	}
}
