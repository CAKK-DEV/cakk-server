package com.cakk.api.service.shop;

import static org.assertj.core.api.Assertions.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.DisplayName;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.context.ApplicationEventPublisher;

import net.jqwik.api.Arbitraries;

import com.navercorp.fixturemonkey.ArbitraryBuilder;

import com.cakk.api.common.annotation.TestWithDisplayName;
import com.cakk.api.common.base.ServiceTest;
import com.cakk.api.dto.request.shop.CakeShopSearchByViewsRequest;
import com.cakk.api.dto.request.shop.CreateShopRequest;
import com.cakk.api.dto.request.shop.PromotionRequest;
import com.cakk.api.dto.response.shop.CakeShopCreateResponse;
import com.cakk.api.dto.response.shop.CakeShopDetailResponse;
import com.cakk.api.dto.response.shop.CakeShopInfoResponse;
import com.cakk.api.dto.response.shop.CakeShopSearchResponse;
import com.cakk.api.dto.response.shop.CakeShopSimpleResponse;
import com.cakk.api.mapper.PointMapper;
import com.cakk.api.mapper.ShopMapper;
import com.cakk.common.enums.ReturnCode;
import com.cakk.common.exception.CakkException;
import com.cakk.domain.mysql.bo.VerificationPolicy;
import com.cakk.domain.mysql.dto.param.shop.CakeShopDetailParam;
import com.cakk.domain.mysql.dto.param.shop.CakeShopInfoParam;
import com.cakk.domain.mysql.dto.param.shop.CakeShopSimpleParam;
import com.cakk.domain.mysql.dto.param.user.CertificationParam;
import com.cakk.domain.mysql.entity.shop.CakeShop;
import com.cakk.domain.mysql.entity.user.BusinessInformation;
import com.cakk.domain.mysql.entity.user.User;
import com.cakk.domain.mysql.event.shop.CertificationEvent;
import com.cakk.domain.mysql.repository.reader.CakeShopReader;
import com.cakk.domain.mysql.repository.reader.UserReader;
import com.cakk.domain.mysql.repository.writer.CakeShopWriter;
import com.cakk.domain.redis.repository.CakeShopViewsRedisRepository;

@DisplayName("케이크 샵 조회 관련 비즈니스 로직 테스트")
public class ShopServiceTest extends ServiceTest {

	@InjectMocks
	private ShopService shopService;

	@Mock
	private UserReader userReader;

	@Mock
	private CakeShopReader cakeShopReader;

	@Mock
	private CakeShopWriter cakeShopWriter;

	@Mock
	private CakeShopViewsRedisRepository cakeShopViewsRedisRepository;

	@Mock
	private ApplicationEventPublisher publisher;

	@Mock
	private VerificationPolicy verificationPolicy;

	private CreateShopRequest getCreateShopRequestFixture() {
		return getConstructorMonkey().giveMeBuilder(CreateShopRequest.class)
			.set("businessNumber", Arbitraries.strings().withCharRange('a', 'z').ofMinLength(1).ofMaxLength(7))
			.setNotNull("operationDays")
			.set("shopName", Arbitraries.strings().withCharRange('a', 'z').ofMinLength(1).ofMaxLength(20))
			.set("shopBio", Arbitraries.strings().withCharRange('a', 'z').ofMaxLength(20))
			.set("shopDescription", Arbitraries.strings().withCharRange('a', 'z').ofMaxLength(20))
			.sample();
	}

	private PromotionRequest getPromotionRequestFixture() {
		return getConstructorMonkey().giveMeBuilder(PromotionRequest.class)
			.set("userId", Arbitraries.longs().greaterOrEqual(0))
			.set("cakeShopId", Arbitraries.longs().greaterOrEqual(0))
			.sample();
	}

	private CertificationParam getCertificationParamFixture(boolean isNull) {
		ArbitraryBuilder<CertificationParam> builder = getConstructorMonkey().giveMeBuilder(CertificationParam.class);
		if (isNull) {
			return builder.setNull("cakeShopId").setNotNull("user").sample();
		}
		return builder.setNotNull("cakeShopId").setNotNull("user").sample();
	}

