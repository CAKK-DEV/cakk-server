package com.cakk.api.service.like;

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
import com.cakk.domain.mysql.dto.param.like.LikeCakeImageResponseParam;
import com.cakk.domain.mysql.entity.user.User;
import com.cakk.domain.mysql.repository.reader.CakeLikeReader;

@DisplayName("좋아요 기능 관련 비즈니스 로직 테스트")
public class LikeServiceTest extends ServiceTest {

	@InjectMocks
	private LikeService likeService;

	@Mock
	private CakeLikeReader cakeLikeReader;

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

		verify(cakeLikeReader, times(1)).searchCakeImagesByCursorAndLike(dto.cakeLikeId(), user.getId(), dto.pageSize());
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

		verify(cakeLikeReader, times(1)).searchCakeImagesByCursorAndLike(dto.cakeLikeId(), user.getId(), dto.pageSize());
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

		verify(cakeLikeReader, times(1)).searchCakeImagesByCursorAndLike(dto.cakeLikeId(), user.getId(), dto.pageSize());
	}
}
