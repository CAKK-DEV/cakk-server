package com.cakk.domain.converter;

import java.util.stream.Stream;

import jakarta.persistence.AttributeConverter;

import com.cakk.common.enums.Days;
import com.cakk.common.enums.ReturnCode;
import com.cakk.common.exception.CakkException;

public class DayOfWeekConverter implements AttributeConverter<Days, Integer> {

	@Override
	public Integer convertToDatabaseColumn(Days days) {
		if (days == null) {
			return null;
		}

		return days.getCode();
	}

	@Override
	public Days convertToEntityAttribute(Integer code) {
		if (code == null) {
			return null;
		}

		return Stream.of(Days.values())
			.filter(days -> days.getCode().equals(code))
			.findFirst()
			.orElseThrow(() -> new CakkException(ReturnCode.INTERNAL_SERVER_ERROR));
	}
}
