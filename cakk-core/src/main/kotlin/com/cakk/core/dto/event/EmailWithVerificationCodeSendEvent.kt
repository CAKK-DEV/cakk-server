package com.cakk.core.dto.event

data class EmailWithVerificationCodeSendEvent(
    val email: String,
    val code: String
)
