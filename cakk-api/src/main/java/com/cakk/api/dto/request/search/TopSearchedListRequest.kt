package com.cakk.api.dto.request.search;

import jakarta.validation.constraints.NotNull;

public record TopSearchedListRequest(
	@NotNull
	Long count
) {
}
