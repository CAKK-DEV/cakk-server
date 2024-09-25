package com.cakk.api.dto.param.operation;

import java.time.LocalDateTime;

import lombok.Builder;

@Builder
public record OwnerCandidateParam(
	Long userId,
	String nickname,
	String profileImageUrl,
	String email,
	LocalDateTime timestamp
) {
}
