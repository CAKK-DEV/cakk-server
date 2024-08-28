package com.cakk.domain.mysql.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.core.annotation.AliasFor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

/**
 * Indicates that an annotated class is a "Service" (e.g. a domain service object).
 *
 * <p>This annotation serves as a specialization of {@link Service @Service},
 * allowing for implementation classes to be autodetected through classpath scanning.
 *
 * @author komment
 * @see Component
 * @see Service
 */

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Service
public @interface DomainFacade {

	/**
	 * Alias for {@link Service#value}.
	 */
	@AliasFor(annotation = Service.class)
	String value() default "";
}
