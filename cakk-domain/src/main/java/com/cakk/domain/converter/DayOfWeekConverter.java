package com.cakk.domain.converter;

import java.util.stream.Stream;

import com.cakk.common.enums.DayOfWeek;
import com.cakk.common.enums.ReturnCode;
import com.cakk.common.exception.CakkException;

import jakarta.persistence.AttributeConverter;

public class DayOfWeekConverter implements AttributeConverter<DayOfWeek, Integer> {

	@Override
	public Integer convertToDatabaseColumn(DayOfWeek dayOfWeek) {
		if (dayOfWeek == null) {
			return null;
		}

		return dayOfWeek.getCode();
	}

	@Override
	public DayOfWeek convertToEntityAttribute(Integer code) {
		if (code == null) {
			return null;
		}

		return Stream.of(DayOfWeek.values())
			.filter(dayOfWeek -> dayOfWeek.getCode().equals(code))
			.findFirst()
			.orElseThrow(() -> new CakkException(ReturnCode.INTERNAL_SERVER_ERROR));
	}
}
