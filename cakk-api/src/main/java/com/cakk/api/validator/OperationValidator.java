package com.cakk.api.validator;

import static java.util.Objects.*;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import com.cakk.api.annotation.OperationDay;
import com.cakk.api.dto.request.operation.ShopOperationParam;
import com.cakk.common.enums.Days;

public class OperationValidator implements ConstraintValidator<OperationDay, List<ShopOperationParam>> {

	@Override
	public boolean isValid(final List<ShopOperationParam> operationParams, final ConstraintValidatorContext context) {
		if (isNull(operationParams)) {
			return false;
		}

		final Map<Days, Boolean> days = new EnumMap<>(Days.class);
		for (ShopOperationParam operationParam : operationParams) {
			if (days.containsKey(operationParam.operationDay())) {
				return false;
			} else {
				days.put(operationParam.operationDay(), true);
			}
		}
		return true;
	}
}

