package com.cakk.infrastructure.persistence.converter

import com.cakk.common.enums.ReturnCode
import com.cakk.common.enums.VerificationStatus
import com.cakk.common.exception.CakkException
import jakarta.persistence.AttributeConverter
import java.util.stream.Stream

class VerificationStatusConverter : AttributeConverter<VerificationStatus?, Int?> {

	override fun convertToDatabaseColumn(verificationStatus: VerificationStatus?): Int? {
        return verificationStatus?.code
    }

    override fun convertToEntityAttribute(code: Int?): VerificationStatus? {
        return if (code == null) {
            null
        } else Stream.of(*VerificationStatus.values())
                .filter { verificationStatus: VerificationStatus -> verificationStatus.code == code }
                .findFirst()
                .orElseThrow { CakkException(ReturnCode.INTERNAL_SERVER_ERROR) }
    }
}

