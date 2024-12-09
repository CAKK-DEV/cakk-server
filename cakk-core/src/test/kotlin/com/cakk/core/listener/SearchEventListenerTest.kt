package com.cakk.core.listener

import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.*

import io.kotest.assertions.throwables.shouldNotThrow

import net.jqwik.api.Arbitraries

import com.cakk.core.common.annotation.TestWithDisplayName
import com.cakk.core.common.base.MockitoTest
import com.cakk.core.dto.event.IncreaseSearchCountEvent
import com.cakk.infrastructure.cache.repository.KeywordRedisRepository

internal class SearchEventListenerTest : MockitoTest() {

    @InjectMocks
    private lateinit var searchEventListener: SearchEventListener

    @Mock
    private lateinit var keywordRedisRepository: KeywordRedisRepository

    private fun eventFixture(): IncreaseSearchCountEvent {
        return getConstructorMonkey().giveMeBuilder(IncreaseSearchCountEvent::class.java)
            .set("keyword", Arbitraries.strings().withCharRange('a', 'z').ofMinLength(5).ofMaxLength(10))
            .sample()
    }

    @TestWithDisplayName("키워드 조회수 상승 이벤트가 발행되면 increaseSearchCount 메서드가 호출되어야 한다.")
    fun increaseSearchCount() {
        // given
        val event: IncreaseSearchCountEvent = eventFixture()

        doNothing().`when`(keywordRedisRepository).saveOrIncreaseSearchCount(event.keyword)

        // when
		shouldNotThrow<Exception> {
			searchEventListener.increaseSearchCount(event)
		}

        // then
        verify(keywordRedisRepository, times(1)).saveOrIncreaseSearchCount(event.keyword)
    }
}
