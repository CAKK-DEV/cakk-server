package com.cakk.api.mapper;

import java.util.List;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import com.cakk.api.dto.param.operation.OwnerCandidateParam;
import com.cakk.api.dto.response.shop.CakeShopOwnerCandidateResponse;
import com.cakk.domain.mysql.entity.user.BusinessInformation;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BusinessInformationMapper {

	public static CakeShopOwnerCandidateResponse supplyCakeShopOwnerCandidateResponseBy(
		List<BusinessInformation> businessInformations
	) {
		List<OwnerCandidateParam> candidates = businessInformations
			.stream()
			.map(businessInformation ->
				OwnerCandidateParam.builder()
					.userId(businessInformation.getUser().getId())
					.nickname(businessInformation.getUser().getNickname())
					.profileImageUrl(businessInformation.getUser().getProfileImageUrl())
					.email(businessInformation.getUser().getEmail())
					.timestamp(businessInformation.getUpdatedAt())
					.build())
			.toList();

		return new CakeShopOwnerCandidateResponse(candidates);
	}
}

