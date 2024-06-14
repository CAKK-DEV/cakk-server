package com.cakk.api.service.cake;

import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;

import org.mockito.InjectMocks;
import org.mockito.Mock;

import net.jqwik.api.Arbitraries;

import com.cakk.api.common.annotation.TestWithDisplayName;
import com.cakk.api.common.base.ServiceTest;
import com.cakk.api.dto.request.cake.CakeSearchByCategoryRequest;
import com.cakk.api.dto.request.cake.CakeSearchByShopRequest;
import com.cakk.api.dto.request.cake.CakeSearchByViewsRequest;
import com.cakk.api.dto.response.cake.CakeImageListResponse;
import com.cakk.common.enums.CakeDesignCategory;
import com.cakk.domain.mysql.dto.param.cake.CakeImageResponseParam;
import com.cakk.domain.mysql.repository.reader.CakeReader;
import com.cakk.domain.redis.repository.CakeViewRedisRepository;

@DisplayName("케이크 조회 관련 비즈니스 로직 테스트")
class CakeServiceTest extends ServiceTest {

	@InjectMocks
	private CakeService cakeService;

	@Mock
	private CakeReader cakeReader;

	@Mock
	private CakeViewRedisRepository cakeViewRedisRepository;

	@TestWithDisplayName("카테고리에 해당하는 케이크 목록을 조회한다")
	void findCakeImagesByCursorAndCategory1() {
		// given
		CakeSearchByCategoryRequest dto = new CakeSearchByCategoryRequest(null, CakeDesignCategory.FLOWER, 3);
		List<CakeImageResponseParam> cakeImages = getConstructorMonkey().giveMeBuilder(CakeImageResponseParam.class)
			.set("cakeId", Arbitraries.longs().greaterOrEqual(1))
			.set("cakeShopId", Arbitraries.longs().greaterOrEqual(1))
			.set("cakeImageUrl", Arbitraries.strings().alpha().ofMinLength(10).ofMaxLength(20))
			.sampleList(3);

		doReturn(cakeImages).when(cakeReader).searchCakeImagesByCursorAndCategory(dto.cakeId(), dto.category(), dto.pageSize());

		// when
		CakeImageListResponse result = cakeService.findCakeImagesByCursorAndCategory(dto);

		// then
		Assertions.assertEquals(cakeImages.size(), result.cakeImages().size());
		Assertions.assertNotNull(result.lastCakeId());

		verify(cakeReader).searchCakeImagesByCursorAndCategory(dto.cakeId(), dto.category(), dto.pageSize());
	}

	@TestWithDisplayName("카테고리에 해당하는 케이크가 없을 시 빈 배열을 리턴한다.")
	void findCakeImagesByCursorAndCategory2() {
		// given
		CakeSearchByCategoryRequest dto = new CakeSearchByCategoryRequest(null, CakeDesignCategory.FLOWER, 3);
		List<CakeImageResponseParam> cakeImages = getConstructorMonkey().giveMe(CakeImageResponseParam.class, 0);

		doReturn(cakeImages).when(cakeReader).searchCakeImagesByCursorAndCategory(dto.cakeId(), dto.category(), dto.pageSize());

		// when
		CakeImageListResponse result = cakeService.findCakeImagesByCursorAndCategory(dto);

		// then
		Assertions.assertEquals(cakeImages.size(), result.cakeImages().size());
		Assertions.assertNull(result.lastCakeId());

		verify(cakeReader).searchCakeImagesByCursorAndCategory(dto.cakeId(), dto.category(), dto.pageSize());
	}

