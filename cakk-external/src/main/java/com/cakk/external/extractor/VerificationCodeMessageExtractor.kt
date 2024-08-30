package com.cakk.external.extractor

import com.cakk.external.vo.VerificationMessage

fun interface VerificationCodeMessageExtractor<T> {

	fun extract(verificationMessage: VerificationMessage): T
}
