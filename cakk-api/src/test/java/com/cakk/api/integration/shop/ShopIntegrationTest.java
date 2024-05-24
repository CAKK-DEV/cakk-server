package com.cakk.api.integration.shop;

import static org.junit.Assert.*;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.*;

import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import com.cakk.api.common.annotation.TestWithDisplayName;
import com.cakk.api.common.base.IntegrationTest;
import com.cakk.api.dto.response.shop.CakeShopDetailResponse;
import com.cakk.api.dto.response.shop.CakeShopSimpleResponse;
import com.cakk.common.enums.Days;
import com.cakk.common.enums.ReturnCode;
import com.cakk.common.response.ApiResponse;
import com.cakk.domain.dto.param.shop.CakeShopLinkParam;
import com.cakk.domain.entity.shop.CakeShop;
import com.cakk.domain.entity.shop.CakeShopOperation;
import com.cakk.domain.repository.reader.CakeShopLinkReader;
import com.cakk.domain.repository.reader.CakeShopOperationReader;
import com.cakk.domain.repository.reader.CakeShopReader;

@SqlGroup({
	@Sql(scripts = "/sql/insert-cake-shop.sql", executionPhase = BEFORE_TEST_METHOD),
	@Sql(scripts = "/sql/delete-all.sql", executionPhase = AFTER_TEST_METHOD)
})
public class ShopIntegrationTest extends IntegrationTest {

	private static final String API_URL = "/api/v1/shops";

	@Autowired
	private CakeShopReader cakeShopReader;

	@Autowired
	private CakeShopOperationReader cakeShopOperationReader;

	@Autowired
	private CakeShopLinkReader cakeShopLinkReader;

	@TestWithDisplayName("케이크 샵을 간단 조회에 성공한다.")
	void simple1() {
		final String url = "%s%d%s".formatted(BASE_URL, port, API_URL);
		final Long cakeShopId = 1L;
		final UriComponents uriComponents = UriComponentsBuilder
			.fromUriString(url)
			.path("/{cakeShopId}/simple")
			.buildAndExpand(cakeShopId);

		// when
		final ResponseEntity<ApiResponse> responseEntity = restTemplate.getForEntity(uriComponents.toUriString(), ApiResponse.class);

		// then
		final ApiResponse response = objectMapper.convertValue(responseEntity.getBody(), ApiResponse.class);
		final CakeShopSimpleResponse data = objectMapper.convertValue(response.getData(), CakeShopSimpleResponse.class);

		assertEquals(HttpStatusCode.valueOf(200), responseEntity.getStatusCode());
		assertEquals(ReturnCode.SUCCESS.getCode(), response.getReturnCode());
		assertEquals(ReturnCode.SUCCESS.getMessage(), response.getReturnMessage());

		final CakeShop cakeShop = cakeShopReader.findById(cakeShopId);
		assertEquals(cakeShop.getId(), data.cakeShopId());
		assertEquals(cakeShop.getThumbnailUrl(), data.thumbnailUrl());
		assertEquals(cakeShop.getShopName(), data.cakeShopName());
		assertEquals(cakeShop.getShopBio(), data.cakeShopBio());
	}

	@TestWithDisplayName("없는 케이크 샵일 경우, 간단 조회에 실패한다.")
	void simple2() {
		final String url = "%s%d%s".formatted(BASE_URL, port, API_URL);
		final Long cakeShopId = 1000L;
		final UriComponents uriComponents = UriComponentsBuilder
			.fromUriString(url)
			.path("/{cakeShopId}/simple")
			.buildAndExpand(cakeShopId);

		// when
		final ResponseEntity<ApiResponse> responseEntity = restTemplate.getForEntity(uriComponents.toUriString(), ApiResponse.class);

		// then
		final ApiResponse response = objectMapper.convertValue(responseEntity.getBody(), ApiResponse.class);

		assertEquals(HttpStatusCode.valueOf(400), responseEntity.getStatusCode());
		assertEquals(ReturnCode.NOT_EXIST_CAKE_SHOP.getCode(), response.getReturnCode());
		assertEquals(ReturnCode.NOT_EXIST_CAKE_SHOP.getMessage(), response.getReturnMessage());
	}

	@TestWithDisplayName("케이크 샵을 상세 조회에 성공한다.")
	void detail1() {
		final String url = "%s%d%s".formatted(BASE_URL, port, API_URL);
		final Long cakeShopId = 1L;
		final UriComponents uriComponents = UriComponentsBuilder
			.fromUriString(url)
			.path("/{cakeShopId}")
			.buildAndExpand(cakeShopId);

		// when
		final ResponseEntity<ApiResponse> responseEntity = restTemplate.getForEntity(uriComponents.toUriString(), ApiResponse.class);

		// then
		final ApiResponse response = objectMapper.convertValue(responseEntity.getBody(), ApiResponse.class);
		final CakeShopDetailResponse data = objectMapper.convertValue(response.getData(), CakeShopDetailResponse.class);

		assertEquals(HttpStatusCode.valueOf(200), responseEntity.getStatusCode());
		assertEquals(ReturnCode.SUCCESS.getCode(), response.getReturnCode());
		assertEquals(ReturnCode.SUCCESS.getMessage(), response.getReturnMessage());

		final CakeShop cakeShop = cakeShopReader.findById(cakeShopId);
		assertEquals(cakeShop.getId(), data.cakeShopId());
		assertEquals(cakeShop.getThumbnailUrl(), data.thumbnailUrl());
		assertEquals(cakeShop.getShopName(), data.cakeShopName());
		assertEquals(cakeShop.getShopBio(), data.cakeShopBio());
		assertEquals(cakeShop.getShopDescription(), data.cakeShopDescription());

		final Set<Days> cakeShopOperations = cakeShopOperationReader.findAllByCakeShopId(cakeShopId).stream()
			.map(CakeShopOperation::getOperationDay)
			.collect(Collectors.toSet());
		assertEquals(cakeShopOperations, data.operationDays());

		final Set<CakeShopLinkParam> cakeShopLinks = cakeShopLinkReader.findAllByCakeShopId(cakeShopId).stream()
			.map((it) -> new CakeShopLinkParam(it.getLinkKind(), it.getLinkPath()))
			.collect(Collectors.toSet());
		assertEquals(cakeShopLinks, data.links());
	}

	@TestWithDisplayName("없는 케이크 샵일 경우, 상세 조회에 실패한다.")
	void detail2() {
		final String url = "%s%d%s".formatted(BASE_URL, port, API_URL);
		final Long cakeShopId = 1000L;
		final UriComponents uriComponents = UriComponentsBuilder
			.fromUriString(url)
			.path("/{cakeShopId}")
			.buildAndExpand(cakeShopId);

		// when
		final ResponseEntity<ApiResponse> responseEntity = restTemplate.getForEntity(uriComponents.toUriString(), ApiResponse.class);

		// then
		final ApiResponse response = objectMapper.convertValue(responseEntity.getBody(), ApiResponse.class);

		assertEquals(HttpStatusCode.valueOf(400), responseEntity.getStatusCode());
		assertEquals(ReturnCode.NOT_EXIST_CAKE_SHOP.getCode(), response.getReturnCode());
		assertEquals(ReturnCode.NOT_EXIST_CAKE_SHOP.getMessage(), response.getReturnMessage());
	}

}