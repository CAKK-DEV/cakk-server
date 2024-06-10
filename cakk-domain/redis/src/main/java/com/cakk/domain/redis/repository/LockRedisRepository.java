package com.cakk.domain.redis.repository;

import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

import lombok.RequiredArgsConstructor;

import com.cakk.common.enums.RedisKey;
import com.cakk.common.enums.ReturnCode;
import com.cakk.common.exception.CakkException;
import com.cakk.domain.redis.annotation.RedisRepository;
import com.cakk.domain.redis.template.impl.RedisStringValueTemplate;

@RedisRepository
@RequiredArgsConstructor
public class LockRedisRepository {

	private final RedisStringValueTemplate redisStringValueTemplate;

	public void executeWithLock(final RedisKey key, final long timeout, Runnable task) {
		final String lockName = key.getValue();

		try {
			getLock(lockName, timeout);
			task.run();
		} finally {
			releaseLock(lockName);
		}
	}

	private void getLock(final String key, final long timeout) {
		final boolean result = redisStringValueTemplate.saveIfAbsent(key, "lock", timeout, TimeUnit.MILLISECONDS);

		checkResult(result);
	}

	private void releaseLock(final String key) {
		final boolean result = redisStringValueTemplate.delete(key);

		checkResult(result);
	}

	private void checkResult(final boolean result) {
		if (!result) {
			throw new CakkException(ReturnCode.LOCK_RESOURCES_ERROR);
		}
	}
}
