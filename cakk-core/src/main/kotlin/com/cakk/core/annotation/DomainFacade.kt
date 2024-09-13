package com.cakk.core.annotation

import org.springframework.core.annotation.AliasFor
import org.springframework.stereotype.Component

/**
 * Indicates that an annotated class is a "Service" (e.g. a domain service object).
 *
 *
 * This annotation serves as a specialization of [@Component][Component],
 * allowing for implementation classes to be autodetected through classpath scanning.
 *
 * @author komment
 * @see Component
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
@Component
annotation class DomainFacade(

	/**
	 * Alias for [Component.value].
	 */
	@get:AliasFor(annotation = Component::class) val value: String = ""
)
