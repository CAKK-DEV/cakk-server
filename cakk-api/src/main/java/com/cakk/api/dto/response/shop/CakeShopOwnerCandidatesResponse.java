package com.cakk.api.dto.response.shop;

import java.util.List;

import com.cakk.api.dto.param.operation.OwnerCandidateParam;

public record CakeShopOwnerCandidatesResponse(
	List<OwnerCandidateParam> candidates
) {
}

