package com.cakk.api.integration.shop;

import static org.assertj.core.api.Assertions.*;
import static org.junit.Assert.*;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
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
import com.cakk.api.dto.request.user.CertificationRequest;
import com.cakk.api.dto.response.shop.CakeShopByMapResponse;
import com.cakk.api.dto.response.shop.CakeShopDetailResponse;
import com.cakk.api.dto.response.shop.CakeShopInfoResponse;
import com.cakk.api.dto.response.shop.CakeShopSearchResponse;
import com.cakk.api.dto.response.shop.CakeShopSimpleResponse;
import com.cakk.common.enums.Days;
import com.cakk.common.enums.ReturnCode;
import com.cakk.common.response.ApiResponse;
import com.cakk.domain.mysql.dto.param.shop.CakeShopLinkParam;
import com.cakk.domain.mysql.dto.param.shop.CakeShopOperationParam;
import com.cakk.domain.mysql.entity.shop.CakeShop;
import com.cakk.domain.mysql.entity.shop.CakeShopOperation;
import com.cakk.domain.mysql.repository.reader.CakeShopLinkReader;
import com.cakk.domain.mysql.repository.reader.CakeShopOperationReader;
import com.cakk.domain.mysql.repository.reader.CakeShopReader;

@SqlGroup({
	@Sql(scripts = {
		"/sql/insert-test-user.sql",
		"/sql/insert-cake-shop.sql",
		"/sql/insert-like.sql"
	}, executionPhase = BEFORE_TEST_METHOD),
	@Sql(scripts = "/sql/delete-all.sql", executionPhase = AFTER_TEST_METHOD)
})
class ShopIntegrationTest extends IntegrationTest {

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

	@TestWithDisplayName("로그인한 사용자는 자신의 케이크샵이 존재하지 않은 상태에서 사장님 인증을 요청한다")
	void request1() {
		final String url = "%s%d%s".formatted(BASE_URL, port, API_URL);
		final UriComponents uriComponents = UriComponentsBuilder
			.fromUriString(url)
			.path("/certification")
			.build();
		CertificationRequest request = getConstructorMonkey().giveMeBuilder(CertificationRequest.class)
			.set("businessRegistrationImageUrl", Arbitraries.strings().withCharRange('a', 'z').ofMaxLength(40).ofMinLength(1))
			.set("idCardImageUrl", Arbitraries.strings().withCharRange('a', 'z').ofMaxLength(40).ofMinLength(1))
			.setNull("cakeShopId")
			.set("emergencyContact", Arbitraries.strings().withCharRange('a', 'z').ofMaxLength(11).ofMinLength(1))
			.set("message", Arbitraries.strings().withCharRange('a', 'z').ofMaxLength(20)).sample();

		HttpEntity<CertificationRequest> entity = new HttpEntity<>(request, getAuthHeader());

		final ResponseEntity<ApiResponse> responseEntity = restTemplate.postForEntity(uriComponents.toUriString(), entity, ApiResponse.class);
		final ApiResponse response = objectMapper.convertValue(responseEntity.getBody(), ApiResponse.class);

		assertEquals(HttpStatusCode.valueOf(200), responseEntity.getStatusCode());
		assertEquals(ReturnCode.SUCCESS.getCode(), response.getReturnCode());
		assertEquals(ReturnCode.SUCCESS.getMessage(), response.getReturnMessage());
	}

	@TestWithDisplayName("로그인한 사용자는 자신의 케이크샵이 존재하지 않은 상태에서 사장님 인증을 요청한다")
	void request2() {
		final String url = "%s%d%s".formatted(BASE_URL, port, API_URL);
		final UriComponents uriComponents = UriComponentsBuilder
			.fromUriString(url)
			.path("/certification")
			.build();
		final CertificationRequest request = getConstructorMonkey().giveMeBuilder(CertificationRequest.class)
			.set("businessRegistrationImageUrl", Arbitraries.strings().withCharRange('a', 'z').ofMaxLength(40).ofMinLength(1))
			.set("idCardImageUrl", Arbitraries.strings().withCharRange('a', 'z').ofMaxLength(40).ofMinLength(1))
			.set("cakeShopId", Arbitraries.longs().greaterOrEqual(1).lessOrEqual(3))
			.set("emergencyContact", Arbitraries.strings().withCharRange('a', 'z').ofMaxLength(11).ofMinLength(1))
			.set("message", Arbitraries.strings().withCharRange('a', 'z').ofMaxLength(20)).sample();

		HttpEntity<CertificationRequest> entity = new HttpEntity<>(request, getAuthHeader());

		final ResponseEntity<ApiResponse> responseEntity = restTemplate.postForEntity(uriComponents.toUriString(), entity, ApiResponse.class);
		final ApiResponse response = objectMapper.convertValue(responseEntity.getBody(), ApiResponse.class);

		assertEquals(HttpStatusCode.valueOf(200), responseEntity.getStatusCode());
		assertEquals(ReturnCode.SUCCESS.getCode(), response.getReturnCode());
		assertEquals(ReturnCode.SUCCESS.getMessage(), response.getReturnMessage());
	}

