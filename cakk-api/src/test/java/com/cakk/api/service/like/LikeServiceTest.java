package com.cakk.api.service.like;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.DisplayName;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import net.jqwik.api.Arbitraries;

import com.cakk.api.common.annotation.TestWithDisplayName;
import com.cakk.api.common.base.ServiceTest;
import com.cakk.core.facade.cake.CakeShopReadFacade;
import com.cakk.core.facade.user.UserLikeFacade;
import com.cakk.domain.mysql.entity.shop.CakeShop;
import com.cakk.domain.mysql.entity.user.User;
@DisplayName("좋아요 기능 관련 비즈니스 로직 테스트")
public class LikeServiceTest extends ServiceTest {

	@InjectMocks
	private LikeService likeService;

	@Mock
	private CakeShopReadFacade cakeShopReadFacade;

	@Mock
	private UserLikeFacade userLikeFacade;

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

		doReturn(cakeShop).when(cakeShopReadFacade).findByIdWithLike(cakeShopId);
		doNothing().when(userLikeFacade).likeCakeShop(user, cakeShop);

		// when
		likeService.likeCakeShop(user, cakeShopId);

		// then
		assertDoesNotThrow(() -> likeService.likeCakeShop(user, cakeShopId));
	}
}
