package com.cakk.api.integration.search

import java.util.stream.IntStream

import org.junit.jupiter.api.BeforeEach
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatusCode
import org.springframework.web.util.UriComponentsBuilder

import com.cakk.api.common.annotation.TestWithDisplayName
import com.cakk.api.common.base.IntegrationTest
import com.cakk.core.dto.response.search.TopSearchedListResponse
import com.cakk.common.enums.ReturnCode
import com.cakk.common.response.ApiResponse
import com.cakk.domain.redis.repository.KeywordRedisRepository
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import org.springframework.boot.test.web.server.LocalServerPort

class KeywordIntegrationTest(
	@LocalServerPort private val port: Int
) : IntegrationTest() {

	protected val baseUrl = "$localhost$port/api/v1/search"

    @Autowired
    private lateinit var keywordRedisRepository: KeywordRedisRepository

    @BeforeEach
    fun setUp() {
        IntStream.range(0, 10).forEach { keywordRedisRepository.saveOrIncreaseSearchCount("1위 검색어") }
        IntStream.range(0, 8).forEach { keywordRedisRepository.saveOrIncreaseSearchCount("2위 검색어") }
        IntStream.range(0, 6).forEach { keywordRedisRepository.saveOrIncreaseSearchCount("3위 검색어") }
        IntStream.range(0, 4).forEach { keywordRedisRepository.saveOrIncreaseSearchCount("4위 검색어") }
        IntStream.range(0, 3).forEach { keywordRedisRepository.saveOrIncreaseSearchCount("5위 검색어") }
        IntStream.range(0, 2).forEach { keywordRedisRepository.saveOrIncreaseSearchCount("6위 검색어") }
        IntStream.range(0, 1).forEach { keywordRedisRepository.saveOrIncreaseSearchCount("7위 검색어") }
    }

    @TestWithDisplayName("인기 검색어를 조회한다.")
    fun topSearched() {
        // given
        val url = "$baseUrl/top-searched"
        val count = 5
        val uriComponents = UriComponentsBuilder
            .fromUriString(url)
            .queryParam("count", count)
            .build()

        // when
        val responseEntity = restTemplate.getForEntity(
            uriComponents.toUriString(),
            ApiResponse::class.java
        )

        // then
        val response = objectMapper.convertValue(responseEntity.body, ApiResponse::class.java)
        val data = objectMapper.convertValue(response.data, TopSearchedListResponse::class.java)

		responseEntity.statusCode shouldBe HttpStatusCode.valueOf(200)
		response.returnCode shouldBe ReturnCode.SUCCESS.code
		response.returnMessage shouldBe ReturnCode.SUCCESS.message

		data.keywordList shouldHaveSize count
        data.keywordList.forEachIndexed { index, keyword -> keyword shouldBe "${index + 1}위 검색어" }
    }

    @TestWithDisplayName("검색 기록이 없을 경우, 인기 검색어 조회 시 빈 배열을 반환한다.")
    fun topSearched2() {
        // given
        keywordRedisRepository.clear()

        val url = "$baseUrl/top-searched"
        val count = 10
        val uriComponents = UriComponentsBuilder
            .fromUriString(url)
            .queryParam("count", count)
            .build()

        // when
        val responseEntity = restTemplate.getForEntity(
            uriComponents.toUriString(),
            ApiResponse::class.java
        )

        // then
        val response = objectMapper.convertValue(responseEntity.body, ApiResponse::class.java)
        val data = objectMapper.convertValue(response.data, TopSearchedListResponse::class.java)

		responseEntity.statusCode shouldBe HttpStatusCode.valueOf(200)
		response.returnCode shouldBe ReturnCode.SUCCESS.code
		response.returnMessage shouldBe ReturnCode.SUCCESS.message

		data.keywordList shouldHaveSize 0
    }
}