	private CakeShop getCakeShopFixture() {
		return getConstructorMonkey().giveMeBuilder(CakeShop.class)
			.set("id", Arbitraries.longs().greaterOrEqual(1))
			.set("shopName", Arbitraries.strings().withCharRange('a', 'z').ofMaxLength(30))
			.set("shopBio", Arbitraries.strings().withCharRange('a', 'z').ofMaxLength(40))
			.set("shopDescription", Arbitraries.strings().withCharRange('a', 'z').ofMaxLength(500))
			.set("location", supplyPointBy(
				Arbitraries.doubles().between(-90, 90).sample(),
				Arbitraries.doubles().between(-180, 180).sample()))
			.sample();
	}

	private BusinessInformation getBusinessInformationFixture() {
		return getConstructorMonkey().giveMeBuilder(BusinessInformation.class)
			.set("businessNumber", Arbitraries.strings().withCharRange('a', 'z').ofMaxLength(20))
			.set("cakeShop", getCakeShopFixture())
			.sample();
	}

	@TestWithDisplayName("id로 케이크 샵을 간단조회 한다.")
	void searchSimpleById1() {
		// given
		Long cakeShopId = 1L;
		CakeShopSimpleParam response = getConstructorMonkey().giveMeBuilder(CakeShopSimpleParam.class)
			.set("cakeShopId", Arbitraries.longs().greaterOrEqual(10))
			.set("thumbnailUrl", Arbitraries.strings().alpha().ofMinLength(100).ofMaxLength(200))
			.set("cakeShopName", Arbitraries.strings().alpha().ofMinLength(1).ofMaxLength(30))
			.set("cakeShopBio", Arbitraries.strings().alpha().ofMinLength(1).ofMaxLength(40))
			.sample();

		doReturn(response).when(cakeShopReader).searchSimpleById(cakeShopId);

		// when
		CakeShopSimpleResponse result = shopService.searchSimpleById(cakeShopId);

		// then
		assertEquals(ShopMapper.cakeShopSimpleResponseFromParam(response), result);

		verify(cakeShopReader, times(1)).searchSimpleById(cakeShopId);
	}

	@TestWithDisplayName("id에 해당하는 케이크 샵이 없으면 간단조회 시, 에러를 반환한다.")
	void searchSimpleById2() {
		// given
		Long cakeShopId = 1L;

		doThrow(new CakkException(ReturnCode.NOT_EXIST_CAKE_SHOP)).when(cakeShopReader).searchSimpleById(cakeShopId);

		// then
		assertThatThrownBy(
			() -> shopService.searchSimpleById(cakeShopId))
			.isInstanceOf(CakkException.class)
			.hasMessageContaining(ReturnCode.NOT_EXIST_CAKE_SHOP.getMessage());

		verify(cakeShopReader, times(1)).searchSimpleById(cakeShopId);
	}

	@TestWithDisplayName("id로 케이크 샵을 상세조회 한다.")
	void searchDetailById1() {
		// given
		Long cakeShopId = 1L;
		CakeShopDetailParam param = getConstructorMonkey().giveMeBuilder(CakeShopDetailParam.class)
			.set("cakeShopId", Arbitraries.longs().greaterOrEqual(10))
			.set("shopName", Arbitraries.strings().alpha().ofMinLength(1).ofMaxLength(30))
			.set("thumbnailUrl", Arbitraries.strings().alpha().ofMinLength(100).ofMaxLength(200))
			.set("shopBio", Arbitraries.strings().alpha().ofMinLength(1).ofMaxLength(40))
			.set("shopDescription", Arbitraries.strings().alpha().ofMinLength(1).ofMaxLength(500))
			.set("operationDays", Set.of())
			.set("links", Set.of())
			.sample();

		doReturn(param).when(cakeShopReader).searchDetailById(cakeShopId);

		// when
		CakeShopDetailResponse result = shopService.searchDetailById(cakeShopId);

		// then
		assertEquals(ShopMapper.cakeShopDetailResponseFromParam(param), result);

		verify(cakeShopReader, times(1)).searchDetailById(cakeShopId);
	}

