package com.cakk.api.integration.shop;

import static org.assertj.core.api.Assertions.*;
import static org.junit.Assert.*;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.assertj.core.api.Assertions;
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

import net.jqwik.api.Arbitraries;

import com.cakk.api.common.annotation.TestWithDisplayName;
import com.cakk.api.common.base.IntegrationTest;
import com.cakk.api.dto.request.link.UpdateLinkRequest;
import com.cakk.api.dto.request.operation.ShopOperationParam;
import com.cakk.api.dto.request.operation.UpdateShopOperationRequest;
import com.cakk.api.dto.request.shop.CreateShopRequest;
import com.cakk.api.dto.request.shop.UpdateShopAddressRequest;
import com.cakk.api.dto.request.shop.UpdateShopRequest;
import com.cakk.api.dto.request.user.CertificationRequest;
import com.cakk.api.dto.response.cake.CakeImageListResponse;
import com.cakk.api.dto.response.like.HeartResponse;
import com.cakk.api.dto.response.shop.CakeShopByMapResponse;
import com.cakk.api.dto.response.shop.CakeShopByMineResponse;
import com.cakk.api.dto.response.shop.CakeShopCreateResponse;
import com.cakk.api.dto.response.shop.CakeShopDetailResponse;
import com.cakk.api.dto.response.shop.CakeShopInfoResponse;
import com.cakk.api.dto.response.shop.CakeShopOwnerResponse;
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
import com.cakk.domain.redis.repository.CakeShopViewsRedisRepository;
import com.cakk.domain.redis.repository.CakeViewsRedisRepository;

