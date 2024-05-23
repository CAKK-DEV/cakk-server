package com.cakk.api.service.shop;

import static org.assertj.core.api.Assertions.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import org.junit.jupiter.api.DisplayName;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.context.ApplicationEventPublisher;

import net.jqwik.api.Arbitraries;
import com.cakk.api.common.annotation.TestWithDisplayName;
import com.cakk.api.common.base.ServiceTest;
import com.cakk.common.enums.ReturnCode;
import com.cakk.common.exception.CakkException;
import com.cakk.domain.dto.param.shop.CakeShopSimpleResponse;
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

	@TestWithDisplayName("id로 케이크 샵을 간단조회 한다.")
	void searchSimpleById1() {
		// given
		Long cakeShopId = 1L;
		CakeShopSimpleResponse response = getConstructorMonkey().giveMeBuilder(CakeShopSimpleResponse.class)
			.set("cakeShopId", Arbitraries.longs().greaterOrEqual(10))
			.set("thumbnailUrl", Arbitraries.strings().alpha().ofMinLength(100).ofMaxLength(200))
			.set("cakeShopName", Arbitraries.strings().alpha().ofMinLength(1).ofMaxLength(30))
			.set("cakeShopBio", Arbitraries.strings().alpha().ofMinLength(1).ofMaxLength(40))
			.sample();

		doReturn(response).when(cakeShopReader).searchSimpleById(cakeShopId);

		// when
		CakeShopSimpleResponse result = shopService.searchSimpleById(cakeShopId);

		// then
		assertEquals(response, result);

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
}
