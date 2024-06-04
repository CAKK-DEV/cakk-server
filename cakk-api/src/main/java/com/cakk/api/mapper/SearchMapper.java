package com.cakk.api.mapper;

import java.util.List;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import com.cakk.api.dto.response.search.TopSearchedListResponse;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SearchMapper {

	public static TopSearchedListResponse supplyTopSearchedListResponseBy(final List<String> keywordList) {
		return new TopSearchedListResponse(keywordList, keywordList.size());
	}
}
