package com.cakk.api.validator;

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
	public boolean isValid(List<ShopOperationParam> operationParams, ConstraintValidatorContext context) {
		Map<Days, Boolean> days = new HashMap<>();

		for (ShopOperationParam operationParam : operationParams)
			if (days.containsKey(operationParam.operationDay())) {
				return false;
			} else {
				days.put(operationParam.operationDay(), true);
			}
		return true;
	}
}

