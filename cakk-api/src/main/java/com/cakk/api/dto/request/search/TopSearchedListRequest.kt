package com.cakk.api.dto.request.search

import jakarta.validation.constraints.NotNull

data class TopSearchedListRequest(
	@field:NotNull
    val count: Long? = 10
)
