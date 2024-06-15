package com.cakk.api.service.like;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.DisplayName;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import net.jqwik.api.Arbitraries;

import com.cakk.api.common.annotation.TestWithDisplayName;
import com.cakk.api.common.base.ServiceTest;
import com.cakk.common.enums.ReturnCode;
import com.cakk.common.exception.CakkException;
import com.cakk.domain.mysql.entity.shop.CakeShop;
import com.cakk.domain.mysql.entity.user.User;
import com.cakk.domain.mysql.repository.reader.CakeShopLikeReader;
import com.cakk.domain.mysql.repository.reader.CakeShopReader;
import com.cakk.domain.mysql.repository.writer.CakeShopLikeWriter;

@DisplayName("좋아요 기능 관련 비즈니스 로직 테스트")
public class LikeServiceTest extends ServiceTest {

	@InjectMocks
	private LikeService likeService;

	@Mock
	private CakeShopReader cakeShopReader;

	@Mock
	private CakeShopLikeReader cakeShopLikeReader;

	@Mock
	private CakeShopLikeWriter cakeShopLikeWriter;

	@TestWithDisplayName("좋아요 개수 50개가 넘지 않으면 유효성 검사에 통과한다.")
	void validateLikeCountSuccess() {
		// given
		final User user = getUser();
		final long cakeShopId = Arbitraries.longs().greaterOrEqual(1).sample();
		final int likeCount = Arbitraries.integers().lessOrEqual(49).sample();

		doReturn(likeCount).when(cakeShopLikeReader).countByCakeShopIdAndUser(cakeShopId, user);

		// when & then
		assertDoesNotThrow(() -> likeService.validateLikeCount(user, cakeShopId));
	}

	@TestWithDisplayName("좋아요 개수 50개가 넘으면 유효성 검사에서 에러를 반환한다.")
	void validateLikeCountFail() {
		// given
		final User user = getUser();
		final long cakeShopId = Arbitraries.longs().greaterOrEqual(1).sample();
		final int likeCount = Arbitraries.integers().greaterOrEqual(50).sample();

		doReturn(likeCount).when(cakeShopLikeReader).countByCakeShopIdAndUser(cakeShopId, user);

		// when & then
		assertThrows(
			CakkException.class,
			() -> likeService.validateLikeCount(user, cakeShopId),
			ReturnCode.MAX_CAKE_SHOP_LIKE.getMessage());
	}

	@TestWithDisplayName("케이크 샵 좋아요를 성공한다.")
	void likeCakeShop() {
		// given
		final User user = getUser();
		final long cakeShopId = Arbitraries.longs().greaterOrEqual(1).sample();
		final CakeShop cakeShop = getConstructorMonkey().giveMeBuilder(CakeShop.class)
			.set("shopName", Arbitraries.strings().withCharRange('a', 'z').ofMaxLength(30))
			.set("shopBio", Arbitraries.strings().withCharRange('a', 'z').ofMaxLength(40))
			.set("shopDescription", Arbitraries.strings().withCharRange('a', 'z').ofMaxLength(500))
			.set("location", supplyPointBy(Arbitraries.doubles().sample(), Arbitraries.doubles().sample()))
			.sample();

		doReturn(cakeShop).when(cakeShopReader).findById(cakeShopId);
		doNothing().when(cakeShopLikeWriter).like(cakeShop, user);

		// when
		likeService.likeCakeShop(user, cakeShopId);

		// then
		assertDoesNotThrow(() -> likeService.likeCakeShop(user, cakeShopId));
	}
}
