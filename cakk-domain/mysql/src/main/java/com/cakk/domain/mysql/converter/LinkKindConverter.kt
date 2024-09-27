package com.cakk.domain.mysql.converter

import com.cakk.common.enums.LinkKind
import com.cakk.common.enums.ReturnCode
import com.cakk.common.exception.CakkException
import jakarta.persistence.AttributeConverter
import java.util.stream.Stream

class LinkKindConverter : AttributeConverter<LinkKind?, String?> {

	override fun convertToDatabaseColumn(linkKind: LinkKind?): String? {
        return linkKind?.value
    }

    override fun convertToEntityAttribute(value: String?): LinkKind? {
        return if (value == null) {
            null
        } else Stream.of(*LinkKind.values())
                .filter { linkKind: LinkKind -> linkKind.value == value }
                .findFirst()
                .orElseThrow { CakkException(ReturnCode.INTERNAL_SERVER_ERROR) }
    }
}

