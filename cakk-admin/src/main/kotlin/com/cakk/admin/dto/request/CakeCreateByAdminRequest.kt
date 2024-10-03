package com.cakk.admin.dto.request

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.Size

import com.cakk.common.enums.CakeDesignCategory

data class CakeCreateByAdminRequest(
	@field:NotBlank @field:Size(max = 100)
	val cakeImageUrl: String?,
	@field:NotEmpty
	val cakeDesignCategories: List<CakeDesignCategory>?,
	@field:NotEmpty
	val tagNames: List<String>?
)
