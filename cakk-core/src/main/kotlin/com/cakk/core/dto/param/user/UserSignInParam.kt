package com.cakk.core.dto.param.user

import com.cakk.common.enums.ProviderType

data class UserSignInParam(
    val providerType: ProviderType,
    val idToken: String
)

