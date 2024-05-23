package com.cakk.api.common.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

@Target({ElementType.ANNOTATION_TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Test
@DisplayNameGeneration(TestWithDisplayName.TestDisplayNameGenerator.class)
public @interface TestWithDisplayName {

	String value() default "";

	class TestDisplayNameGenerator extends DisplayNameGenerator.Standard {

		@Override
		public String generateDisplayNameForClass(Class<?> testClass) {
			final TestWithDisplayName testWithDisplayName = testClass.getAnnotation(TestWithDisplayName.class);

			if (testWithDisplayName != null && !testWithDisplayName.value().isEmpty()) {
				return testWithDisplayName.value();
			}

			return super.generateDisplayNameForClass(testClass);
		}

		@Override
		public String generateDisplayNameForMethod(Class<?> testClass, Method testMethod) {
			return testMethod.getName();
		}
	}
}
