package com.cakk.api.dto.request.user

import java.time.LocalDate

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

import com.cakk.common.enums.Gender
import com.cakk.common.enums.ProviderType

data class UserSignUpRequest(
    @field:NotNull
    val providerType: ProviderType?,
    @field:NotBlank
    val idToken: String?,
    val deviceOs: String?,
    val deviceToken: String?,
    @field:NotBlank
    val nickname: String?,
    @field:NotBlank
    val email: String?,
    @field:NotNull
    val birthday: LocalDate?,
    @field:NotNull
    val gender: Gender?
)
