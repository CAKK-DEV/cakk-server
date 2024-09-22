package com.cakk.api.validator;

import static java.util.Objects.*;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import com.cakk.api.annotation.OperationDay;
import com.cakk.common.enums.Days;
import com.cakk.core.dto.param.shop.ShopOperationParam;

public class OperationValidator implements ConstraintValidator<OperationDay, List<ShopOperationParam>> {

	@Override
	public boolean isValid(final List<ShopOperationParam> operationParams, final ConstraintValidatorContext context) {
		if (isNull(operationParams)) {
			return false;
		}

		final Map<Days, Boolean> days = new EnumMap<>(Days.class);
		for (ShopOperationParam operationParam : operationParams) {
			if (days.containsKey(operationParam.getOperationDay())) {
				return false;
			} else {
				days.put(operationParam.getOperationDay(), true);
			}
		}
		return true;
	}
}
