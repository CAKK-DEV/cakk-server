package com.cakk.api.dto.request.cake

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size

import com.cakk.common.enums.CakeDesignCategory

data class CakeUpdateRequest(
	@field:NotBlank @field:Size(max = 200)
    val cakeImageUrl: String?,
	@field:NotNull
    val cakeDesignCategories: List<CakeDesignCategory>?,
	@field:NotNull
    val tagNames: List<String>?
)
