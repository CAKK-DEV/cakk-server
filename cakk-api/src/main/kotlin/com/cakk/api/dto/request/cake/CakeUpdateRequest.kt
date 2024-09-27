package com.cakk.api.dto.request.cake

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.Size

import com.cakk.common.enums.CakeDesignCategory


data class CakeUpdateRequest(
	@field:NotBlank @field:Size(max = 200)
    val cakeImageUrl: String?,
	@field:NotEmpty
    val cakeDesignCategories: List<CakeDesignCategory>?,
	@field:NotEmpty
    val tagNames: List<String>?
)
