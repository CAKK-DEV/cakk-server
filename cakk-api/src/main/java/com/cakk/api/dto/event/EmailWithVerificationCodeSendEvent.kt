package com.cakk.api.dto.event

data class EmailWithVerificationCodeSendEvent(
    val email: String,
    val code: String
)
