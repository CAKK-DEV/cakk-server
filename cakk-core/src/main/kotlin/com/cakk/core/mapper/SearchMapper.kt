package com.cakk.core.mapper

import com.cakk.core.dto.response.search.TopSearchedListResponse

fun supplyTopSearchedListResponseBy(keywordList: List<String>): TopSearchedListResponse {
	return TopSearchedListResponse(keywordList, keywordList.size)
}
