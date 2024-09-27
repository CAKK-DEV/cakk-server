package com.cakk.api.dto.request.link

import jakarta.validation.constraints.Size

data class UpdateLinkRequest(
	@field:Size(min = 1, max = 200)
	val instagram: String?,
	@field:Size(min = 1, max = 200)
	val kakao: String?,
	@field:Size(min = 1, max = 200)
	val web: String?
)
