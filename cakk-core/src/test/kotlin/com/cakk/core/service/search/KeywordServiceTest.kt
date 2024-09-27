package com.cakk.core.service.search

import io.kotest.matchers.shouldBe

import org.junit.jupiter.api.DisplayName
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.*

import com.cakk.core.common.annotation.TestWithDisplayName
import com.cakk.core.common.base.MockitoTest
import com.cakk.core.common.fixture.FixtureCommon.fixtureMonkey
import com.cakk.core.dto.param.search.TopSearchedListParam
import com.cakk.core.dto.response.search.TopSearchedListResponse
import com.cakk.domain.redis.repository.KeywordRedisRepository

@DisplayName("키워드 관련 비즈니스 로직 테스트")
internal class KeywordServiceTest : MockitoTest() {

	@InjectMocks
	private lateinit var keywordService: KeywordService

	@Mock
	private lateinit var keywordRedisRepository: KeywordRedisRepository

	@TestWithDisplayName("인기 검색어 리스트를 조회한다.")
	fun findTopSearched1() {
		// given
		val param = fixtureMonkey.giveMeOne(TopSearchedListParam::class.java)
		val keywordList: List<String> = getConstructorMonkey().giveMe(String::class.java, 3)

		doReturn(keywordList).`when`(keywordRedisRepository).findTopSearchedLimitCount(param.count)

		// when
		val response = keywordService.findTopSearched(param)

		// then
		response.keywordList shouldBe keywordList
		response.totalCount shouldBe keywordList.size

		verify(keywordRedisRepository, times(1)).findTopSearchedLimitCount(param.count)
	}

	@TestWithDisplayName("redis에 저장된 검색어가 없을 때, 인기 검색어 리스트를 조회 시 빈 배열을 반환한다.")
	fun findTopSearched2() {
		// given
		val param: TopSearchedListParam = getConstructorMonkey().giveMeOne(TopSearchedListParam::class.java)
		val keywordList = listOf<String>()

		doReturn(keywordList).`when`(keywordRedisRepository).findTopSearchedLimitCount(param.count)

		// when
		val response: TopSearchedListResponse = keywordService.findTopSearched(param)

		// then
		response.keywordList shouldBe keywordList
		response.totalCount shouldBe 0

		verify(keywordRedisRepository, times(1)).findTopSearchedLimitCount(param.count)
	}
}
