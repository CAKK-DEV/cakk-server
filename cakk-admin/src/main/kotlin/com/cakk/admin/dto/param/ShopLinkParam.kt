package com.cakk.admin.dto.param

import com.cakk.common.enums.LinkKind
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class ShopLinkParam(
    @field:NotBlank
    val linkKind: LinkKind,
    @field:NotBlank @field:Size(min = 1, max = 200)
    val linkPath: String
)
