package com.cakk.api.dto.request.user

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

import com.cakk.common.enums.ProviderType

data class UserSignInRequest(
    @field:NotNull
    val providerType: ProviderType?,
    @field:NotBlank
    val idToken: String?
)
