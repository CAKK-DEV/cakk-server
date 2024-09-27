package com.cakk.core.aspect

import org.aspectj.lang.ProceedingJoinPoint
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional

import com.cakk.common.exception.CakkException

@Component
class AopForTransaction {

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	fun proceed(joinPoint: ProceedingJoinPoint): Any? {
		return try {
			joinPoint.proceed()
		} catch (e: Throwable) {
			when (e) {
				is CakkException -> throw e
				else -> throw RuntimeException(e)
			}
		}
	}
}
