package com.cakk.api.annotation.validator;

import com.cakk.api.annotation.OperationDays;
import com.cakk.api.dto.request.shop.OperationDay;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class OperationValidator implements ConstraintValidator<OperationDays, OperationDay> {
	@Override
	public boolean isValid(OperationDay value, ConstraintValidatorContext context) {
		if (value.days().size() != value.startTimes().size()) {
			return false;
		} else if (value.days().size() != value.endTimes().size()) {
			return false;
		}
		return true;
	}
}
