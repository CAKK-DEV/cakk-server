package com.cakk.api.common.annotation

import org.junit.jupiter.api.DisplayNameGeneration
import org.junit.jupiter.api.DisplayNameGenerator
import org.junit.jupiter.api.Test
import java.lang.reflect.Method

@Target(AnnotationTarget.ANNOTATION_CLASS, AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY_GETTER, AnnotationTarget.PROPERTY_SETTER)
@Retention(
	AnnotationRetention.RUNTIME
)
@MustBeDocumented
@Test
@DisplayNameGeneration(
	TestWithDisplayName.TestDisplayNameGenerator::class
)
annotation class TestWithDisplayName(val value: String = "") {

	class TestDisplayNameGenerator : DisplayNameGenerator.Standard() {
		override fun generateDisplayNameForClass(testClass: Class<*>): String {
			val testWithDisplayName = testClass.getAnnotation(
				TestWithDisplayName::class.java
			)

			if (testWithDisplayName != null && testWithDisplayName.value.isNotEmpty()) {
				return testWithDisplayName.value
			}

			return super.generateDisplayNameForClass(testClass)
		}

		override fun generateDisplayNameForMethod(testClass: Class<*>?, testMethod: Method): String {
			return testMethod.name
		}
	}
}
