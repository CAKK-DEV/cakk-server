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
import com.cakk.api.dto.request.like.LikeCakeSearchRequest;
import com.cakk.api.dto.response.like.LikeCakeImageListResponse;
import com.cakk.common.enums.RedisKey;
import com.cakk.common.enums.ReturnCode;
import com.cakk.common.exception.CakkException;
import com.cakk.domain.mysql.dto.param.like.LikeCakeImageResponseParam;
import com.cakk.domain.mysql.entity.cake.Cake;
import com.cakk.domain.mysql.entity.shop.CakeShop;
import com.cakk.domain.mysql.entity.shop.CakeShopLike;
import com.cakk.domain.mysql.entity.user.User;
import com.cakk.domain.mysql.repository.reader.CakeLikeReader;
import com.cakk.domain.mysql.repository.reader.CakeReader;
import com.cakk.domain.mysql.repository.reader.CakeShopLikeReader;
import com.cakk.domain.mysql.repository.reader.CakeShopReader;
import com.cakk.domain.mysql.repository.writer.CakeLikeWriter;
import com.cakk.domain.mysql.repository.writer.CakeShopLikeWriter;
import com.cakk.domain.redis.repository.LockRedisRepository;

@DisplayName("좋아요 기능 관련 비즈니스 로직 테스트")
public class LikeServiceTest extends ServiceTest {

	@InjectMocks
	private LikeService likeService;

	@Mock
	private CakeReader cakeReader;

	@Mock
	private CakeShopReader cakeShopReader;

	@Mock
	private CakeLikeReader cakeLikeReader;

	@Mock
	private CakeLikeWriter cakeLikeWriter;

	@Mock
	private CakeShopLikeReader cakeShopLikeReader;

	@Mock
	private CakeShopLikeWriter cakeShopLikeWriter;

	@Mock
	private LockRedisRepository lockRedisRepository;

	@TestWithDisplayName("좋아요 한 케이크 목록을 조회한다.")
	void findCakeImagesByCursorAndLike() {
		final LikeCakeSearchRequest dto = new LikeCakeSearchRequest(null, 5);
		final User user = getUser();
		final List<LikeCakeImageResponseParam> cakeImages =
			getConstructorMonkey().giveMeBuilder(LikeCakeImageResponseParam.class)
				.set("cakeShopId", Arbitraries.longs().greaterOrEqual(1))
				.set("cakeId", Arbitraries.longs().greaterOrEqual(1))
				.set("cakeLikeId", Arbitraries.longs().greaterOrEqual(1))
				.set("cakeImageUrl", Arbitraries.strings().alpha().ofMinLength(10).ofMaxLength(20))
				.sampleList(5);

		doReturn(cakeImages)
			.when(cakeLikeReader)
			.searchCakeImagesByCursorAndLike(dto.cakeLikeId(), user.getId(), dto.pageSize());

		// when
		final LikeCakeImageListResponse result = likeService.findCakeImagesByCursorAndLike(dto, user);

		// then
		Assertions.assertEquals(cakeImages, result.cakeImages());
		Assertions.assertNotNull(result.lastCakeLikeId());

		verify(cakeLikeReader, times(1))
			.searchCakeImagesByCursorAndLike(dto.cakeLikeId(), user.getId(), dto.pageSize());
	}

	@TestWithDisplayName("좋아요 한 케이크 목록 n번째 페이지를 조회한다.")
	void findCakeImagesByCursorAndLike2() {
		final LikeCakeSearchRequest dto = new LikeCakeSearchRequest(12L, 5);
		final User user = getUser();
		final List<LikeCakeImageResponseParam> cakeImages =
			getConstructorMonkey().giveMeBuilder(LikeCakeImageResponseParam.class)
				.set("cakeShopId", Arbitraries.longs().greaterOrEqual(1))
				.set("cakeId", Arbitraries.longs().greaterOrEqual(1))
				.set("cakeLikeId", Arbitraries.longs().between(1, 11))
				.set("cakeImageUrl", Arbitraries.strings().alpha().ofMinLength(10).ofMaxLength(20))
				.sampleList(5);

		doReturn(cakeImages)
			.when(cakeLikeReader)
			.searchCakeImagesByCursorAndLike(dto.cakeLikeId(), user.getId(), dto.pageSize());

		// when
		final LikeCakeImageListResponse result = likeService.findCakeImagesByCursorAndLike(dto, user);

		// then
		Assertions.assertEquals(cakeImages, result.cakeImages());
		Assertions.assertNotNull(result.lastCakeLikeId());

		verify(cakeLikeReader, times(1))
			.searchCakeImagesByCursorAndLike(dto.cakeLikeId(), user.getId(), dto.pageSize());
	}

	@TestWithDisplayName("좋아요 한 케이크가 없을 때 목록 조회 시 빈 배열을 반환한다.")
	void findCakeImagesByCursorAndLike3() {
		final LikeCakeSearchRequest dto = new LikeCakeSearchRequest(5L, 5);
		final User user = getUser();

		doReturn(List.of())
			.when(cakeLikeReader)
			.searchCakeImagesByCursorAndLike(dto.cakeLikeId(), user.getId(), dto.pageSize());

		// when
		final LikeCakeImageListResponse result = likeService.findCakeImagesByCursorAndLike(dto, user);

		// then
		Assertions.assertEquals(0, result.cakeImages().size());
		Assertions.assertNull(result.lastCakeLikeId());

		verify(cakeLikeReader, times(1))
			.searchCakeImagesByCursorAndLike(dto.cakeLikeId(), user.getId(), dto.pageSize());
	}