@SqlGroup({
	@Sql(scripts = {
		"/sql/insert-test-user.sql",
		"/sql/insert-cake-shop.sql",
		"/sql/insert-heart.sql"
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

	@Autowired
	private CakeViewsRedisRepository cakeViewsRedisRepository;

	@Autowired
	private CakeShopViewsRedisRepository cakeShopViewsRedisRepository;

	private void initializeViews() {
		cakeShopViewsRedisRepository.saveOrIncreaseSearchCount(1L);
		cakeShopViewsRedisRepository.saveOrIncreaseSearchCount(1L);
		cakeShopViewsRedisRepository.saveOrIncreaseSearchCount(1L);
		cakeShopViewsRedisRepository.saveOrIncreaseSearchCount(1L);
		cakeShopViewsRedisRepository.saveOrIncreaseSearchCount(2L);
		cakeShopViewsRedisRepository.saveOrIncreaseSearchCount(2L);
		cakeShopViewsRedisRepository.saveOrIncreaseSearchCount(2L);
		cakeShopViewsRedisRepository.saveOrIncreaseSearchCount(3L);
		cakeShopViewsRedisRepository.saveOrIncreaseSearchCount(3L);
		cakeShopViewsRedisRepository.saveOrIncreaseSearchCount(4L);
		cakeShopViewsRedisRepository.saveOrIncreaseSearchCount(5L);
		cakeShopViewsRedisRepository.saveOrIncreaseSearchCount(6L);
		cakeShopViewsRedisRepository.saveOrIncreaseSearchCount(7L);
		cakeShopViewsRedisRepository.saveOrIncreaseSearchCount(7L);
		cakeShopViewsRedisRepository.saveOrIncreaseSearchCount(1L);
		cakeShopViewsRedisRepository.saveOrIncreaseSearchCount(7L);
		cakeShopViewsRedisRepository.saveOrIncreaseSearchCount(8L);
		cakeShopViewsRedisRepository.saveOrIncreaseSearchCount(9L);
	}

	@AfterEach
	void setUp() {
		cakeViewsRedisRepository.clear();
		cakeShopViewsRedisRepository.clear();
	}

	@TestWithDisplayName("백 오피스 API, 케이크샵 생성에 성공한다")
	void backOfficeCreateCakeShop() {
		final String url = "%s%d%s".formatted(BASE_URL, port, API_URL);
		final UriComponents uriComponents = UriComponentsBuilder
			.fromUriString(url)
			.path("/admin/create")
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

	@TestWithDisplayName("케이크 샵을 간단 조회 시, 케이크 조회수를 올리는 데 성공한다.")
	void simple2() {
		final String url = "%s%d%s".formatted(BASE_URL, port, API_URL);
		final Long cakeShopId = 1L;
		final Long cakeId = 1L;
		final UriComponents uriComponents = UriComponentsBuilder
			.fromUriString(url)
			.path("/{cakeShopId}/simple")
			.queryParam("cakeId", cakeId)
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

		Long viewCakeId = cakeViewsRedisRepository.findTopCakeIdsByOffsetAndCount(0, 10).get(0);
		assertEquals(cakeId, viewCakeId);
	}

	@TestWithDisplayName("없는 케이크 샵일 경우, 간단 조회에 실패한다.")
	void simple3() {
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

		final ResponseEntity<ApiResponse> responseEntity = restTemplate.postForEntity(uriComponents.toUriString(), entity,
			ApiResponse.class);
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

		final ResponseEntity<ApiResponse> responseEntity = restTemplate.postForEntity(uriComponents.toUriString(), entity,
			ApiResponse.class);
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
			assertThat(cakeShop.cakeShopId()).isIn(1L, 2L, 3L);
			assertThat(cakeShop.cakeImageUrls().size()).isLessThanOrEqualTo(4);
			assertThat(cakeShop.cakeShopName()).isNotNull();
		});
	}

	@TestWithDisplayName("해당 id의 이전에 하트 누른 케이크샵에 대하여 하트 상태인지 조회에 성공한다.")
	void isHeartShop1() {
		// given
		final Long cakeShopId = 1L;
		final String url = "%s%d%s/{cakeShopId}/heart".formatted(BASE_URL, port, API_URL);
		final UriComponents uriComponents = UriComponentsBuilder
			.fromUriString(url)
			.buildAndExpand(cakeShopId);

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

	@TestWithDisplayName("해당 id의 이전에 하트 누르지 않은 케이크샵에 대하여 하트 상태인지 조회에 성공한다.")
	void isHeartCake2() {
		// given
		final Long cakeId = 2L;
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

	@TestWithDisplayName("해당 id의 케이크 샵 하트에 성공한다.")
	void heartCakeShop() {
		// given
		final Long cakeShopId = 2L;
		final String url = "%s%d%s/{cakeShopId}/heart".formatted(BASE_URL, port, API_URL);
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

	@TestWithDisplayName("해당 id의 케이크 샵 하트 취소에 성공한다.")
	void heartCancelCake() {
		// given
		final Long cakeShopId = 1L;
		final String url = "%s%d%s/{cakeShopId}/heart".formatted(BASE_URL, port, API_URL);
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

	@TestWithDisplayName("해당 id의 케이크 샵 좋아요에 성공한다.")
	void likeCakeShop() {
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
			assertThat(cakeShop.cakeShopId()).isIn(4L, 5L, 6L, 7L, 8L, 9L, 10L);
			assertThat(cakeShop.cakeImageUrls().size()).isLessThanOrEqualTo(4);
			assertThat(cakeShop.cakeShopName()).isNotNull();
		});
	}

	@TestWithDisplayName("테스트 sql script 기준 7개의 케이크 샵이 조회된다")
	void searchCakeShopsByKeywordsWithConditions1() {
		final String url = "%s%d%s/search/shops".formatted(BASE_URL, port, API_URL);
		final UriComponents uriComponents = UriComponentsBuilder
			.fromUriString(url)
			.queryParam("cakeShopId", 11L)
			.queryParam("latitude", 37.543343)
			.queryParam("longitude", 127.052609)
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

		assertEquals(7, data.size());
		data.cakeShops().forEach(cakeShop -> {
			assertThat(cakeShop.cakeImageUrls().size()).isLessThanOrEqualTo(4);
			assertThat(cakeShop.cakeShopId()).isNotNull();
			assertThat(cakeShop.cakeShopName()).isNotNull();
			assertThat(cakeShop.operationDays()).isNotNull();
		});
	}

	@TestWithDisplayName("테스트 sql script를 기준 3개의 케이크샵이 조회된다")
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

		assertEquals(3, data.size());
		data.cakeShops().forEach(cakeShop -> {
			assertThat(cakeShop.cakeImageUrls().size()).isLessThanOrEqualTo(4);
			assertThat(cakeShop.cakeShopId()).isNotNull();
			assertThat(cakeShop.cakeShopName()).isNotNull();
			assertThat(cakeShop.operationDays()).isNotNull();
		});
	}

	@TestWithDisplayName("테스트 sql script를 기준 4개의 케이크샵이 조회된다")
	void searchCakeShopsByKeywordWithConditions3() {
		final String url = "%s%d%s/search/shops".formatted(BASE_URL, port, API_URL);
		final UriComponents uriComponents = UriComponentsBuilder
			.fromUriString(url)
			.queryParam("cakeShopId", 5)
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

		assertEquals(4, data.size());
		data.cakeShops().forEach(cakeShop -> {
			assertThat(cakeShop.cakeImageUrls().size()).isLessThanOrEqualTo(4);
			assertThat(cakeShop.cakeShopId()).isNotNull();
			assertThat(cakeShop.cakeShopName()).isNotNull();
			assertThat(cakeShop.operationDays()).isNotNull();
		});
	}

	@TestWithDisplayName("케이크 샵 기본 정보 업데이트에 성공한다")
	void updateCakeShopDefaultInfo() {
		// given
		final Long cakeShopId = 1L;
		final String url = "%s%d%s/{cakeShopId}".formatted(BASE_URL, port, API_URL);
		final UriComponents uriComponents = UriComponentsBuilder
			.fromUriString(url)
			.buildAndExpand(cakeShopId);
		final UpdateShopRequest request = getConstructorMonkey().giveMeBuilder(UpdateShopRequest.class).sample();

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
	}

	@TestWithDisplayName("어드민에 의해 케이크 샵 기본 정보 업데이트에 성공한다")
	void updateCakeShopDefaultInfoByAdmin() {
		// given
		final Long cakeShopId = 1L;
		final String url = "%s%d%s/{cakeShopId}".formatted(BASE_URL, port, API_URL);
		final UriComponents uriComponents = UriComponentsBuilder
			.fromUriString(url)
			.buildAndExpand(cakeShopId);
		final UpdateShopRequest request = getConstructorMonkey().giveMeBuilder(UpdateShopRequest.class).sample();

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
	}

	@TestWithDisplayName("케이크샵 외부 링크 업데이트에 성공한다")
	void updateShopLinks() {
		// given
		final Long cakeShopId = 1L;
		final String url = "%s%d%s/{cakeShopId}/links".formatted(BASE_URL, port, API_URL);
		final UriComponents uriComponents = UriComponentsBuilder
			.fromUriString(url)
			.buildAndExpand(cakeShopId);
		final UpdateLinkRequest request = getConstructorMonkey().giveMeBuilder(UpdateLinkRequest.class).sample();

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
	}

	@TestWithDisplayName("어드민에 의해 케이크샵 외부 링크 업데이트에 성공한다")
	void updateShopLinksByAdmin() {
		// given
		final Long cakeShopId = 1L;
		final String url = "%s%d%s/{cakeShopId}/links".formatted(BASE_URL, port, API_URL);
		final UriComponents uriComponents = UriComponentsBuilder
			.fromUriString(url)
			.buildAndExpand(cakeShopId);
		final UpdateLinkRequest request = getConstructorMonkey().giveMeBuilder(UpdateLinkRequest.class).sample();

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
	}

	@TestWithDisplayName("케이크 샵 주소 업데이트에 성공한다")
	void updateShopAddress() {
		// given
		final Long cakeShopId = 1L;
		final String url = "%s%d%s/{cakeShopId}/address".formatted(BASE_URL, port, API_URL);
		final UriComponents uriComponents = UriComponentsBuilder
			.fromUriString(url)
			.buildAndExpand(cakeShopId);
		final UpdateShopAddressRequest request = getConstructorMonkey().giveMeBuilder(UpdateShopAddressRequest.class).sample();

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
	}

	@TestWithDisplayName("어드민에 의해 케이크 샵 주소 업데이트에 성공한다")
	void updateShopAddressByAdmin() {
		// given
		final Long cakeShopId = 1L;
		final String url = "%s%d%s/{cakeShopId}/address".formatted(BASE_URL, port, API_URL);
		final UriComponents uriComponents = UriComponentsBuilder
			.fromUriString(url)
			.buildAndExpand(cakeShopId);
		final UpdateShopAddressRequest request = getConstructorMonkey().giveMeBuilder(UpdateShopAddressRequest.class).sample();

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
	}

	@TestWithDisplayName("케이크 샵 영업일 업데이트에 성공한다")
	void updateShopOperationDays() {
		// given
		final Long cakeShopId = 1L;
		final String url = "%s%d%s/{cakeShopId}/operation-days".formatted(BASE_URL, port, API_URL);
		final UriComponents uriComponents = UriComponentsBuilder
			.fromUriString(url)
			.buildAndExpand(cakeShopId);
		List<ShopOperationParam> shopOperationParams = getConstructorMonkey().giveMeBuilder(
				ShopOperationParam.class)
			.setNotNull("operationDay")
			.setNotNull("operationStartTime")
			.setNotNull("operationEndTime").sampleList(6);
		final UpdateShopOperationRequest request = getConstructorMonkey().giveMeBuilder(UpdateShopOperationRequest.class)
			.set("operationDays", shopOperationParams)
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
	}

	@TestWithDisplayName("케이크 샵 영업일 업데이트에 성공한다")
	void updateShopOperationDaysByAdmin() {
		// given
		final Long cakeShopId = 1L;
		final String url = "%s%d%s/{cakeShopId}/operation-days".formatted(BASE_URL, port, API_URL);
		final UriComponents uriComponents = UriComponentsBuilder
			.fromUriString(url)
			.buildAndExpand(cakeShopId);
		List<ShopOperationParam> shopOperationParams = getConstructorMonkey().giveMeBuilder(
				ShopOperationParam.class)
			.setNotNull("operationDay")
			.setNotNull("operationStartTime")
			.setNotNull("operationEndTime").sampleList(6);
		final UpdateShopOperationRequest request = getConstructorMonkey().giveMeBuilder(UpdateShopOperationRequest.class)
			.set("operationDays", shopOperationParams)
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
	}

	@TestWithDisplayName("케이크 샵 사업자 연동 여부 True 확인에 성공한다")
	void isOwnedCakeShop1() {
		// given
		final Long cakeShopId = 1L;
		final String url = "%s%d%s/{cakeShopId}/owner".formatted(BASE_URL, port, API_URL);
		final UriComponents uriComponents = UriComponentsBuilder
			.fromUriString(url)
			.buildAndExpand(cakeShopId);

		// when
		final ResponseEntity<ApiResponse> responseEntity = restTemplate.exchange(
			uriComponents.toUriString(),
			HttpMethod.GET,
			new HttpEntity<>(getAuthHeader()),
			ApiResponse.class);

		// then
		final ApiResponse response = objectMapper.convertValue(responseEntity.getBody(), ApiResponse.class);
		final CakeShopOwnerResponse data = objectMapper.convertValue(response.getData(), CakeShopOwnerResponse.class);

		assertEquals(HttpStatusCode.valueOf(200), responseEntity.getStatusCode());
		assertEquals(ReturnCode.SUCCESS.getCode(), response.getReturnCode());
		assertEquals(ReturnCode.SUCCESS.getMessage(), response.getReturnMessage());
		assertEquals(true, data.isOwned());
	}

	@TestWithDisplayName("케이크 샵 사업자 연동 여부 True 확인에 성공한다")
	void isOwnedCakeShop2() {
		// given
		final Long cakeShopId = 2L;
		final String url = "%s%d%s/{cakeShopId}/owner".formatted(BASE_URL, port, API_URL);
		final UriComponents uriComponents = UriComponentsBuilder
			.fromUriString(url)
			.buildAndExpand(cakeShopId);

		// when
		final ResponseEntity<ApiResponse> responseEntity = restTemplate.exchange(
			uriComponents.toUriString(),
			HttpMethod.GET,
			new HttpEntity<>(getAuthHeader()),
			ApiResponse.class);

		// then
		final ApiResponse response = objectMapper.convertValue(responseEntity.getBody(), ApiResponse.class);
		final CakeShopOwnerResponse data = objectMapper.convertValue(response.getData(), CakeShopOwnerResponse.class);

		assertEquals(HttpStatusCode.valueOf(200), responseEntity.getStatusCode());
		assertEquals(ReturnCode.SUCCESS.getCode(), response.getReturnCode());
		assertEquals(ReturnCode.SUCCESS.getMessage(), response.getReturnMessage());
		assertEquals(false, data.isOwned());
	}

	@TestWithDisplayName("나의 케이크 샵 아이디 조회에 성공한다")
	void getMyShopIds() {
		// given
		final String url = "%s%d%s/mine".formatted(BASE_URL, port, API_URL);
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
		final CakeShopByMineResponse data = objectMapper.convertValue(response.getData(), CakeShopByMineResponse.class);

		assertEquals(HttpStatusCode.valueOf(200), responseEntity.getStatusCode());
		assertEquals(ReturnCode.SUCCESS.getCode(), response.getReturnCode());
		assertEquals(ReturnCode.SUCCESS.getMessage(), response.getReturnMessage());
		assertEquals(true, data.isExist());
		assertEquals(1L, data.cakeShopId().longValue());
	}

	@TestWithDisplayName("조회수로 케이크 샵 조회에 성공한다")
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
		final CakeShopSearchResponse data = objectMapper.convertValue(response.getData(), CakeShopSearchResponse.class);

		assertEquals(HttpStatusCode.valueOf(200), responseEntity.getStatusCode());
		assertEquals(ReturnCode.SUCCESS.getCode(), response.getReturnCode());
		assertEquals(ReturnCode.SUCCESS.getMessage(), response.getReturnMessage());

		assertEquals(4, data.size());
	}

	@TestWithDisplayName("조회한 케이크 샵이 없을 시, 인기 케이크 샵 조회에 빈 배열을 리턴한다")
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
		final CakeShopSearchResponse data = objectMapper.convertValue(response.getData(), CakeShopSearchResponse.class);

		assertEquals(HttpStatusCode.valueOf(200), responseEntity.getStatusCode());
		assertEquals(ReturnCode.SUCCESS.getCode(), response.getReturnCode());
		assertEquals(ReturnCode.SUCCESS.getMessage(), response.getReturnMessage());

		assertEquals(0, data.size());
	}
}
