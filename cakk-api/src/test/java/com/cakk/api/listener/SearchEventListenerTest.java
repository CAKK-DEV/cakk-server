package com.cakk.api.listener;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.mockito.InjectMocks;
import org.mockito.Mock;

import net.jqwik.api.Arbitraries;

import com.cakk.api.common.annotation.TestWithDisplayName;
import com.cakk.api.common.base.MockitoTest;
import com.cakk.api.dto.event.IncreaseSearchCountEvent;
import com.cakk.domain.redis.repository.KeywordRedisRepository;

class SearchEventListenerTest extends MockitoTest {

	@InjectMocks
	private SearchEventListener searchEventListener;

	@Mock
	private KeywordRedisRepository keywordRedisRepository;

	private IncreaseSearchCountEvent eventFixture() {
		return getConstructorMonkey().giveMeBuilder(IncreaseSearchCountEvent.class)
			.set("keyword", Arbitraries.strings().withCharRange('a', 'z').ofMinLength(5).ofMaxLength(10))
			.sample();
	}

	@TestWithDisplayName("키워드 조회수 상승 이벤트가 발행되면 increaseSearchCount 메서드가 호출되어야 한다.")
	void increaseSearchCount() {
		// given
		IncreaseSearchCountEvent event = eventFixture();

		doNothing().when(keywordRedisRepository).saveOrIncreaseSearchCount(event.keyword());

		// when
		assertDoesNotThrow(() -> searchEventListener.increaseSearchCount(event));

		// then
		verify(keywordRedisRepository, times(1)).saveOrIncreaseSearchCount(event.keyword());
	}

	@TestWithDisplayName("이벤트에 null 데이터가 포함돼 있으면, increaseSearchCount 메서드가 호출 시, 에러를 반환한다.")
	void increaseSearchCount2() {
		// given
		IncreaseSearchCountEvent event = new IncreaseSearchCountEvent(null);

		// when
		assertThrows(
			NullPointerException.class,
			() -> searchEventListener.increaseSearchCount(event)
		);

		// then
		verify(keywordRedisRepository, times(0)).saveOrIncreaseSearchCount(event.keyword());
	}
}
