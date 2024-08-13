package com.cakk.domain.mysql.converter;

import java.util.stream.Stream;

import jakarta.persistence.AttributeConverter;

import com.cakk.common.enums.ReturnCode;
import com.cakk.common.exception.CakkException;
import com.cakk.domain.mysql.bo.user.VerificationStatus;

public class VerificationStatusConverter implements AttributeConverter<VerificationStatus, Integer> {
	@Override
	public Integer convertToDatabaseColumn(VerificationStatus verificationStatus) {
		if (verificationStatus == null) {
			return null;
		}

		return verificationStatus.getCode();
	}

	@Override
	public VerificationStatus convertToEntityAttribute(Integer code) {
		if (code == null) {
			return null;
		}

		return Stream.of(VerificationStatus.values())
			.filter(verificationStatus -> verificationStatus.getCode().equals(code))
			.findFirst()
			.orElseThrow(() -> new CakkException(ReturnCode.INTERNAL_SERVER_ERROR));
	}
}

