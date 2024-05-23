package com.cakk.api.service.cake;

import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;

import org.mockito.InjectMocks;
import org.mockito.Mock;

import com.cakk.api.common.annotation.TestWithDisplayName;
import com.cakk.api.common.base.ServiceTest;
import com.cakk.api.dto.request.cake.CakeSearchByCategoryRequest;
import com.cakk.api.dto.response.cake.CakeImageListResponse;
import com.cakk.common.enums.CakeDesignCategory;
import com.cakk.domain.dto.param.cake.CakeImageResponseParam;
import com.cakk.domain.repository.reader.CakeReader;

@DisplayName("케이크 조회 관련 비즈니스 로직 테스트")
class CakeServiceTest extends ServiceTest {

	@InjectMocks
	private CakeService cakeService;

	@Mock
	private CakeReader cakeReader;

	@TestWithDisplayName("카테고리에 해당하는 케이크 목록을 조회한다")
	void findCakeImagesByCursorAndCategory1() {
		// given
		CakeSearchByCategoryRequest dto = new CakeSearchByCategoryRequest(null, CakeDesignCategory.FLOWER, 3);
		List<CakeImageResponseParam> cakeImages = getConstructorMonkey().giveMe(CakeImageResponseParam.class, 3);

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
}
