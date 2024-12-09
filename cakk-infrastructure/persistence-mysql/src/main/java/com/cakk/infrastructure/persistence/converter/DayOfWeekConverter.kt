package com.cakk.infrastructure.persistence.converter

import com.cakk.common.enums.Days
import com.cakk.common.enums.ReturnCode
import com.cakk.common.exception.CakkException
import jakarta.persistence.AttributeConverter
import java.util.stream.Stream

class DayOfWeekConverter : AttributeConverter<Days?, Int?> {

	override fun convertToDatabaseColumn(days: Days?): Int? {
        return days?.code
    }

    override fun convertToEntityAttribute(code: Int?): Days? {
        return if (code == null) {
            null
        } else Stream.of(*Days.values())
                .filter { days: Days -> days.code == code }
                .findFirst()
                .orElseThrow { CakkException(ReturnCode.INTERNAL_SERVER_ERROR) }
    }
}