	@TestWithDisplayName("케이크에 대하여 좋아요를 동작한다.")
	void likeCake1() {
		// given
		final User user = getUser();
		final Long cakeId = 1L;
		final Cake cake = getConstructorMonkey().giveMeOne(Cake.class);

		doReturn(cake).when(cakeReader).findById(cakeId);
		doReturn(null).when(cakeLikeReader).findOrNullByUserAndCake(user, cake);

		// when & then
		assertDoesNotThrow(() -> likeService.likeCake(user, cakeId));

		verify(cakeReader, times(1)).findById(cakeId);
		verify(cakeLikeReader, times(1)).findOrNullByUserAndCake(user, cake);
	}

	@TestWithDisplayName("해당 케이크가 없으면 좋아요 동작을 실패한다.")
	void likeCake2() {
		// given
		final User user = getUser();
		final Long cakeId = 1L;
		final Cake cake = getConstructorMonkey().giveMeOne(Cake.class);

		doThrow(new CakkException(ReturnCode.NOT_EXIST_CAKE)).when(cakeReader).findById(cakeId);

		// when & then
		assertThrows(
			CakkException.class,
			() -> likeService.likeCake(user, cakeId),
			ReturnCode.NOT_EXIST_CAKE.getMessage());

		verify(cakeReader, times(1)).findById(cakeId);
		verify(cakeLikeReader, times(0)).findOrNullByUserAndCake(user, cake);
		verify(lockRedisRepository, times(0)).executeWithLock(any(RedisKey.class), anyLong(), any(Runnable.class));
	}

	@TestWithDisplayName("케이크 샵에 대하여 좋아요를 동작한다.")
	void likeCakeShop1() {
		// given
		final User user = getUser();
		final Long cakeShopId = 1L;
		final CakeShop cakeShop = getConstructorMonkey().giveMeBuilder(CakeShop.class)
			.set("shopName", Arbitraries.strings().withCharRange('a', 'z').ofMaxLength(30))
			.set("shopBio", Arbitraries.strings().withCharRange('a', 'z').ofMaxLength(40))
			.set("shopDescription", Arbitraries.strings().withCharRange('a', 'z').ofMaxLength(500))
			.set("location", supplyPointBy(Arbitraries.doubles().sample(), Arbitraries.doubles().sample()))
			.sample();

		doReturn(cakeShop).when(cakeShopReader).findById(cakeShopId);
		doReturn(null).when(cakeShopLikeReader).findOrNullByUserAndCakeShop(user, cakeShop);

		// when & then
		assertDoesNotThrow(() -> likeService.likeCakeShop(user, cakeShopId));

		verify(cakeShopReader, times(1)).findById(cakeShopId);
		verify(cakeShopLikeReader, times(1)).findOrNullByUserAndCakeShop(user, cakeShop);
	}

	@TestWithDisplayName("케이크 샵에 대하여 좋아요 취소를 동작한다.")
	void likeCakeShop2() {
		// given
		final User user = getUser();
		final Long cakeShopId = 1L;
		final CakeShop cakeShop = getConstructorMonkey().giveMeBuilder(CakeShop.class)
			.set("shopName", Arbitraries.strings().withCharRange('a', 'z').ofMaxLength(30))
			.set("shopBio", Arbitraries.strings().withCharRange('a', 'z').ofMaxLength(40))
			.set("shopDescription", Arbitraries.strings().withCharRange('a', 'z').ofMaxLength(500))
			.set("location", supplyPointBy(Arbitraries.doubles().sample(), Arbitraries.doubles().sample()))
			.sample();
		final CakeShopLike cakeShopLike = getConstructorMonkey().giveMeOne(CakeShopLike.class);

		doReturn(cakeShop).when(cakeShopReader).findById(cakeShopId);
		doReturn(cakeShopLike).when(cakeShopLikeReader).findOrNullByUserAndCakeShop(user, cakeShop);

		// when & then
		assertDoesNotThrow(() -> likeService.likeCakeShop(user, cakeShopId));

		verify(cakeShopReader, times(1)).findById(cakeShopId);
		verify(cakeShopLikeReader, times(1)).findOrNullByUserAndCakeShop(user, cakeShop);
	}

	@TestWithDisplayName("해당 케이크 샵이 없으면 좋아요 동작을 실패한다.")
	void likeCakeShop3() {
		// given
		final User user = getUser();
		final Long cakeShopId = 1L;

		doThrow(new CakkException(ReturnCode.NOT_EXIST_CAKE_SHOP)).when(cakeShopReader).findById(cakeShopId);

		// when & then
		assertThrows(
			CakkException.class,
			() -> likeService.likeCakeShop(user, cakeShopId),
			ReturnCode.NOT_EXIST_CAKE_SHOP.getMessage());

		verify(cakeShopReader, times(1)).findById(cakeShopId);
		verify(cakeShopLikeReader, times(0)).findOrNullByUserAndCakeShop(any(User.class), any(CakeShop.class));
	}
}
