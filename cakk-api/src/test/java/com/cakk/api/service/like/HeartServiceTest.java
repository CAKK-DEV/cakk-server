package com.cakk.api.service.like;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import net.jqwik.api.Arbitraries;

import com.cakk.api.common.annotation.TestWithDisplayName;
import com.cakk.api.common.base.ServiceTest;
import com.cakk.common.enums.ReturnCode;
import com.cakk.common.exception.CakkException;
import com.cakk.core.dto.param.search.HeartCakeSearchParam;
import com.cakk.core.dto.response.like.HeartCakeImageListResponse;
import com.cakk.core.facade.cake.CakeReadFacade;
import com.cakk.core.facade.cake.CakeShopReadFacade;
import com.cakk.core.facade.cake.CakeShopUserReadFacade;
import com.cakk.core.facade.user.UserHeartFacade;
import com.cakk.domain.mysql.dto.param.like.HeartCakeImageResponseParam;
import com.cakk.domain.mysql.entity.cake.Cake;
import com.cakk.domain.mysql.entity.shop.CakeShop;
import com.cakk.domain.mysql.entity.user.User;

@DisplayName("하트 기능 관련 비즈니스 로직 테스트")
class HeartServiceTest extends ServiceTest {

	@InjectMocks
	private HeartService heartService;

	@Mock
	private CakeReadFacade cakeReadFacade;

	@Mock
	private CakeShopReadFacade cakeShopReadFacade;

	@Mock
	private CakeShopUserReadFacade cakeShopUserReadFacade;

	@Mock
	private UserHeartFacade userHeartFacade;


	@TestWithDisplayName("하트 한 케이크 목록을 조회한다.")
	void findCakeImagesByCursorAndHeart() {
		final User user = getUser();
		final HeartCakeSearchParam param = new HeartCakeSearchParam(null, 5, user);
		final List<HeartCakeImageResponseParam> cakeImages =
			getConstructorMonkey().giveMeBuilder(HeartCakeImageResponseParam.class)
				.set("cakeShopId", Arbitraries.longs().greaterOrEqual(1))
				.set("cakeId", Arbitraries.longs().greaterOrEqual(1))
				.set("cakeHeartId", Arbitraries.longs().greaterOrEqual(1))
				.set("cakeImageUrl", Arbitraries.strings().alpha().ofMinLength(10).ofMaxLength(20))
				.sampleList(5);

		doReturn(cakeImages)
			.when(cakeShopUserReadFacade)
			.searchCakeImagesByCursorAndHeart(param.getCakeHeartId(), user.getId(), param.getPageSize());

		// when
		final HeartCakeImageListResponse result = heartService.searchCakeImagesByCursorAndHeart(param);

		// then
		Assertions.assertEquals(cakeImages, result.getCakeImages());
		Assertions.assertNotNull(result.getLastCakeHeartId());

		verify(cakeShopUserReadFacade, times(1))
			.searchCakeImagesByCursorAndHeart(param.getCakeHeartId(), user.getId(), param.getPageSize());
	}

	@TestWithDisplayName("하트 한 케이크 목록 n번째 페이지를 조회한다.")
	void findCakeImagesByCursorAndHeart2() {
		final User user = getUser();
		final HeartCakeSearchParam param = new HeartCakeSearchParam(12L, 5, user);
		final List<HeartCakeImageResponseParam> cakeImages =
			getConstructorMonkey().giveMeBuilder(HeartCakeImageResponseParam.class)
				.set("cakeShopId", Arbitraries.longs().greaterOrEqual(1))
				.set("cakeId", Arbitraries.longs().greaterOrEqual(1))
				.set("cakeHeartId", Arbitraries.longs().between(1, 11))
				.set("cakeImageUrl", Arbitraries.strings().alpha().ofMinLength(10).ofMaxLength(20))
				.sampleList(5);

		doReturn(cakeImages)
			.when(cakeShopUserReadFacade)
			.searchCakeImagesByCursorAndHeart(param.getCakeHeartId(), user.getId(), param.getPageSize());

		// when
		final HeartCakeImageListResponse result = heartService.searchCakeImagesByCursorAndHeart(param);

		// then
		Assertions.assertEquals(cakeImages, result.getCakeImages());
		Assertions.assertNotNull(result.getLastCakeHeartId());

		verify(cakeShopUserReadFacade, times(1))
			.searchCakeImagesByCursorAndHeart(param.getCakeHeartId(), user.getId(), param.getPageSize());
	}

