package com.cakk.admin.dto.response

import com.cakk.admin.dto.param.OwnerCandidateParam

data class CakeShopOwnerCandidatesResponse(
	val candidates: List<OwnerCandidateParam>
)
