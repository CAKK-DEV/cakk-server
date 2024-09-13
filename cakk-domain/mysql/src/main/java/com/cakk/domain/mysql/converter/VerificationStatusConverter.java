package com.cakk.domain.mysql.converter;

import java.util.stream.Stream;

import jakarta.persistence.AttributeConverter;

import com.cakk.common.enums.ReturnCode;
import com.cakk.common.enums.VerificationStatus;
import com.cakk.common.exception.CakkException;

public class VerificationStatusConverter implements AttributeConverter<VerificationStatus, Integer> {
	@Override
	public Integer convertToDatabaseColumn(VerificationStatus verificationStatus) {
		if (verificationStatus == null) {
			return null;
		}

		return verificationStatus.code;
	}

	@Override
	public VerificationStatus convertToEntityAttribute(Integer code) {
		if (code == null) {
			return null;
		}

		return Stream.of(VerificationStatus.values())
			.filter(verificationStatus -> verificationStatus.code == code)
			.findFirst()
			.orElseThrow(() -> new CakkException(ReturnCode.INTERNAL_SERVER_ERROR));
	}
}