	@TestWithDisplayName("하트 한 케이크가 없을 때 목록 조회 시 빈 배열을 반환한다.")
	void findCakeImagesByCursorAndHeart3() {
		final User user = getUser();
		final HeartCakeSearchParam param = new HeartCakeSearchParam(5L, 5, user);

		doReturn(List.of())
			.when(cakeShopUserReadFacade)
			.searchCakeImagesByCursorAndHeart(param.getCakeHeartId(), user.getId(), param.getPageSize());

		// when
		final HeartCakeImageListResponse result = heartService.searchCakeImagesByCursorAndHeart(param);

		// then
		Assertions.assertEquals(0, result.getCakeImages().size());
		Assertions.assertNull(result.getLastCakeHeartId());

		verify(cakeShopUserReadFacade, times(1))
			.searchCakeImagesByCursorAndHeart(param.getCakeHeartId(), user.getId(), param.getPageSize());
	}

	@TestWithDisplayName("케이크에 대하여 하트를 동작한다.")
	void heartCake1() {
		// given
		final User user = getUser();
		final Long cakeId = 1L;
		final Cake cake = getConstructorMonkey().giveMeOne(Cake.class);

		doReturn(cake).when(cakeReadFacade).findByIdWithHeart(cakeId);
		doNothing().when(userHeartFacade).heartCake(user, cake);

		// when & then
		assertDoesNotThrow(() -> heartService.heartCake(user, cakeId));

		verify(cakeReadFacade, times(1)).findByIdWithHeart(cakeId);
		verify(userHeartFacade, times(1)).heartCake(user, cake);
	}

	@TestWithDisplayName("케이크에 대하여 하트 취소를 동작한다.")
	void heartCake2() {
		// given
		final User user = getUser();
		final Long cakeId = 1L;
		final Cake cake = getConstructorMonkey().giveMeOne(Cake.class);
		cake.heart(user);

		doReturn(cake).when(cakeReadFacade).findByIdWithHeart(cakeId);
		doNothing().when(userHeartFacade).heartCake(user, cake);

		// when & then
		assertDoesNotThrow(() -> heartService.heartCake(user, cakeId));

		verify(cakeReadFacade, times(1)).findByIdWithHeart(cakeId);
		verify(userHeartFacade, times(1)).heartCake(user, cake);
	}

	@TestWithDisplayName("해당 케이크가 없으면 하트 동작을 실패한다.")
	void heartCake3() {
		// given
		final User user = getUser();
		final Long cakeId = 1L;

		doThrow(new CakkException(ReturnCode.NOT_EXIST_CAKE)).when(cakeReadFacade).findByIdWithHeart(cakeId);

		// when & then
		assertThrows(
			CakkException.class,
			() -> heartService.heartCake(user, cakeId),
			ReturnCode.NOT_EXIST_CAKE.getMessage());

		verify(cakeReadFacade, times(1)).findByIdWithHeart(cakeId);
	}

	@TestWithDisplayName("케이크 샵에 대하여 하트를 동작한다.")
	void heartCakeShop1() {
		// given
		final User user = getUser();
		final Long cakeShopId = 1L;
		final CakeShop cakeShop = getConstructorMonkey().giveMeBuilder(CakeShop.class)
			.set("shopName", Arbitraries.strings().withCharRange('a', 'z').ofMaxLength(30))
			.set("shopBio", Arbitraries.strings().withCharRange('a', 'z').ofMaxLength(40))
			.set("shopDescription", Arbitraries.strings().withCharRange('a', 'z').ofMaxLength(500))
			.set("location", supplyPointBy(Arbitraries.doubles().sample(), Arbitraries.doubles().sample()))
			.sample();

		doReturn(cakeShop).when(cakeShopReadFacade).findByIdWithHeart(cakeShopId);
		doNothing().when(userHeartFacade).heartCakeShop(user, cakeShop);

		// when & then
		assertDoesNotThrow(() -> heartService.heartCakeShop(user, cakeShopId));

		verify(cakeShopReadFacade, times(1)).findByIdWithHeart(cakeShopId);
		verify(userHeartFacade, times(1)).heartCakeShop(user, cakeShop);
	}

	@TestWithDisplayName("해당 케이크 샵이 없으면 하트 동작을 실패한다.")
	void heartCakeShop3() {
		// given
		final User user = getUser();
		final Long cakeShopId = 1L;

		doThrow(new CakkException(ReturnCode.NOT_EXIST_CAKE_SHOP)).when(cakeShopReadFacade).findByIdWithHeart(cakeShopId);

		// when & then
		assertThrows(
			CakkException.class,
			() -> heartService.heartCakeShop(user, cakeShopId),
			ReturnCode.NOT_EXIST_CAKE_SHOP.getMessage());

		verify(cakeShopReadFacade, times(1)).findByIdWithHeart(cakeShopId);
	}
}
