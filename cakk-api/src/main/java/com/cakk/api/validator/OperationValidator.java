package com.cakk.api.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import com.cakk.api.annotation.OperationDay;
import com.cakk.api.dto.request.shop.OperationDays;

public class OperationValidator implements ConstraintValidator<OperationDay, OperationDays> {

	@Override
	public boolean isValid(OperationDays value, ConstraintValidatorContext context) {
		if (value.days().size() != value.startTimes().size()) {
			return false;
		} else if (value.days().size() != value.endTimes().size()) {
			return false;
		}
		return true;
	}
}