	@TestWithDisplayName("케이크 샵에 속한 케이크 목록을 조회한다")
	void findCakeImagesByCursorAndShop1() {
		// given
		CakeSearchByShopRequest dto = new CakeSearchByShopRequest(null, 1L, 3);
		List<CakeImageResponseParam> cakeImages = getConstructorMonkey().giveMeBuilder(CakeImageResponseParam.class)
			.set("cakeId", 1L)
			.set("cakeShopId", Arbitraries.longs().greaterOrEqual(1))
			.set("cakeImageUrl", Arbitraries.strings().alpha().ofMinLength(10).ofMaxLength(20))
			.sampleList(3);

		doReturn(cakeImages).when(cakeReader).searchCakeImagesByCursorAndCakeShopId(dto.cakeId(), dto.cakeShopId(), dto.pageSize());

		// when
		CakeImageListResponse result = cakeService.findCakeImagesByCursorAndCakeShopId(dto);

		// then
		Assertions.assertEquals(cakeImages.size(), result.cakeImages().size());
		Assertions.assertNotNull(result.lastCakeId());

		verify(cakeReader).searchCakeImagesByCursorAndCakeShopId(dto.cakeId(), dto.cakeShopId(), dto.pageSize());
	}

	@TestWithDisplayName("케이크 샵에 속한 케이크가 없을 시 빈 배열을 리턴한다.")
	void findCakeImagesByCursorAndShop2() {
		// given
		CakeSearchByShopRequest dto = new CakeSearchByShopRequest(null, 1L, 3);
		List<CakeImageResponseParam> cakeImages = getConstructorMonkey().giveMe(CakeImageResponseParam.class, 0);

		doReturn(cakeImages).when(cakeReader).searchCakeImagesByCursorAndCakeShopId(dto.cakeId(), dto.cakeShopId(), dto.pageSize());

		// when
		CakeImageListResponse result = cakeService.findCakeImagesByCursorAndCakeShopId(dto);

		// then
		Assertions.assertEquals(cakeImages.size(), result.cakeImages().size());
		Assertions.assertNull(result.lastCakeId());

		verify(cakeReader).searchCakeImagesByCursorAndCakeShopId(dto.cakeId(), dto.cakeShopId(), dto.pageSize());
	}

	@TestWithDisplayName("인기 케이크 목록을 조회한다")
	void searchCakeImagesByCursorAndViews1() {
		// given
		final long cursor = 0L;
		final int pageSize = 3;
		final CakeSearchByViewsRequest dto = new CakeSearchByViewsRequest(cursor, pageSize);
		final List<Long> cakeIds = List.of(1L, 2L, 3L);
		final List<CakeImageResponseParam> cakeImages = getConstructorMonkey().giveMeBuilder(CakeImageResponseParam.class)
			.set("cakeId", Arbitraries.longs().between(1, 3))
			.set("cakeShopId", Arbitraries.longs().greaterOrEqual(1))
			.set("cakeImageUrl", Arbitraries.strings().alpha().ofMinLength(10).ofMaxLength(20))
			.sampleList(3);

		doReturn(cakeIds).when(cakeViewRedisRepository).findTopCakeIdsByOffsetAndCount(cursor, pageSize);
		doReturn(cakeImages).when(cakeReader).searchCakeImagesByCakeIds(cakeIds);

		// when
		CakeImageListResponse result = cakeService.searchCakeImagesByCursorAndViews(dto);

		// then
		Assertions.assertNotNull(result.cakeImages());
		Assertions.assertNull(result.lastCakeId());

		verify(cakeViewRedisRepository, times(1)).findTopCakeIdsByOffsetAndCount(cursor, pageSize);
		verify(cakeReader, times(1)).searchCakeImagesByCakeIds(cakeIds);
	}

	@TestWithDisplayName("인기 케이크 목록이 없을 시 빈 배열을 조회한다")
	void searchCakeImagesByCursorAndViews2() {
		// given
		final long cursor = 0L;
		final int pageSize = 3;
		final CakeSearchByViewsRequest dto = new CakeSearchByViewsRequest(cursor, pageSize);

		doReturn(List.of()).when(cakeViewRedisRepository).findTopCakeIdsByOffsetAndCount(cursor, pageSize);

		// when
		CakeImageListResponse result = cakeService.searchCakeImagesByCursorAndViews(dto);

		// then
		Assertions.assertEquals(0, result.cakeImages().size());
		Assertions.assertNull(result.lastCakeId());

		verify(cakeViewRedisRepository, times(1)).findTopCakeIdsByOffsetAndCount(cursor, pageSize);
		verify(cakeReader, times(0)).searchCakeImagesByCakeIds(anyList());
	}
}
