package com.cakk.api.integration.admin;

import static org.junit.Assert.*;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.*;

import org.assertj.core.api.Assertions;
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
import com.cakk.api.dto.request.shop.CreateShopRequest;
import com.cakk.api.dto.request.shop.PromotionRequest;
import com.cakk.api.dto.response.shop.CakeShopCreateResponse;
import com.cakk.api.dto.response.shop.CakeShopOwnerCandidateResponse;
import com.cakk.api.dto.response.shop.CakeShopOwnerCandidatesResponse;
import com.cakk.common.enums.ReturnCode;
import com.cakk.common.response.ApiResponse;

@SqlGroup({
	@Sql(scripts = {
		"/sql/insert-test-user.sql",
		"/sql/insert-business-information.sql"
	}, executionPhase = BEFORE_TEST_METHOD),
	@Sql(scripts = "/sql/delete-all.sql", executionPhase = AFTER_TEST_METHOD)
})
public class AdminIntegrationTest extends IntegrationTest  {

	private static final String API_URL = "/api/v1/admin";


	@TestWithDisplayName("백 오피스 API, 케이크샵 생성에 성공한다")
	void backOfficeCreateCakeShop() {
		final String url = "%s%d%s".formatted(BASE_URL, port, API_URL);
		final UriComponents uriComponents = UriComponentsBuilder
			.fromUriString(url)
			.path("/shops/create")
			.build();
		final CreateShopRequest request = getConstructorMonkey().giveMeBuilder(CreateShopRequest.class)
			.sample();

		// when
		final ResponseEntity<ApiResponse> responseEntity = restTemplate.exchange(
			uriComponents.toUriString(),
			HttpMethod.POST,
			new HttpEntity<>(request),
			ApiResponse.class);

		// then
		final ApiResponse response = objectMapper.convertValue(responseEntity.getBody(), ApiResponse.class);
		final CakeShopCreateResponse data = objectMapper.convertValue(response.getData(), CakeShopCreateResponse.class);

		Assertions.assertThat(data.cakeShopId()).isNotNull();
	}

	@TestWithDisplayName("백 오피스 API, 케이크샵 사장님 인증 요청 리스트 조회에 성공한다")
	void backOfficeSearchByCakeShopBusinessOwnerCandidates() {
		//given
		final String url = "%s%d%s".formatted(BASE_URL, port, API_URL);
		final UriComponents uriComponents = UriComponentsBuilder
			.fromUriString(url)
			.path("/shops/candidates")
			.build();

		// when
		final ResponseEntity<ApiResponse> responseEntity = restTemplate.getForEntity(uriComponents.toUriString(), ApiResponse.class);

		// then
		final ApiResponse response = objectMapper.convertValue(responseEntity.getBody(), ApiResponse.class);
		final CakeShopOwnerCandidatesResponse data = objectMapper.convertValue(response.getData(),
			CakeShopOwnerCandidatesResponse.class);

		assertEquals(HttpStatusCode.valueOf(200), responseEntity.getStatusCode());
		assertEquals(ReturnCode.SUCCESS.getCode(), response.getReturnCode());
		assertEquals(ReturnCode.SUCCESS.getMessage(), response.getReturnMessage());
	}

	@TestWithDisplayName("백 오피스 API, 케이크 샵 사장님 인증 완료 처리에 성공한다")
	void backOfficeCakeShopBusinessOwnerApproved() {
		//given
		final String url = "%s%d%s".formatted(BASE_URL, port, API_URL);
		final UriComponents uriComponents = UriComponentsBuilder
			.fromUriString(url)
			.path("/shops/promote")
			.build();

		final PromotionRequest request = getConstructorMonkey().giveMeBuilder(PromotionRequest.class)
			.set("userId", 1L)
			.set("cakeShopId", 1L)
			.sample();

		// when
		final ResponseEntity<ApiResponse> responseEntity = restTemplate.exchange(
			uriComponents.toUriString(),
			HttpMethod.PUT,
			new HttpEntity<>(request),
			ApiResponse.class);

		// then
		final ApiResponse response = objectMapper.convertValue(responseEntity.getBody(), ApiResponse.class);

		assertEquals(HttpStatusCode.valueOf(200), responseEntity.getStatusCode());
		assertEquals(ReturnCode.SUCCESS.getCode(), response.getReturnCode());
		assertEquals(ReturnCode.SUCCESS.getMessage(), response.getReturnMessage());
	}

	@TestWithDisplayName("백 오피스 API, 케이크샵 사장님 인증 요청 상세 내용 조회에 성공한다")
	void backOfficeSearchByCakeShopBusinessOwnerCandidate() {
		//given
		final Long userId = 1L;
		final String url = "%s%d%s/shops/candidates/{userId}".formatted(BASE_URL, port, API_URL);
		final UriComponents uriComponents = UriComponentsBuilder
			.fromUriString(url)
			.buildAndExpand(userId);

		// when
		final ResponseEntity<ApiResponse> responseEntity = restTemplate.getForEntity(uriComponents.toUriString(), ApiResponse.class);

		// then
		final ApiResponse response = objectMapper.convertValue(responseEntity.getBody(), ApiResponse.class);
		final CakeShopOwnerCandidateResponse data = objectMapper.convertValue(response.getData(),
			CakeShopOwnerCandidateResponse.class);

		assertEquals(HttpStatusCode.valueOf(200), responseEntity.getStatusCode());
		assertEquals(ReturnCode.SUCCESS.getCode(), response.getReturnCode());
		assertEquals(ReturnCode.SUCCESS.getMessage(), response.getReturnMessage());

		assertEquals(1L, data.userId().longValue());
		assertEquals(1L, data.cakeShopId().longValue());
		assertEquals("test1@google.com", data.email());
		assertEquals("https://business_registration_image_url1", data.businessRegistrationImageUrl());
		assertEquals("https://id_card_image_url1", data.idCardImageUrl());
		assertEquals("010-0000-0000", data.emergencyContact());
	}
}