	@TestWithDisplayName("id에 해당하는 케이크 샵이 없으면 상세조회 시, 에러를 반환한다.")
	void searchDetailById2() {
		// given
		Long cakeShopId = 1L;

		doThrow(new CakkException(ReturnCode.NOT_EXIST_CAKE_SHOP)).when(cakeShopReader).searchDetailById(cakeShopId);

		// then
		assertThatThrownBy(
			() -> shopService.searchDetailById(cakeShopId))
			.isInstanceOf(CakkException.class)
			.hasMessageContaining(ReturnCode.NOT_EXIST_CAKE_SHOP.getMessage());

		verify(cakeShopReader, times(1)).searchDetailById(cakeShopId);
	}

	@TestWithDisplayName("Admin에 의해 케이크 샵을 생성하는데 성공한다")
	void createCakeShop() {
		//given
		CreateShopRequest request = getCreateShopRequestFixture();
		CakeShop cakeShop = getCakeShopFixture();
		when(cakeShopWriter.createCakeShop(any(CakeShop.class), anyList(), any(BusinessInformation.class), anyList()))
			.thenReturn(cakeShop);

		//when
		final CakeShopCreateResponse response = shopService.createCakeShopByCertification(request);

		//verify
		assertEquals(response.cakeShopId(), cakeShop.getId());
		verify(cakeShopWriter, times(1))
			.createCakeShop(any(CakeShop.class), anyList(), any(BusinessInformation.class), anyList());
	}

	@TestWithDisplayName("userId와 cakeShopId가 존재한다면, 해당 userId의 사용자는 Owner로 승격된다")
	void promoteUser() {
		//given
		PromotionRequest request = getPromotionRequestFixture();
		BusinessInformation businessInformation = getBusinessInformationFixture();

		doReturn(getConstructorMonkey().giveMeOne(User.class)).when(userReader).findByUserId(request.userId());
		doReturn(businessInformation).when(cakeShopReader).findBusinessInformationWithShop(request.cakeShopId());

		//when,then
		shopService.promoteUserToBusinessOwner(request);

		//verify
		verify(userReader, times(1)).findByUserId(request.userId());
		verify(cakeShopReader, times(1)).findBusinessInformationWithShop(request.cakeShopId());
	}

	@TestWithDisplayName("cakeShopId가 존재한다면, 정보를 찾아서 이벤트를 발행한다")
	void requestCertificationEventWithInfo() {
		//given
		CertificationParam param = getCertificationParamFixture(false);
		doReturn(getBusinessInformationFixture())
			.when(cakeShopReader).findBusinessInformationByCakeShopId(param.cakeShopId());

		//when
		shopService.requestCertificationBusinessOwner(param);

		//verify
		verify(cakeShopReader, times(1)).findBusinessInformationByCakeShopId(param.cakeShopId());
		verify(publisher, times(1)).publishEvent(any(CertificationEvent.class));
	}

	@TestWithDisplayName("cakeShopId가 존재하지 않는다면, 요청 정보로만 이벤트를 발행한다")
	void requestCertificationEventWithParam() {
		//given
		CertificationParam param = getCertificationParamFixture(true);

		//when
		shopService.requestCertificationBusinessOwner(param);

		//verify
		verify(cakeShopReader, times(0)).findBusinessInformationByCakeShopId(any());
		verify(publisher, times(1)).publishEvent(any(CertificationEvent.class));
	}

