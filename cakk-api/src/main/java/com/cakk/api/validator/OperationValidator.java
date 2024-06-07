package com.cakk.api.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import com.cakk.api.annotation.OperationDay;
import com.cakk.api.dto.request.shop.OperationDayRequest;

public class OperationValidator implements ConstraintValidator<OperationDay, OperationDayRequest> {

	@Override
	public boolean isValid(OperationDayRequest value, ConstraintValidatorContext context) {
		if (value.days().size() != value.startTimes().size()) {
			return false;
		} else if (value.days().size() != value.endTimes().size()) {
			return false;
		}
		return true;
	}
}

