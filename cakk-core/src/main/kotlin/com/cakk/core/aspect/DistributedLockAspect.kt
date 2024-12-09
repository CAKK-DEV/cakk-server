package com.cakk.core.aspect

import java.lang.reflect.Method

import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.reflect.MethodSignature
import org.springframework.stereotype.Component

import com.cakk.common.enums.RedisKey.Companion.getLockByMethodName
import com.cakk.core.annotation.DistributedLock
import com.cakk.core.utils.CustomSpringExpressionLanguageParser
import com.cakk.infrastructure.cache.dto.param.ExecuteWithLockParam
import com.cakk.infrastructure.cache.repository.LockRedisRepository

@Aspect
@Component
class DistributedLockAspect(
    private val lockRedisRepository: LockRedisRepository,
    private val aopForTransaction: AopForTransaction
) {

	@Around("@annotation(com.cakk.core.annotation.DistributedLock)")
	fun lock(joinPoint: ProceedingJoinPoint): Any? {
		val signature = joinPoint.signature as MethodSignature
		val method = signature.method
		val distributedLock = method.getAnnotation(DistributedLock::class.java)

		val key = generateKey(method, distributedLock.key, signature.parameterNames, joinPoint.args)

		val param = ExecuteWithLockParam.builder()
			.key(key)
			.waitTime(distributedLock.waitTime)
			.leaseTime(distributedLock.leaseTime)
			.timeUnit(distributedLock.timeUnit)
			.supplier { aopForTransaction.proceed(joinPoint) }
			.build()

		return lockRedisRepository.executeWithLock(param)
	}

	private fun generateKey(method: Method, key: String, parameterNames: Array<String>, args: Array<Any>): String {
		var generatedKey = getLockByMethodName(method.name).value
		generatedKey += CustomSpringExpressionLanguageParser.getDynamicValue(key, parameterNames, args)

		return generatedKey
	}
}
