package com.cakk.core.dto.response.search

data class TopSearchedListResponse(
    val keywordList: List<String>,
    val totalCount: Int
)
