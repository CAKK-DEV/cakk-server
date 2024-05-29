package com.cakk.api.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import com.cakk.api.annotation.validator.OperationValidator;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Constraint(validatedBy = OperationValidator.class)
public @interface OperationDays {

	String message() default "영업 일자 형식이 잘못됐습니다.";

	Class<?>[] groups() default { };

	Class<? extends Payload>[] payload() default { };
}
