package com.cakk.admin.dto.request

import jakarta.validation.constraints.Size

data class LinkUpdateByAdminRequest(
	@field:Size(min = 1, max = 200)
	val instagram: String?,
	@field:Size(min = 1, max = 200)
	val kakao: String?,
	@field:Size(min = 1, max = 200)
	val web: String?
)