	@TestWithDisplayName("id로 케이크 샵을 상세정보를 조회한다.")
	void searchInfoById1() {
		// given
		Long cakeShopId = 1L;
		CakeShopInfoParam param = getConstructorMonkey().giveMeBuilder(CakeShopInfoParam.class)
			.set("shopAddress", Arbitraries.strings().alpha().ofMinLength(1).ofMaxLength(30))
			.set("point",
				PointMapper.supplyPointBy(
					Arbitraries.doubles().greaterOrEqual(0).lessOrEqual(100).sample(),
					Arbitraries.doubles().greaterOrEqual(0).lessOrEqual(100).sample()
				)
			)
			.set("operationDays", List.of())
			.sample();

		doReturn(param).when(cakeShopReader).searchInfoById(cakeShopId);

		// when
		CakeShopInfoResponse result = shopService.searchInfoById(cakeShopId);

		// then
		assertEquals(ShopMapper.supplyCakeShopInfoResponseBy(param), result);

		verify(cakeShopReader, times(1)).searchInfoById(cakeShopId);
	}

	@TestWithDisplayName("id에 해당하는 케이크 샵이 없으면 상세조회 시, 에러를 반환한다.")
	void searchInfoById2() {
		// given
		Long cakeShopId = 1L;

		doThrow(new CakkException(ReturnCode.NOT_EXIST_CAKE_SHOP)).when(cakeShopReader).searchInfoById(cakeShopId);

		// then
		assertThatThrownBy(
			() -> shopService.searchInfoById(cakeShopId))
			.isInstanceOf(CakkException.class)
			.hasMessageContaining(ReturnCode.NOT_EXIST_CAKE_SHOP.getMessage());

		verify(cakeShopReader, times(1)).searchInfoById(cakeShopId);
	}

	@TestWithDisplayName("인기 케이크 샵 목록을 조회한다.")
	void searchCakeShopsByCursorAndViews1() {
		// given
		final long offset = 0L;
		final int pageSize = 3;
		final CakeShopSearchByViewsRequest dto = new CakeShopSearchByViewsRequest(offset, pageSize);
		final List<Long> cakeShopIds = List.of(1L, 2L, 3L);
		final List<CakeShop> cakeShops = getConstructorMonkey().giveMeBuilder(CakeShop.class)
			.set("cakeShopId", Arbitraries.longs().greaterOrEqual(1))
			.set("thumbnailUrl", Arbitraries.strings().alpha().ofMinLength(100).ofMaxLength(200))
			.set("cakeShopName", Arbitraries.strings().alpha().ofMinLength(1).ofMaxLength(30))
			.set("cakeShopBio", Arbitraries.strings().alpha().ofMinLength(1).ofMaxLength(40))
			.sampleList(3);

		doReturn(cakeShopIds).when(cakeShopViewsRedisRepository).findTopShopIdsByOffsetAndCount(offset, pageSize);
		doReturn(cakeShops).when(cakeShopReader).searchShopsByShopIds(cakeShopIds);

		// when
		final CakeShopSearchResponse result = shopService.searchCakeShopsByCursorAndViews(dto);

		// then
		assertNotNull(result);
		assertEquals(cakeShops.size(), result.size());

		verify(cakeShopViewsRedisRepository, times(1)).findTopShopIdsByOffsetAndCount(offset, pageSize);
		verify(cakeShopReader, times(1)).searchShopsByShopIds(cakeShopIds);
	}

	@TestWithDisplayName("인기 케이크 샵 목록이 없는 경우, 빈 배열을 조회한다.")
	void searchCakeShopsByCursorAndViews2() {
		// given
		final long offset = 0L;
		final int pageSize = 3;
		final CakeShopSearchByViewsRequest dto = new CakeShopSearchByViewsRequest(offset, pageSize);
		final List<Long> cakeShopIds = List.of();

		doReturn(cakeShopIds).when(cakeShopViewsRedisRepository).findTopShopIdsByOffsetAndCount(offset, pageSize);

		// when
		final CakeShopSearchResponse result = shopService.searchCakeShopsByCursorAndViews(dto);

		// then
		assertNotNull(result);
		assertThat(result.cakeShops()).isEmpty();

		verify(cakeShopViewsRedisRepository, times(1)).findTopShopIdsByOffsetAndCount(offset, pageSize);
		verify(cakeShopReader, times(0)).searchShopsByShopIds(cakeShopIds);
	}
}
