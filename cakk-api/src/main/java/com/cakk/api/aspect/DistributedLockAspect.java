package com.cakk.api.aspect;

import java.lang.reflect.Method;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

import com.cakk.api.annotation.DistributedLock;
import com.cakk.api.utils.CustomSpringExpressionLanguageParser;
import com.cakk.common.enums.RedisKey;
import com.cakk.domain.redis.dto.param.ExecuteWithLockParam;
import com.cakk.domain.redis.repository.LockRedisRepository;

@Aspect
@Component
@RequiredArgsConstructor
public class DistributedLockAspect {

	private final LockRedisRepository lockRedisRepository;

	private final AopForTransaction aopForTransaction;

	@Around("@annotation(com.cakk.api.annotation.DistributedLock)")
	public Object lock(final ProceedingJoinPoint joinPoint) {
		final MethodSignature signature = (MethodSignature) joinPoint.getSignature();
		final Method method = signature.getMethod();
		final DistributedLock distributedLock = method.getAnnotation(DistributedLock.class);

		String key = generateKey(method, distributedLock.key(), signature.getParameterNames(), joinPoint.getArgs());


		final ExecuteWithLockParam param = ExecuteWithLockParam.builder()
			.key(key)
			.waitTime(distributedLock.waitTime())
			.leaseTime(distributedLock.leaseTime())
			.timeUnit(distributedLock.timeUnit())
			.supplier(() -> aopForTransaction.proceed(joinPoint))
			.build();

		return lockRedisRepository.executeWithLock(param);
	}

	private String generateKey(final Method method, final String key, final String[] parameterNames, final Object[] args) {
		String generatedKey = RedisKey.getLockByMethodName(method.getName()).getValue();
		generatedKey += CustomSpringExpressionLanguageParser.getDynamicValue(key, parameterNames, args);

		return generatedKey;
	}
}
