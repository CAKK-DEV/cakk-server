package com.cakk.core.dto.response.shop

import com.cakk.core.dto.param.user.OwnerCandidateParam

data class CakeShopOwnerCandidatesResponse(
    val candidates: List<OwnerCandidateParam>
)

