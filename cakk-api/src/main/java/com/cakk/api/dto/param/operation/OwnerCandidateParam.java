package com.cakk.api.dto.param.operation;

import java.time.LocalDateTime;

public record OwnerCandidateParam(
	Long userId,
	String nickname,
	String profileImageUrl,
	String email,
	LocalDateTime timestamp
) {
}