	@TestWithDisplayName("케이크 샵의 상세 정보 조회에 성공한다.")
	void detailInfo1() {
		final String url = "%s%d%s".formatted(BASE_URL, port, API_URL);
		final Long cakeShopId = 1L;
		final UriComponents uriComponents = UriComponentsBuilder
			.fromUriString(url)
			.path("/{cakeShopId}/info")
			.buildAndExpand(cakeShopId);

		// when
		final ResponseEntity<ApiResponse> responseEntity = restTemplate.getForEntity(uriComponents.toUriString(), ApiResponse.class);

		// then
		final ApiResponse response = objectMapper.convertValue(responseEntity.getBody(), ApiResponse.class);
		final CakeShopInfoResponse data = objectMapper.convertValue(response.getData(), CakeShopInfoResponse.class);

		assertEquals(HttpStatusCode.valueOf(200), responseEntity.getStatusCode());
		assertEquals(ReturnCode.SUCCESS.getCode(), response.getReturnCode());
		assertEquals(ReturnCode.SUCCESS.getMessage(), response.getReturnMessage());

		final CakeShop cakeShop = cakeShopReader.findById(cakeShopId);
		Double longitude = cakeShop.getLocation().getX();
		Double latitude = cakeShop.getLocation().getY();

		assertEquals(cakeShop.getShopAddress(), data.shopAddress());
		assertEquals(latitude, data.latitude());
		assertEquals(longitude, data.longitude());

		List<CakeShopOperationParam> cakeShopOperations = cakeShopOperationReader.findAllByCakeShopId(cakeShopId).stream()
			.map((it) -> new CakeShopOperationParam(it.getOperationDay(), it.getOperationStartTime(), it.getOperationEndTime()))
			.toList();
		assertEquals(cakeShopOperations, data.shopOperationDays());
	}

	@TestWithDisplayName("없는 케이크 샵일 경우, 상세정보 조회에 실패한다.")
	void detailInfo2() {
		final String url = "%s%d%s".formatted(BASE_URL, port, API_URL);
		final Long cakeShopId = 1000L;
		final UriComponents uriComponents = UriComponentsBuilder
			.fromUriString(url)
			.path("/{cakeShopId}/info")
			.buildAndExpand(cakeShopId);

		// when
		final ResponseEntity<ApiResponse> responseEntity = restTemplate.getForEntity(uriComponents.toUriString(), ApiResponse.class);

		// then
		final ApiResponse response = objectMapper.convertValue(responseEntity.getBody(), ApiResponse.class);

		assertEquals(HttpStatusCode.valueOf(400), responseEntity.getStatusCode());
		assertEquals(ReturnCode.NOT_EXIST_CAKE_SHOP.getCode(), response.getReturnCode());
		assertEquals(ReturnCode.NOT_EXIST_CAKE_SHOP.getMessage(), response.getReturnMessage());
	}

	@TestWithDisplayName("테스트 sql script 기준으로 사용자 위치를 중심으로 반경 5km 이내의 가게들을 조회한다")
	void findAllShopsByLocationBased1() {
		final String url = "%s%d%s".formatted(BASE_URL, port, API_URL);
		final UriComponents uriComponents = UriComponentsBuilder
			.fromUriString(url)
			.path("/location-based")
			.queryParam("latitude", 37.2096575)
			.queryParam("longitude", 127.0998228)
			.build();

		//when
		final ResponseEntity<ApiResponse> responseEntity = restTemplate.getForEntity(uriComponents.toUriString(), ApiResponse.class);

		//then
		final ApiResponse response = objectMapper.convertValue(responseEntity.getBody(), ApiResponse.class);
		final CakeShopByMapResponse data = objectMapper.convertValue(response.getData(), CakeShopByMapResponse.class);

		assertEquals(HttpStatusCode.valueOf(200), responseEntity.getStatusCode());
		assertEquals(ReturnCode.SUCCESS.getCode(), response.getReturnCode());
		assertEquals(ReturnCode.SUCCESS.getMessage(), response.getReturnMessage());

		assertEquals(3, data.cakeShops().size());
		data.cakeShops().forEach(cakeShop -> {
			assertThat(cakeShop.getCakeShopId()).isIn(1L, 2L, 3L);
			assertThat(cakeShop.getCakeImageUrls().size()).isLessThanOrEqualTo(4);
			assertThat(cakeShop.getCakeShopName()).isNotNull();
		});
	}

