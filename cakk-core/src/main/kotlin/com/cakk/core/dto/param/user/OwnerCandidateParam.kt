package com.cakk.core.dto.param.user

import java.time.LocalDateTime

data class OwnerCandidateParam(
    val userId: Long,
    val nickname: String,
    val profileImageUrl: String,
    val email: String,
    val timestamp: LocalDateTime
)
