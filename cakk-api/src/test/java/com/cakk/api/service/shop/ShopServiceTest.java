package com.cakk.api.service.shop;

import static org.assertj.core.api.Assertions.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Set;

import org.junit.jupiter.api.DisplayName;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.context.ApplicationEventPublisher;

import net.jqwik.api.Arbitraries;

import com.cakk.api.common.annotation.TestWithDisplayName;
import com.cakk.api.common.base.ServiceTest;
import com.cakk.api.dto.request.shop.CreateShopRequest;
import com.cakk.api.dto.response.shop.CakeShopDetailResponse;
import com.cakk.api.dto.response.shop.CakeShopSimpleResponse;
import com.cakk.api.mapper.ShopMapper;
import com.cakk.common.enums.ReturnCode;
import com.cakk.common.exception.CakkException;
import com.cakk.domain.dto.param.shop.CakeShopDetailParam;
import com.cakk.domain.entity.shop.CakeShop;
import com.cakk.domain.entity.user.BusinessInformation;
import com.cakk.domain.dto.param.shop.CakeShopSimpleParam;
import com.cakk.domain.repository.reader.CakeShopReader;
import com.cakk.domain.repository.reader.UserReader;
import com.cakk.domain.repository.writer.CakeShopWriter;

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
	private ApplicationEventPublisher publisher;

	private CreateShopRequest getCreateShopRequestFixture() {
		return getConstructorMonkey().giveMeBuilder(CreateShopRequest.class)
			.set("businessNumber", Arbitraries.strings().withCharRange('a', 'z').ofMinLength(1).ofMaxLength(7))
			.set("shopName", Arbitraries.strings().withCharRange('a', 'z').ofMinLength(1).ofMaxLength(20))
			.set("shopBio", Arbitraries.strings().withCharRange('a', 'z').ofMaxLength(20))
			.set("shopDescription", Arbitraries.strings().withCharRange('a', 'z').ofMaxLength(20))
			.set("startTimes", Arbitraries.of(LocalTime.now()).list().ofSize(3))
			.set("endTimes", Arbitraries.of(LocalTime.now()).list().ofSize(3))
			.set("operationsDays", Arbitraries.of(Days.class).list().ofSize(3))
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
		System.out.println(request);

		//when
		shopService.createCakeShopByCertification(request);

		//verify
		verify(cakeShopWriter, times(1))
			.createCakeShop(any(CakeShop.class), anyList(), any(BusinessInformation.class));
	}
}