	@TestWithDisplayName("해당 id의 케이크 샵 좋아요에 성공한다.")
	void likeCakeShop() {
		// given
		final Long cakeShopId = 2L;
		final String url = "%s%d%s/{cakeShopId}/like".formatted(BASE_URL, port, API_URL);
		final UriComponents uriComponents = UriComponentsBuilder
			.fromUriString(url)
			.buildAndExpand(cakeShopId);

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

	@TestWithDisplayName("해당 id의 케이크 샵 좋아요 취소에 성공한다.")
	void likeCancelCake() {
		// given
		final Long cakeShopId = 1L;
		final String url = "%s%d%s/{cakeShopId}/like".formatted(BASE_URL, port, API_URL);
		final UriComponents uriComponents = UriComponentsBuilder
			.fromUriString(url)
			.buildAndExpand(cakeShopId);

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

	@TestWithDisplayName("테스트 sql script 기준으로 사용자 위치를 중심으로 반경 5km 이내의 가게들을 조회한다")
	void findAllShopsByLocationBased2() {
		final String url = "%s%d%s".formatted(BASE_URL, port, API_URL);
		final UriComponents uriComponents = UriComponentsBuilder
			.fromUriString(url)
			.path("/location-based")
			.queryParam("latitude", 37.543343)
			.queryParam("longitude", 127.052609)
			.build();

		//when
		final ResponseEntity<ApiResponse> responseEntity = restTemplate.getForEntity(uriComponents.toUriString(), ApiResponse.class);

		//then
		final ApiResponse response = objectMapper.convertValue(responseEntity.getBody(), ApiResponse.class);
		final CakeShopByMapResponse data = objectMapper.convertValue(response.getData(), CakeShopByMapResponse.class);

		assertEquals(HttpStatusCode.valueOf(200), responseEntity.getStatusCode());
		assertEquals(ReturnCode.SUCCESS.getCode(), response.getReturnCode());
		assertEquals(ReturnCode.SUCCESS.getMessage(), response.getReturnMessage());

		assertEquals(7, data.cakeShops().size());
		data.cakeShops().forEach(cakeShop -> {
			assertThat(cakeShop.getCakeShopId()).isIn(4L, 5L, 6L, 7L, 8L, 9L, 10L);
			assertThat(cakeShop.getCakeImageUrls().size()).isLessThanOrEqualTo(4);
			assertThat(cakeShop.getCakeShopName()).isNotNull();
		});
	}

	@TestWithDisplayName("테스트 sql script 기준 10개의 케이크 샵이 조회된다")
	void searchCakeShopsByKeywordsWithConditions1() {
		final String url = "%s%d%s/search/shops".formatted(BASE_URL, port, API_URL);
		final UriComponents uriComponents = UriComponentsBuilder
			.fromUriString(url)
			.queryParam("keyword", "케이크")
			.queryParam("pageSize", 10)
			.build();

		//when
		final ResponseEntity<ApiResponse> responseEntity = restTemplate.getForEntity(uriComponents.toUriString(), ApiResponse.class);

		//then
		final ApiResponse response = objectMapper.convertValue(responseEntity.getBody(), ApiResponse.class);
		final CakeShopSearchResponse data = objectMapper.convertValue(response.getData(), CakeShopSearchResponse.class);

		assertEquals(HttpStatusCode.valueOf(200), responseEntity.getStatusCode());
		assertEquals(ReturnCode.SUCCESS.getCode(), response.getReturnCode());
		assertEquals(ReturnCode.SUCCESS.getMessage(), response.getReturnMessage());

		assertEquals(10, data.size());
		data.cakeShops().forEach(cakeShop -> {
			assertThat(cakeShop.getCakeImageUrls().size()).isLessThanOrEqualTo(4);
			assertThat(cakeShop.getCakeShopId()).isNotNull();
			assertThat(cakeShop.getCakeShopName()).isNotNull();
			assertThat(cakeShop.getOperationDays()).isNotNull();
		});
	}

	@TestWithDisplayName("테스트 sql script를 기준 10개의 케이크샵이 조회된다")
	void searchCakeShopsByKeywordWithConditions2() {
		final String url = "%s%d%s/search/shops".formatted(BASE_URL, port, API_URL);
		final UriComponents uriComponents = UriComponentsBuilder
			.fromUriString(url)
			.queryParam("keyword", "케이크")
			.queryParam("latitude", 37.2096575)
			.queryParam("longitude", 127.0998228)
			.queryParam("pageSize", 10)
			.build();

		//when
		final ResponseEntity<ApiResponse> responseEntity = restTemplate.getForEntity(uriComponents.toUriString(), ApiResponse.class);

		//then
		final ApiResponse response = objectMapper.convertValue(responseEntity.getBody(), ApiResponse.class);
		final CakeShopSearchResponse data = objectMapper.convertValue(response.getData(), CakeShopSearchResponse.class);

		assertEquals(HttpStatusCode.valueOf(200), responseEntity.getStatusCode());
		assertEquals(ReturnCode.SUCCESS.getCode(), response.getReturnCode());
		assertEquals(ReturnCode.SUCCESS.getMessage(), response.getReturnMessage());

		assertEquals(10, data.size());
		data.cakeShops().forEach(cakeShop -> {
			assertThat(cakeShop.getCakeImageUrls().size()).isLessThanOrEqualTo(4);
			assertThat(cakeShop.getCakeShopId()).isNotNull();
			assertThat(cakeShop.getCakeShopName()).isNotNull();
			assertThat(cakeShop.getOperationDays()).isNotNull();
		});
	}
}
