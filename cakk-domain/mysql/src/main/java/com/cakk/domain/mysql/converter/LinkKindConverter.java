package com.cakk.domain.mysql.converter;

import java.util.stream.Stream;

import jakarta.persistence.AttributeConverter;

import com.cakk.common.enums.LinkKind;
import com.cakk.common.enums.ReturnCode;
import com.cakk.common.exception.CakkException;

public class LinkKindConverter implements AttributeConverter<LinkKind, String> {

	@Override
	public String convertToDatabaseColumn(final LinkKind linkKind) {
		if (linkKind == null) {
			return null;
		}

		return linkKind.getValue();
	}

	@Override
	public LinkKind convertToEntityAttribute(final String value) {
		if (value == null) {
			return null;
		}

		return Stream.of(LinkKind.values())
			.filter(linkKind -> linkKind.getValue().equals(value))
			.findFirst()
			.orElseThrow(() -> new CakkException(ReturnCode.INTERNAL_SERVER_ERROR));
	}
}
