package com.cakk.core.service.search

import org.springframework.stereotype.Service

import com.cakk.core.dto.param.search.TopSearchedListParam
import com.cakk.core.dto.response.search.TopSearchedListResponse
import com.cakk.core.mapper.supplyTopSearchedListResponseBy
import com.cakk.infrastructure.cache.repository.KeywordRedisRepository

@Service
class KeywordService(
	private val keywordRedisRepository: KeywordRedisRepository
) {

    fun findTopSearched(param: TopSearchedListParam): TopSearchedListResponse {
        val keywordList = keywordRedisRepository.findTopSearchedLimitCount(param.count)

        return supplyTopSearchedListResponseBy(keywordList)
    }
}
