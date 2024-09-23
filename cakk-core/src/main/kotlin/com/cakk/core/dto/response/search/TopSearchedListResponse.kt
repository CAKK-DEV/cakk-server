package com.cakk.api.dto.response.search;

import java.util.List;

public record TopSearchedListResponse(
	List<String> keywordList,
	Integer totalCount
) {
}
